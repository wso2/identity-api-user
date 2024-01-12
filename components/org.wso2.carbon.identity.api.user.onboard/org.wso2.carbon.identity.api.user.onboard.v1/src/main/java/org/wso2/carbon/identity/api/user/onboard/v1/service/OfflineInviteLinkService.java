/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.user.onboard.v1.service;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.onboard.common.error.APIError;
import org.wso2.carbon.identity.api.user.onboard.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.user.onboard.common.util.Constants;
import org.wso2.carbon.identity.api.user.onboard.v1.model.InvitationRequest;
import org.wso2.carbon.identity.recovery.IdentityRecoveryClientException;
import org.wso2.carbon.identity.recovery.IdentityRecoveryConstants;
import org.wso2.carbon.identity.recovery.IdentityRecoveryException;
import org.wso2.carbon.identity.user.onboard.core.service.model.Configuration;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.onboard.common.UserOnboardServiceDataHolder.getUserOnboardCoreService;

/**
 * This class provides the implementation of the OfflineInviteLinkService.
 */
public class OfflineInviteLinkService {

    public static final String AUTH_USER_TENANT_DOMAIN = "authUserTenantDomain";

    private static final Log LOG = LogFactory.getLog(OfflineInviteLinkService.class);

    private static final List<String> CONFLICT_ERROR_SCENARIOS = Arrays.asList(
            IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_LOCKED_ACCOUNT.getCode(),
            IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_DISABLED_ACCOUNT.getCode());

    private static final List<String> NOT_FOUND_ERROR_SCENARIOS = Arrays.asList(
            IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_INVALID_USER.getCode(),
            IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_USER_STORE_INVALID.getCode()

    );

    /**
     * This method generates the password reset URL.
     *
     * @param invitationRequest Invitation request.
     * @return password reset URL.
     */
    public String generatePasswordURL(InvitationRequest invitationRequest) {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        Configuration configuration = prepareConfigObject(invitationRequest, tenantDomain);
        try {
            return getUserOnboardCoreService().generatePasswordResetLink(configuration);
        } catch (IdentityRecoveryException e) {
            throw handleException(e, Constants.ErrorMessages.ERROR_UNABLE_TO_GENERATE_INVITE_LINK,
                    invitationRequest.getUsername());
        }
    }

    private Configuration prepareConfigObject(InvitationRequest invitationRequest, String tenantDomain) {
        return new Configuration(invitationRequest.getUsername(), invitationRequest.getUserstore(), tenantDomain);
    }

    private APIError handleException(IdentityRecoveryException exception, Constants.ErrorMessages errorEnum,
                                     String... data) {

        ErrorResponse errorResponse;
        Response.Status status;
        if (exception instanceof IdentityRecoveryClientException) {
            status = Response.Status.BAD_REQUEST;
            if (isConflictScenario(exception.getErrorCode())) {
                status = Response.Status.CONFLICT;
            } else if (isNotFoundScenario(exception.getErrorCode())) {
                status = Response.Status.NOT_FOUND;
            }

            errorResponse = getErrorBuilder(exception, errorEnum, data)
                    .build(LOG, exception, buildErrorDescription(errorEnum.getDescription(), data), true);

        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
            errorResponse = getErrorBuilder(errorEnum, data).
                    build(LOG, exception, buildErrorDescription(errorEnum.getDescription(), data),
                            false);
        }
        return new APIError(status, errorResponse);
    }


    private boolean isConflictScenario(String errorCode) {

        return !StringUtils.isBlank(errorCode) && CONFLICT_ERROR_SCENARIOS.contains(errorCode);
    }

    private boolean isNotFoundScenario(String errorCode) {

        return !StringUtils.isBlank(errorCode) && NOT_FOUND_ERROR_SCENARIOS.contains(errorCode);
    }
    private ErrorResponse.Builder getErrorBuilder(IdentityRecoveryException exception,
                                                  Constants.ErrorMessages errorEnum, String... data) {

        String errorCode = (StringUtils.isBlank(exception.getErrorCode())) ?
                errorEnum.getCode() : exception.getErrorCode();
        String description = (StringUtils.isBlank(exception.getMessage())) ?
                errorEnum.getDescription() : exception.getMessage();
        return new ErrorResponse.Builder()
                .withCode(errorCode)
                .withMessage(errorEnum.getMessage())
                .withDescription(buildErrorDescription(description, data));
    }

    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessages errorEnum, String... data) {

        return new ErrorResponse.Builder()
                .withCode(errorEnum.getCode())
                .withMessage(errorEnum.getMessage())
                .withDescription(buildErrorDescription(errorEnum.getDescription(), data));
    }

    private String buildErrorDescription(String description, String... data) {

        if (ArrayUtils.isNotEmpty(data)) {
            return String.format(description, (Object[]) data);
        }
        return description;
    }

}
