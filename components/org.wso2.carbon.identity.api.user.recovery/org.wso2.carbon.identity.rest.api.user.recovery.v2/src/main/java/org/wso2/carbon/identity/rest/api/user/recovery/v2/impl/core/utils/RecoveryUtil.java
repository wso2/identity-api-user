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

package org.wso2.carbon.identity.rest.api.user.recovery.v2.impl.core.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.core.ServiceURLBuilder;
import org.wso2.carbon.identity.core.URLBuilderException;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.recovery.IdentityRecoveryClientException;
import org.wso2.carbon.identity.recovery.IdentityRecoveryConstants;
import org.wso2.carbon.identity.recovery.IdentityRecoveryException;
import org.wso2.carbon.identity.recovery.dto.NotificationChannelDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.impl.core.APICalls;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.impl.core.Constants;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.impl.core.exceptions.PreconditionFailedException;

import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.APICall;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.Property;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.RecoveryChannel;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.RetryErrorResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.UserClaim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.TENANT_CONTEXT_PATH_COMPONENT;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.Error.UNEXPECTED_SERVER_ERROR;

/**
 * Contains the recovery endpoint utils.
 */
public class RecoveryUtil {

    private static final Log LOG = LogFactory.getLog(RecoveryUtil.class);

    /**
     * Converts a list of UserClaim in to a UserClaim array.
     *
     * @param userClaimsList UserClaims List.
     * @return Map of user claims.
     */
    public static Map<String, String> buildUserClaimsMap(List<UserClaim> userClaimsList) {

        Map<String, String> userClaims = new HashMap<>();
        for (UserClaim userClaimModel : userClaimsList) {
            userClaims.put(userClaimModel.getUri(), userClaimModel.getValue());
        }
        return userClaims;
    }

    /**
     * Convert the list of Properties in to an array.
     *
     * @param propertyList List of {@link Property} objects.
     * @return Map of properties.
     */
    public static Map<String, String> buildPropertiesMap(List<Property> propertyList) {

        Map<String, String> properties = new HashMap<>();
        if (propertyList == null) {
            return properties;
        }
        for (Property propertyDTO : propertyList) {
            properties.put(propertyDTO.getKey(), propertyDTO.getValue());
        }
        return properties;
    }

    /**
     * Build the channel response object list.
     *
     * @param channels Available notification channels list as objects of {@link NotificationChannelDTO}.
     * @return List of RecoveryChannels {@link RecoveryChannel}.
     */
    public static List<RecoveryChannel> buildRecoveryChannelInformation(NotificationChannelDTO[] channels) {

        List<RecoveryChannel> recoveryChannelDTOs = new ArrayList<>();
        if (channels != null) {
            // Create a response object and add the details to each object.
            for (NotificationChannelDTO channel : channels) {
                RecoveryChannel recoveryChannel = new RecoveryChannel();
                recoveryChannel.setId(Integer.toString(channel.getId()));
                recoveryChannel.setType(channel.getType());
                recoveryChannel.setValue(channel.getValue());
                if (StringUtils.isNotEmpty(channel.getValue())) {
                    recoveryChannel.setPreferred(channel.isPreferred());
                }
                recoveryChannelDTOs.add(recoveryChannel);
            }
        }
        return recoveryChannelDTOs;
    }

    /**
     * Build API call information.
     *
     * @param type   Type of the API call.
     * @param rel    API relation.
     * @param apiUrl Url of the API.
     * @param data   Additional data.
     * @return APICall {@link APICall} which encapsulates the API name and the url.
     */
    public static APICall buildApiCall(String type, String rel, String apiUrl, String data) {

        if (StringUtils.isNotEmpty(data)) {
            apiUrl = String.format(apiUrl, data);
        }
        APICall apiCall = new APICall();
        apiCall.setType(type);
        apiCall.setRel(rel);
        apiCall.setHref(apiUrl);
        return apiCall;
    }

    /**
     * Builds URI prepending the user API context with the proxy context path to the endpoint.
     * Ex: /t/<tenant-domain>/api/users/<endpoint>
     *
     * @param endpoint Relative endpoint path.
     * @return Relative URI.
     */
    public static String buildURIForBody(String tenantDomain, String endpoint, String baseUrl) {

        String url;
        String context = getContext(tenantDomain, endpoint, baseUrl);

        try {
            url = ServiceURLBuilder.create().addPath(context).build().getRelativePublicURL();
        } catch (URLBuilderException e) {
            String errorDescription = "Server encountered an error while building URL for response body.";
            ErrorResponse errorResponse =
                    new org.wso2.carbon.identity.api.user.common.error.ErrorResponse.Builder()
                    .withCode(UNEXPECTED_SERVER_ERROR.getCode())
                    .withMessage("Error while building response.")
                    .withDescription(errorDescription)
                    .build(LOG, e, errorDescription);

            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw new APIError(status, errorResponse);
        }
        return url;
    }

    /**
     * Handle errors with specific http codes.
     *
     * @param e             Identity Recovery Exception.
     * @param tenantDomain  Tenant domain.
     * @param scenario      Recovery scenario.
     * @param correlationId Correlation Id.
     * @return WebApplicationException (NOTE: Returns null when the client error is for no user available or for
     * multiple users available.
     */
    public static WebApplicationException handleIdentityRecoveryException(IdentityRecoveryException e,
                                                                          String tenantDomain, String scenario,
                                                                          String correlationId) {

        return handleIdentityRecoveryException(e, tenantDomain, scenario, StringUtils.EMPTY, correlationId);
    }

    /**
     * Handle errors with specific http codes.
     *
     * @param e Identity    Recovery Exception.
     * @param scenario      Recovery scenario.
     * @param tenantDomain  Tenant domain.
     * @param code          Recovery code.
     * @param correlationId Correlation Id.
     * @return WebApplicationException (NOTE: Returns null when the client error is for no user available or for
     * multiple users available.
     */
    public static WebApplicationException handleIdentityRecoveryException(IdentityRecoveryException e,
                                                                          String tenantDomain, String scenario,
                                                                          String code, String correlationId) {

        String errorCode = prependOperationScenarioToErrorCode(e.getErrorCode(), scenario);
        String errorDescription = e.getMessage();
        String serverErrorDescription = Constants.STATUS_INTERNAL_SERVER_ERROR_DESCRIPTION_DEFAULT;
        String errorMessage = Constants.STATUS_INTERNAL_SERVER_ERROR_MESSAGE_DEFAULT;
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

        if (IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_PASSWORD_RECOVERY_WITH_NOTIFICATIONS_NOT_ENABLED.
                getCode().equals(e.getErrorCode()) || IdentityRecoveryConstants.ErrorMessages.
                ERROR_CODE_USERNAME_RECOVERY_NOT_ENABLED.getCode().equals(e.getErrorCode()) ||
                IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_DISABLED_ACCOUNT.getCode().equals(e.getErrorCode())
                || IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_LOCKED_ACCOUNT.getCode().equals(e.getErrorCode())
                || IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_PASSWORD_RECOVERY_NOT_ENABLED.getCode().equals(
                        e.getErrorCode())) {
            errorMessage = Constants.STATUS_FORBIDDEN_MESSAGE_DEFAULT;
            status = Response.Status.FORBIDDEN;
        } else if (IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_USER_TENANT_DOMAIN_MISS_MATCH_WITH_CONTEXT.
                getCode().equals(e.getErrorCode())) {
            errorMessage = Constants.STATUS_CONFLICT_MESSAGE_DEFAULT;
            status = Response.Status.CONFLICT;
        } else if (IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_MULTIPLE_MATCHING_USERS.getCode().equals(
                e.getErrorCode())) {
            // If user notify is not enabled, throw an accepted response.
            if (!Boolean.parseBoolean(IdentityUtil
                    .getProperty(IdentityRecoveryConstants.ConnectorConfig.NOTIFY_USER_EXISTENCE))) {
                return new WebApplicationException(Response.accepted().build());
            }
            errorMessage = Constants.STATUS_CONFLICT_MESSAGE_DEFAULT;
            status = Response.Status.CONFLICT;
        } else if (IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_NO_USER_FOUND.getCode().equals(e.
                getErrorCode())) {
            // If user notify is not enabled, throw an accepted response.
            if (!Boolean.parseBoolean(IdentityUtil
                    .getProperty(IdentityRecoveryConstants.ConnectorConfig.NOTIFY_USER_EXISTENCE))) {
                return new WebApplicationException(Response.accepted().build());
            }
            errorMessage = Constants.STATUS_NOT_FOUND_MESSAGE_DEFAULT;
            status = Response.Status.NOT_FOUND;
        } else if (IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_NO_ACCOUNT_RECOVERY_DATA.getCode().equals(
                e.getErrorCode()) || IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_NO_VERIFIED_CHANNELS_FOR_USER.
                getCode().equals(e.getErrorCode())) {
            errorMessage = Constants.STATUS_NOT_FOUND_MESSAGE_DEFAULT;
            status = Response.Status.NOT_FOUND;
        } else if (IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_INVALID_RECOVERY_CODE.getCode().equals(
                e.getErrorCode()) || IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_INVALID_RESEND_CODE.getCode().
                equals(e.getErrorCode()) || IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_EXPIRED_RECOVERY_CODE.
                getCode().equals(e.getErrorCode()) || IdentityRecoveryConstants.ErrorMessages.
                ERROR_CODE_INVALID_RECOVERY_FLOW_ID.getCode().equals(e.getErrorCode()) || IdentityRecoveryConstants.
                ErrorMessages.ERROR_CODE_EXPIRED_RECOVERY_FLOW_ID.getCode().equals(e.getErrorCode()) ||
                IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_NO_RECOVERY_FLOW_DATA.getCode().equals(
                        e.getErrorCode())) {
            errorMessage = Constants.STATUS_METHOD_NOT_ACCEPTED_MESSAGE_DEFAULT;
            status = Response.Status.NOT_ACCEPTABLE;
        } else if (IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_PASSWORD_HISTORY_VIOLATION.getCode().equals(
                e.getErrorCode()) || IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_PASSWORD_POLICY_VIOLATION.
                getCode().equals(e.getErrorCode())) {
            return RecoveryUtil.buildRetryPasswordResetObject(tenantDomain, errorDescription, errorCode, code,
                    correlationId);
        } else if (IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_INVALID_CHANNEL_ID.getCode().equals(
                e.getErrorCode())) {
            errorMessage = Constants.STATUS_BAD_REQUEST_DEFAULT;
            status = Response.Status.BAD_REQUEST;
        }

        if (e instanceof IdentityRecoveryClientException) {
            status = Response.Status.BAD_REQUEST;

            if (IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_PRE_UPDATE_PASSWORD_ACTION_FAILURE.getCode()
                    .equals(e.getErrorCode())) {
                IdentityRecoveryClientException identityRecoveryClientException = (IdentityRecoveryClientException) e;
                ErrorResponse errorResponse =
                        buildErrorResponse(errorCode, identityRecoveryClientException.getMessage(),
                                identityRecoveryClientException.getDescription());
                return new APIError(status, errorResponse);
            }
            return buildClientError(errorCode, errorMessage, errorDescription, status);
        }
        return buildServerError(e, e.getErrorCode(), errorMessage, serverErrorDescription, status);
    }

    /**
     * Builds API error to be thrown.
     *
     * @param errorCode Error code.
     * @param errorMessage Error message.
     * @param errorDescription Error description.
     * @param status HTTP status.
     * @return APIError object which contains the error description.
     */
    private static APIError buildClientError(String errorCode, String errorMessage, String errorDescription,
                                           Response.Status status) {

        ErrorResponse errorResponse = buildErrorResponse(errorCode, errorMessage, errorDescription);
        return new APIError(status, errorResponse);
    }

    /**
     * Builds API error to be thrown for server errors.
     *
     * @param e Identity Recovery Exception.
     * @param errorCode Error code.
     * @param errorMessage Error message.
     * @param errorDescription Error description.
     * @param status HTTP status.
     * @return APIError object which contains the error description.
     */
    private static APIError buildServerError(IdentityRecoveryException e, String errorCode, String errorMessage,
                                                     String errorDescription, Response.Status status) {

        ErrorResponse errorResponse = buildServerErrorResponse(e, errorCode, errorMessage, errorDescription);
        return new APIError(status, errorResponse);
    }

    /**
     * Builds the API context on whether the tenant qualified url is enabled or not. In tenant qualified mode the
     * ServiceURLBuilder appends the tenant domain to the URI as a path param automatically. But
     * in non tenant qualified mode we need to append the tenant domain to the path manually.
     *
     * @param endpoint Relative endpoint path.
     * @return Context of the API.
     */
    private static String getContext(String tenantDomain, String endpoint, String baseUrl) {

        String context;
        if (IdentityTenantUtil.isTenantQualifiedUrlsEnabled()) {
            context = baseUrl + endpoint;
        } else {
            context = String.format(TENANT_CONTEXT_PATH_COMPONENT, tenantDomain) + baseUrl + endpoint;
        }
        return context;
    }

    /**
     * Builds error response.
     *
     * @param errorCode Error code.
     * @param errorMessage Error message.
     * @param errorDescription Error description.
     * @return ErrorResponse.
     */
    private static ErrorResponse buildErrorResponse(String errorCode, String errorMessage, String errorDescription) {

        return getErrorBuilder(errorCode, errorMessage, errorDescription).build(LOG, errorMessage);
    }

    /**
     * Builds server error response.
     *
     * @param e Identity Recovery Exception.
     * @param errorCode Error code.
     * @param errorMessage Error message.
     * @param errorDescription Error description.
     * @return ErrorResponse.
     */
    private static ErrorResponse buildServerErrorResponse(IdentityRecoveryException e, String errorCode,
                                                          String errorMessage, String errorDescription) {

        return getErrorBuilder(errorCode, errorMessage, errorDescription).build(LOG, e, errorMessage);
    }

    /**
     * Get ErrorResponse Builder
     *
     * @param errorCode Error code.
     * @param errorMessage Error message.
     * @param errorDescription Error description.
     * @return ErrorResponse.Builder.
     */
    private static ErrorResponse.Builder getErrorBuilder(String errorCode, String errorMessage,
                                                         String errorDescription) {

        return new ErrorResponse.Builder().withCode(errorCode)
                .withMessage(errorMessage)
                .withDescription(errorDescription);
    }

    /**
     * Build the RetryErrorResponse for not valid password scenario.
     *
     * @param message           Error message.
     * @param description       Error description.
     * @param code              Error code.
     * @param resetCode         Password reset code.
     * @param correlationId     Trace Id.
     * @param apiCallsArrayList Available APIs.
     * @return RetryErrorResponse.
     */
    private static RetryErrorResponse buildRetryErrorResponse(String message, String description, String code,
                                                              String resetCode, String correlationId,
                                                              ArrayList<APICall> apiCallsArrayList) {

        RetryErrorResponse retryErrorResponse = new RetryErrorResponse();
        retryErrorResponse.setCode(code);
        retryErrorResponse.setMessage(message);
        retryErrorResponse.setDescription(description);
        retryErrorResponse.setResetCode(resetCode);
        retryErrorResponse.setTraceId(correlationId);
        retryErrorResponse.setLinks(apiCallsArrayList);
        return retryErrorResponse;
    }

    /**
     * Returns a new PreconditionFailedException.
     *
     * @param tenantDomain  Tenant domain.
     * @param description   Description of the exception.
     * @param code          Error code.
     * @param resetCode     Reset code given to the user by confirmation API.
     * @param correlationId Correlation Id.
     * @return A new PreconditionFailedException with the specified details as a response.
     */
    private static PreconditionFailedException buildRetryPasswordResetObject(String tenantDomain, String description,
                                                                             String code, String resetCode,
                                                                             String correlationId) {

        // Build next API calls.
        ArrayList<APICall> apiCallsArrayList = new ArrayList<>();
        apiCallsArrayList.add(RecoveryUtil
                .buildApiCall(APICalls.RESET_PASSWORD_API.getType(), Constants.RelationStates.NEXT_REL,
                        buildURIForBody(tenantDomain, APICalls.RESET_PASSWORD_API.getApiUrl(),
                                Constants.ACCOUNT_RECOVERY_ENDPOINT_BASEPATH), null));
        RetryErrorResponse retryErrorResponse = buildRetryErrorResponse(
                Constants.STATUS_PRECONDITION_FAILED_MESSAGE_DEFAULT, code, description, resetCode, correlationId,
                apiCallsArrayList);
        LOG.debug(description);
        return new PreconditionFailedException(retryErrorResponse);
    }

    /**
     * Prepend the operation scenario to the existing exception error code.
     * (Eg: USR-20045)
     *
     * @param exceptionErrorCode Existing error code.
     * @param scenario           Operation scenario.
     * @return New error code with the scenario prepended.
     */
    private static String prependOperationScenarioToErrorCode(String exceptionErrorCode, String scenario) {

        if (StringUtils.isNotEmpty(exceptionErrorCode)) {
            if (exceptionErrorCode.contains(IdentityRecoveryConstants.EXCEPTION_SCENARIO_SEPARATOR)) {
                return exceptionErrorCode;
            }
            if (StringUtils.isNotEmpty(scenario)) {
                exceptionErrorCode =
                        scenario + IdentityRecoveryConstants.EXCEPTION_SCENARIO_SEPARATOR + exceptionErrorCode;
            }
        }
        return exceptionErrorCode;
    }
}
