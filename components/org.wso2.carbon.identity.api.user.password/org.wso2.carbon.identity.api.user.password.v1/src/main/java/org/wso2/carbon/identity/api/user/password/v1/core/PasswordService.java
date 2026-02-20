/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.user.password.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.user.password.common.Constants;
import org.wso2.carbon.identity.api.user.password.common.PasswordServiceHolder;
import org.wso2.carbon.identity.api.user.password.v1.model.PasswordChangeRequest;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.central.log.mgt.utils.LoggerUtils;
import org.wso2.carbon.identity.core.context.IdentityContext;
import org.wso2.carbon.identity.core.context.model.Flow;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.event.IdentityEventConstants;
import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.event.event.Event;
import org.wso2.carbon.identity.mgt.policy.PolicyViolationException;
import org.wso2.carbon.identity.user.action.api.constant.UserActionError;
import org.wso2.carbon.identity.user.action.api.exception.UserActionExecutionClientException;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.UserCoreConstants;
import org.wso2.carbon.user.core.UserStoreClientException;
import org.wso2.carbon.user.core.common.AbstractUserStoreManager;
import org.wso2.carbon.user.core.constants.UserCoreErrorConstants;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.user.core.util.UserCoreUtil;
import org.wso2.carbon.utils.DiagnosticLog;

import java.util.HashMap;

import javax.ws.rs.core.Response;

/**
 * Service class for password change operations.
 */
public class PasswordService {

    private static final Log LOG = LogFactory.getLog(PasswordService.class);

    private static final String ERROR_CODE_PASSWORD_HISTORY_VIOLATION = "22001";
    private static final String ERROR_CODE_INVALID_CREDENTIAL = "30003";
    private static final String ERROR_CODE_INVALID_CREDENTIAL_DURING_UPDATE = "36001";
    private static final String ERROR_CODE_READ_ONLY_USERSTORE = "30002";

    private final RealmService realmService;

    /**
     * Constructs a PasswordService with the given RealmService.
     *
     * @param realmService RealmService used to access tenant user realms.
     */
    public PasswordService(RealmService realmService) {

        this.realmService = realmService;
    }

    /**
     * Change the password for the authenticated user.
     *
     * @param passwordChangeRequest Password change request containing current and new passwords.
     * @throws APIError if the current password is a mismatch or the password update encounters an error.
     */
    public void changePassword(PasswordChangeRequest passwordChangeRequest) {

        validatePasswordChangeRequest(passwordChangeRequest);

        User user = ContextLoader.getUserFromContext();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        int tenantId = IdentityTenantUtil.getTenantId(tenantDomain);

        try {
            UserRealm userRealm = realmService.getTenantUserRealm(tenantId);
            if (userRealm == null) {
                throw handleError(Response.Status.INTERNAL_SERVER_ERROR,
                        Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_PASSWORD);
            }

            AbstractUserStoreManager userStoreManager =
                    (AbstractUserStoreManager) userRealm.getUserStoreManager();
            String username = user.getUserName();
            String userStoreDomain = user.getUserStoreDomain();

            if (StringUtils.isNotEmpty(userStoreDomain)) {
                username = userStoreDomain + UserCoreConstants.DOMAIN_SEPARATOR + username;
            }

            // Validate the current password by authenticating the user.
            boolean isAuthenticated = userStoreManager.authenticate(username,
                    passwordChangeRequest.getCurrentPassword());
            if (!isAuthenticated) {
                throw handleError(Response.Status.BAD_REQUEST,
                        Constants.ErrorMessage.ERROR_CODE_INVALID_CURRENT_PASSWORD);
            }

            String userID = userStoreManager.getUserIDFromUserName(username);
            if (StringUtils.isEmpty(userID)) {
                throw handleError(Response.Status.INTERNAL_SERVER_ERROR,
                        Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_PASSWORD);
            }

            // Enter password update flow for context transfer to downstream handlers.
            IdentityContext.getThreadLocalIdentityContext().enterFlow(
                    new Flow.CredentialFlowBuilder()
                            .name(Flow.Name.CREDENTIAL_UPDATE)
                            .credentialType(Flow.CredentialType.PASSWORD)
                            .initiatingPersona(Flow.InitiatingPersona.USER)
                            .build());
            try {
                userStoreManager.updateCredentialByAdminWithID(userID,
                        passwordChangeRequest.getNewPassword());
                publishCredentialUpdateEvent(username, tenantDomain, tenantId,
                        passwordChangeRequest.getNewPassword());
            } finally {
                IdentityContext.getThreadLocalIdentityContext().exitFlow();
            }

        } catch (UserStoreClientException e) {
            handleClientExceptionForActionFailure(e);
            throw handleUserStoreClientException(e);
        } catch (UserStoreException e) {
            // Sometimes client exceptions are wrapped in the super class.
            // Therefore checking for possible client exception.
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof UserStoreClientException) {
                throw handleUserStoreClientException((UserStoreClientException) rootCause);
            }
            handleExceptionOnPasswordPolicy(e);
            throw handleUserStoreException(e, user.getUserName());
        }
    }

    /**
     * Publish credential update event after a successful password change.
     *
     * @param username     Username of the user.
     * @param tenantDomain Tenant domain.
     * @param tenantId     Tenant ID.
     * @param credential   New credential.
     */
    private void publishCredentialUpdateEvent(String username, String tenantDomain, int tenantId,
                                              String credential) {

        HashMap<String, Object> properties = new HashMap<>();
        properties.put(IdentityEventConstants.EventProperty.USER_NAME,
                UserCoreUtil.removeDomainFromName(username));
        properties.put(IdentityEventConstants.EventProperty.TENANT_DOMAIN, tenantDomain);
        properties.put(IdentityEventConstants.EventProperty.TENANT_ID, tenantId);
        properties.put(IdentityEventConstants.EventProperty.USER_STORE_DOMAIN,
                IdentityUtil.extractDomainFromName(username));
        properties.put(IdentityEventConstants.EventProperty.CREDENTIAL, credential);
        properties.put(IdentityEventConstants.EventProperty.SCENARIO,
                IdentityEventConstants.EventProperty.Scenario.ScenarioTypes.POST_CREDENTIAL_UPDATE_BY_USER);

        Event event = new Event(IdentityEventConstants.Event.POST_UPDATE_CREDENTIAL_BY_ME_API, properties);
        try {
            PasswordServiceHolder.getIdentityEventService().handleEvent(event);
        } catch (IdentityEventException | IllegalStateException e) {
            LOG.error("Error while publishing POST_UPDATE_CREDENTIAL_BY_ME_API event for user: " +
                    maskIfRequired(username), e);
            if (LoggerUtils.isDiagnosticLogsEnabled()) {
                LoggerUtils.triggerDiagnosticLogEvent(new DiagnosticLog.DiagnosticLogBuilder(
                        Constants.LogConstants.USER_PASSWORD_API,
                        Constants.LogConstants.CHANGE_PASSWORD)
                        .inputParam("username", maskIfRequired(username))
                        .resultMessage("Failed to publish POST_UPDATE_CREDENTIAL_BY_ME_API event after " +
                                "successful password change. Post-update handlers may not have executed.")
                        .logDetailLevel(DiagnosticLog.LogDetailLevel.APPLICATION)
                        .resultStatus(DiagnosticLog.ResultStatus.FAILED));
            }
        }
    }

    /**
     * Validate the password change request by ensuring the current and new passwords are not identical.
     *
     * @param passwordChangeRequest Password change request.
     * @throws APIError if the current and new passwords are the same.
     */
    private void validatePasswordChangeRequest(PasswordChangeRequest passwordChangeRequest) {

        if (passwordChangeRequest == null || passwordChangeRequest.getCurrentPassword() == null ||
                passwordChangeRequest.getNewPassword() == null) {
            throw handleError(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_INVALID_REQUEST_BODY);
        }
        if (passwordChangeRequest.getCurrentPassword().equals(passwordChangeRequest.getNewPassword())) {
            throw handleError(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_SAME_AS_CURRENT_PASSWORD);
        }
    }

    /**
     * Handle exceptions caused due to password policy violations (invalid credentials,
     * policy violations, or password history violations).
     * 
     * @param e UserStoreException to check for password policy violations.
     * @throws APIError if the exception is caused by a password policy violation.
     */
    private void handleExceptionOnPasswordPolicy(UserStoreException e) {

        Throwable current = e;
        int depth = 0;

        while (current != null && depth < 10) {
            String errorMessage = current.getMessage();

            if (current instanceof UserStoreException && errorMessage != null &&
                    (errorMessage.contains(ERROR_CODE_INVALID_CREDENTIAL) ||
                            errorMessage.contains(ERROR_CODE_INVALID_CREDENTIAL_DURING_UPDATE))) {
                throw handleClientException(
                        Constants.ErrorMessage.ERROR_CODE_INVALID_CURRENT_PASSWORD, errorMessage);
            }

            if (current instanceof PolicyViolationException) {
                throw handleClientException(
                        Constants.ErrorMessage.ERROR_CODE_PASSWORD_POLICY_VIOLATION, errorMessage);
            }

            if (current instanceof IdentityEventException && StringUtils.equals(
                    ERROR_CODE_PASSWORD_HISTORY_VIOLATION,
                    ((IdentityEventException) current).getErrorCode())) {
                throw handleClientException(
                        Constants.ErrorMessage.ERROR_CODE_PASSWORD_POLICY_VIOLATION, errorMessage);
            }

            current = current.getCause();
            depth++;
        }
    }

    /**
     * Handle pre-update-password action execution failures.
     *
     * @param e UserStoreClientException to check for action failure.
     * @throws APIError if the exception is caused by a pre-update-password action execution failure.
     */
    private void handleClientExceptionForActionFailure(UserStoreClientException e) {

        /* Note that UserActionExecutionClientException has been wrapped multiple times at this point and needs to be
         unwrapped accordingly. Since there is a possibility of altering the error stack and not constructing the
         failure response correctly, integration tests are added to detect any regressions.
         */
        if (UserActionError.PRE_UPDATE_PASSWORD_ACTION_EXECUTION_FAILED.equals(e.getErrorCode()) &&
                e.getCause() != null && e.getCause().getCause() != null &&
                e.getCause().getCause().getCause() instanceof UserActionExecutionClientException) {
            UserActionExecutionClientException actionException =
                    (UserActionExecutionClientException) e.getCause().getCause().getCause();
            throw handleClientException(
                    Constants.ErrorMessage.ERROR_CODE_PASSWORD_UPDATE_ACTION_FAILURE,
                    actionException.getDescription());
        }

        if (e instanceof UserActionExecutionClientException) {
            UserActionExecutionClientException actionException = (UserActionExecutionClientException) e;
            throw handleClientException(
                    Constants.ErrorMessage.ERROR_CODE_PASSWORD_UPDATE_ACTION_FAILURE,
                    actionException.getDescription());
        }
    }

    /**
     * Handle generic UserStoreClientException.
     *
     * @param e UserStoreClientException.
     * @return APIError.
     */
    private APIError handleUserStoreClientException(UserStoreClientException e) {

        return handleClientException(
                Constants.ErrorMessage.ERROR_CODE_PASSWORD_UPDATE_CLIENT_ERROR, e.getMessage());
    }

    /**
     * Handle generic UserStoreException by returning an appropriate APIError.
     * Returns a client error for read-only user store scenarios and a server error for all other cases.
     *
     * @param e        UserStoreException.
     * @param username Username for logging context.
     * @return APIError with the appropriate HTTP status and error details.
     */
    private APIError handleUserStoreException(UserStoreException e, String username) {

        // Check for read-only user store scenario which is a client error.
        if (StringUtils.contains(e.getMessage(), ERROR_CODE_READ_ONLY_USERSTORE) ||
                (e instanceof org.wso2.carbon.user.core.UserStoreException && StringUtils
                        .equals(UserCoreErrorConstants.ErrorMessages.ERROR_CODE_READONLY_USER_STORE.getCode(),
                                ((org.wso2.carbon.user.core.UserStoreException) e).getErrorCode()))) {

            return handleError(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_READ_ONLY_USERSTORE);
        }

        // For other UserStoreExceptions, treat them as server errors.
        return handleServerException(
                Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_PASSWORD, e, username);
                        
    }

    /**
     * Handle errors generated in API.
     *
     * @param status       HTTP Status.
     * @param errorMessage Error message enum.
     * @return APIError.
     */
    private APIError handleError(Response.Status status, Constants.ErrorMessage errorMessage) {

        return new APIError(status, getErrorBuilder(errorMessage).build());
    }

    /**
     * Handle client errors (400 Bad Request).
     *
     * @param errorMessage Error message enum.
     * @param description  Dynamic error description from the caught exception.
     * @return APIError.
     */
    private APIError handleClientException(Constants.ErrorMessage errorMessage, String description) {

        return new APIError(Response.Status.BAD_REQUEST,
                getErrorBuilder(errorMessage)
                        .withDescription(description)
                        .build(LOG, description));
    }

    /**
     * Handle server errors (500 Internal Server Error).
     *
     * @param errorMessage Error message enum.
     * @param e            Exception to log.
     * @param username     Username for logging context.
     * @return APIError.
     */
    private APIError handleServerException(Constants.ErrorMessage errorMessage, Exception e, String username) {

        String logMessage = String.format("%s User: %s", errorMessage.getDescription(),
                maskIfRequired(username));
        return new APIError(Response.Status.INTERNAL_SERVER_ERROR,
                getErrorBuilder(errorMessage)
                        .build(LOG, e, logMessage));
    }

    /**
     * Get error response builder with error details.
     *
     * @param errorMessage Error message enum.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMessage) {

        return new ErrorResponse.Builder()
                .withCode(errorMessage.getCode())
                .withMessage(errorMessage.getMessage())
                .withDescription(errorMessage.getDescription());
    }

    /**
     * Mask the given value if log masking is enabled.
     *
     * @param value Value to mask.
     * @return Masked value if log masking is enabled, otherwise the original value.
     */
    private String maskIfRequired(String value) {

        return LoggerUtils.isLogMaskingEnable ? LoggerUtils.getMaskedContent(value) : value;
    }
}
