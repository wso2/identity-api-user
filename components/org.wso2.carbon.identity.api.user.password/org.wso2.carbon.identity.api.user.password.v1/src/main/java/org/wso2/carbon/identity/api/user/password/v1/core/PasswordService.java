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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.user.password.common.Constants;
import org.wso2.carbon.identity.api.user.password.v1.model.PasswordUpdateRequest;
import org.wso2.carbon.identity.api.user.password.v1.model.PasswordUpdateResponse;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.Locale;
import javax.ws.rs.core.Response;

/**
 * Service class for password management operations.
 */
public class PasswordService {

    private static final Log LOG = LogFactory.getLog(PasswordService.class);
    private final RealmService realmService;

    public PasswordService(RealmService realmService) {

        this.realmService = realmService;
    }

    /**
     * Update the password for the authenticated user.
     *
     * @param passwordUpdateRequest Password update request containing current and new passwords
     * @return PasswordUpdateResponse with status and message
     */
    public PasswordUpdateResponse updatePassword(PasswordUpdateRequest passwordUpdateRequest) {

        validatePasswordUpdateRequest(passwordUpdateRequest);

        User user = ContextLoader.getUserFromContext();

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        int tenantId = IdentityTenantUtil.getTenantId(tenantDomain);

        try {
            UserRealm userRealm = realmService.getTenantUserRealm(tenantId);
            if (userRealm == null) {
                throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                        Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_PASSWORD);
            }

            UserStoreManager userStoreManager = userRealm.getUserStoreManager();
            String username = user.getUserName();
            String userStoreDomain = user.getUserStoreDomain();

            if (StringUtils.isNotEmpty(userStoreDomain)) {
                username = userStoreDomain + "/" + username;
            }

            // Authenticate with current password
            boolean authenticated = userStoreManager.authenticate(username,
                    passwordUpdateRequest.getCurrentPassword());

            if (!authenticated) {
                throw handleException(Response.Status.BAD_REQUEST,
                        Constants.ErrorMessage.ERROR_CODE_INVALID_CURRENT_PASSWORD);
            }

            // Update password
            userStoreManager.updateCredential(username, passwordUpdateRequest.getNewPassword(),
                    passwordUpdateRequest.getCurrentPassword());

            PasswordUpdateResponse response = new PasswordUpdateResponse();
            response.setStatus("SUCCESS");
            response.setMessage("Password updated successfully");

            return response;

        } catch (UserStoreException e) {
            throw handleUserStoreException(e, user.getUserName());
        }
    }

    /**
     * Validate password update request.
     *
     * @param passwordUpdateRequest Password update request
     */
    private void validatePasswordUpdateRequest(PasswordUpdateRequest passwordUpdateRequest) {

        if (passwordUpdateRequest == null) {
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_INVALID_INPUT);
        }

        if (StringUtils.isEmpty(passwordUpdateRequest.getCurrentPassword()) ||
                StringUtils.isEmpty(passwordUpdateRequest.getNewPassword())) {
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_INVALID_INPUT);
        }

        if (passwordUpdateRequest.getCurrentPassword().equals(passwordUpdateRequest.getNewPassword())) {
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_INVALID_INPUT);
        }
    }

    /**
     * Handle UserStoreException and return appropriate APIError.
     *
     * @param e        UserStoreException
     * @param username Username for logging context
     * @return APIError
     */
    private APIError handleUserStoreException(UserStoreException e, String username) {

        Constants.ErrorMessage errorMessage;
        Response.Status status;

        if (LOG.isDebugEnabled()) {
            LOG.debug("UserStoreException occurred while updating password for user: " + username, e);
        }

        // Check if it's a password policy violation
        if (e.getMessage() != null && e.getMessage().toLowerCase(Locale.ENGLISH).contains("policy")) {
            errorMessage = Constants.ErrorMessage.ERROR_CODE_PASSWORD_POLICY_VIOLATION;
            status = Response.Status.BAD_REQUEST;
            return new APIError(status, getErrorBuilder(errorMessage).build(LOG, e.getMessage()));
        }

        // Default to internal server error
        errorMessage = Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_PASSWORD;
        status = Response.Status.INTERNAL_SERVER_ERROR;
        return new APIError(status, getErrorBuilder(errorMessage).build(LOG, e, errorMessage.getDescription()));
    }

    /**
     * Handle exceptions generated in API.
     *
     * @param status       HTTP Status
     * @param errorMessage Error message enum
     * @return APIError
     */
    private APIError handleException(Response.Status status, Constants.ErrorMessage errorMessage) {

        return new APIError(status, getErrorBuilder(errorMessage).build());
    }

    /**
     * Get error response builder with error details.
     *
     * @param errorMessage Error message enum
     * @return ErrorResponse.Builder
     */
    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMessage) {

        return new ErrorResponse.Builder()
                .withCode(errorMessage.getCode())
                .withMessage(errorMessage.getMessage())
                .withDescription(errorMessage.getDescription());
    }
}

