/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.rest.api.user.mfa.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.user.mfa.common.MFAConstants;
import org.wso2.carbon.identity.application.authentication.framework.exception.AuthenticationFailedException;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.rest.api.user.mfa.v1.dto.EnabledAuthenticatorsDTO;
import org.wso2.carbon.identity.rest.api.user.mfa.v1.util.UserMFAServiceHolder;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.api.UserStoreManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.mfa.common.MFAConstants.BACKUP_CODE_AUTHENTICATOR;
import static org.wso2.carbon.identity.api.user.mfa.common.MFAConstants.ENABLED_AUTHENTICATORS_CLAIM;
import static org.wso2.carbon.identity.api.user.mfa.common.MFAConstants.ErrorMessage.SERVER_ERROR_RETRIEVE_CLAIM_USERSTORE;
import static org.wso2.carbon.identity.api.user.mfa.common.MFAConstants.ErrorMessage.SERVER_ERROR_RETRIEVING_USERSTORE_MANAGER;
import static org.wso2.carbon.identity.api.user.mfa.common.MFAConstants.ErrorMessage.SERVER_ERROR_UPDATING_CLAIM_USERSTORE;
import static org.wso2.carbon.identity.api.user.mfa.common.MFAConstants.ErrorMessage.USER_ERROR_ACCESS_DENIED_FOR_BASIC_AUTH;
import static org.wso2.carbon.identity.api.user.mfa.common.MFAConstants.ErrorMessage.USER_ERROR_INVALID_AUTHENTICATOR;
import static org.wso2.carbon.identity.api.user.mfa.common.MFAConstants.ErrorMessage.USER_ERROR_UNAUTHORIZED_USER;
import static org.wso2.carbon.identity.api.user.mfa.common.MFAConstants.TOTP_AUTHENTICATOR;

/**
 * Service class for MFA endpoint.
 */
public class MFAService {

    private static final Log log = LogFactory.getLog(MFAService.class);

    /**
     * Uses to get enabled authenticators of the authenticated user.
     *
     * @return A DTO object contains enabledAuthenticators.
     */
    public EnabledAuthenticatorsDTO getEnabledAuthenticators() {

        if (!isValidAuthenticationType()) {
            throw handleError(Response.Status.FORBIDDEN, USER_ERROR_ACCESS_DENIED_FOR_BASIC_AUTH);
        }
        try {
            UserStoreManager userStoreManager =
                    UserMFAServiceHolder.getRealmService().getTenantUserRealm(IdentityTenantUtil
                            .getTenantId(getTenantDomain())).getUserStoreManager();
            if (userStoreManager == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Unable to retrieve userstore manager.");
                }
                throw handleError(Response.Status.INTERNAL_SERVER_ERROR, SERVER_ERROR_RETRIEVING_USERSTORE_MANAGER);
            }
            Map<String, String> claims = userStoreManager.getUserClaimValues(getUserName(),
                    new String[]{ENABLED_AUTHENTICATORS_CLAIM}, null);
            EnabledAuthenticatorsDTO enabledAuthenticatorsDTO = new EnabledAuthenticatorsDTO();
            enabledAuthenticatorsDTO.setEnabledAuthenticators(claims.get(ENABLED_AUTHENTICATORS_CLAIM));
            return enabledAuthenticatorsDTO;
        } catch (UserStoreException e) {
            throw handleException(e, SERVER_ERROR_RETRIEVE_CLAIM_USERSTORE, getUserName());
        }
    }

    /**
     * Uses to update enabled authenticators of the authenticated user.
     *
     * @param enabledAuthenticators Enabled authenticators.
     * @return Updated enabledAuthenticator List.
     */
    public EnabledAuthenticatorsDTO updateEnabledAuthenticators(String enabledAuthenticators) {

        if (!isValidAuthenticationType()) {
            throw handleError(Response.Status.FORBIDDEN, USER_ERROR_ACCESS_DENIED_FOR_BASIC_AUTH);
        }
        if (StringUtils.isNotBlank(enabledAuthenticators)) {
            validateAuthenticatorList(enabledAuthenticators);
        }
        try {
            UserStoreManager userStoreManager =
                    UserMFAServiceHolder.getRealmService().getTenantUserRealm(IdentityTenantUtil
                            .getTenantId(getTenantDomain())).getUserStoreManager();
            Map<String, String> claims = new HashMap<>();
            if (userStoreManager == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Unable to retrieve userstore manager.");
                }
                throw handleError(Response.Status.INTERNAL_SERVER_ERROR, SERVER_ERROR_RETRIEVING_USERSTORE_MANAGER);
            }
            claims.put(ENABLED_AUTHENTICATORS_CLAIM, enabledAuthenticators);
            userStoreManager.setUserClaimValues(getUserName(), claims, null);
            EnabledAuthenticatorsDTO enabledAuthenticatorsDTO = new EnabledAuthenticatorsDTO();
            enabledAuthenticatorsDTO.setEnabledAuthenticators(enabledAuthenticators);
            return enabledAuthenticatorsDTO;
        } catch (UserStoreException e) {
            throw handleException(e, SERVER_ERROR_UPDATING_CLAIM_USERSTORE, getUserName());
        }
    }

    /**
     * Validate if the user input contains invalid values for authenticators.
     *
     * @param enabledAuthenticators Enabled authenticators.
     */
    private void validateAuthenticatorList(String enabledAuthenticators) {

        List<String> enabledAuthList = new ArrayList<>(Arrays.asList(enabledAuthenticators.split(",")));
        for (String authenticator : enabledAuthList) {
            /*
             * TODO Use configs to get available authenticator list
             * TODO Track in https://github.com/wso2-enterprise/asgardeo-product/issues/10533
             */
            if (!TOTP_AUTHENTICATOR.equals(authenticator) &&
                    !BACKUP_CODE_AUTHENTICATOR.equals(authenticator)) {
                throw handleError(Response.Status.BAD_REQUEST, USER_ERROR_INVALID_AUTHENTICATOR);
            }
        }
    }

    private String getUserName() {

        return ContextLoader.getUsernameFromContext();
    }

    private String getTenantDomain() {

        return ContextLoader.getTenantDomainFromContext();
    }

    /**
     * Handle Exceptions.
     *
     * @param e         Exception
     * @param errorEnum Error message enum.
     * @return An APIError.
     */
    private APIError handleException(Exception e, MFAConstants.ErrorMessage errorEnum, String... data) {

        ErrorResponse errorResponse;
        if (data != null) {
            errorResponse = getErrorBuilder(errorEnum).build(log, e, String.format(errorEnum.getDescription(),
                    (Object[]) data));
        } else {
            errorResponse = getErrorBuilder(errorEnum).build(log, e, errorEnum.getDescription());
        }

        if (e instanceof AuthenticationFailedException) {
            return handleError(Response.Status.UNAUTHORIZED, USER_ERROR_UNAUTHORIZED_USER);
        } else if (e instanceof UserStoreException) {
            return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        } else {
            return new APIError(Response.Status.BAD_REQUEST, errorResponse);
        }
    }

    /**
     * Handle User errors.
     *
     * @param status Http status.
     * @param error  Error .
     * @return An APIError.
     */
    private APIError handleError(Response.Status status, MFAConstants.ErrorMessage error) {

        return new APIError(status, getErrorBuilder(error).build());
    }

    /**
     * Get ErrorResponse Builder for Error enum.
     *
     * @param errorEnum Error message enum.
     * @return Error response for the given errorEnum.
     */
    private ErrorResponse.Builder getErrorBuilder(MFAConstants.ErrorMessage errorEnum) {

        return new ErrorResponse.Builder().withCode(errorEnum.getCode()).withMessage(errorEnum.getMessage())
                .withDescription(errorEnum.getDescription());
    }

    /**
     * Used to validate if this API call is called using basic auth or not.
     *
     * @return True if this API called with basic auth.
     */
    private boolean isValidAuthenticationType() {

        /*
          Check whether the request is authenticated with basic auth. MFA endpoint should not be allowed for basic
          authentication. This approach can be improved by providing a Level of Assurance (LOA) and checking that in
          MFAService.
         */
        if (Boolean.parseBoolean(
                (String) IdentityUtil.threadLocalProperties.get().get(MFAConstants.AUTHENTICATED_WITH_BASIC_AUTH))) {
            if (log.isDebugEnabled()) {
                log.debug("Not a valid authentication method. "
                        + "This method is blocked for the requests with basic authentication.");
            }
            return false;
        }
        return true;
    }

}
