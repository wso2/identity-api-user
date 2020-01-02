/*
 *
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.recovery.IdentityRecoveryClientException;
import org.wso2.carbon.identity.recovery.IdentityRecoveryConstants;
import org.wso2.carbon.identity.recovery.dto.NotificationChannelDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.APICallDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.ErrorResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.PropertyDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.RecoveryChannelDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.RetryErrorResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.UserClaimDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.Constants;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.exceptions.ConflictException;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.exceptions.ForbiddenException;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.exceptions.InternalServerErrorException;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.exceptions.NotAcceptableException;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.exceptions.NotFoundException;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.exceptions.PreconditionFailedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.TENANT_CONTEXT_PATH_COMPONENT;

/**
 * Contains the recovery endpoint utils.
 */
public class RecoveryUtil {

    private static final Log log = LogFactory.getLog(RecoveryUtil.class);
    private static final String LOG_MESSAGE_PREFIX = "INITIATOR";
    private static final String FORBIDDEN_ERROR_CATEGORY = "FORBIDDEN_ERROR_CATEGORY";
    private static final String CONFLICT_REQUEST_ERROR_CATEGORY = "CONFLICT_REQUEST_ERROR_CATEGORY";
    private static final String REQUEST_NOT_FOUND_ERROR_CATEGORY = "REQUEST_NOT_FOUND_ERROR_CATEGORY";
    private static final String REQUEST_NOT_ACCEPTABLE_ERROR_CATEGORY = "REQUEST_NOT_ACCEPTABLE_ERROR_CATEGORY";
    private static final String RETRY_ERROR_CATEGORY = "RETRY_ERROR_CATEGORY";

    // Map with the error codes categorized in to different error groups.
    private static final HashMap<String, String> clientErrorMap = generateClientErrorMap();

    /**
     * Converts a list of UserClaimDTO in to a UserClaim array.
     *
     * @param claimDTOs UserClaimDTO List
     * @return Map of user claims
     */
    public static HashMap<String, String> buildUserClaimsMap(List<UserClaimDTO> claimDTOs) {

        HashMap<String, String> userClaims = new HashMap<>();
        for (UserClaimDTO userClaimDTO : claimDTOs) {
            userClaims.put(userClaimDTO.getUri(), userClaimDTO.getValue());
        }
        return userClaims;
    }

    /**
     * Convert the list of PropertyDTOs in to an array.
     *
     * @param propertyDTOs Lost of {@link PropertyDTO} objects
     * @return Map of properties
     */
    public static HashMap<String, String> buildPropertiesMap(List<PropertyDTO> propertyDTOs) {

        HashMap<String, String> properties = new HashMap<>();
        if (propertyDTOs == null) {
            return properties;
        }
        for (PropertyDTO propertyDTO : propertyDTOs) {
            properties.put(propertyDTO.getKey(), propertyDTO.getValue());
        }
        return properties;
    }

    /**
     * Build the channel response object list.
     *
     * @param channels Available notification channels list as objects of {@link NotificationChannelDTO}
     * @return List of RecoveryChannelDTOs {@link RecoveryChannelDTO}
     */
    public static List<RecoveryChannelDTO> buildRecoveryChannelInformation(NotificationChannelDTO[] channels) {

        List<RecoveryChannelDTO> recoveryChannelDTOS = new ArrayList<>();
        if (channels != null) {
            // Create a response object and add the details to each object.
            for (NotificationChannelDTO channel : channels) {
                RecoveryChannelDTO recoveryChannelDTO = new RecoveryChannelDTO();
                recoveryChannelDTO.setId(Integer.toString(channel.getId()));
                recoveryChannelDTO.setType(channel.getType());
                recoveryChannelDTO.setValue(channel.getValue());
                if (StringUtils.isNotEmpty(channel.getValue())) {
                    recoveryChannelDTO.setPreferred(channel.isPreferred());
                }
                recoveryChannelDTOS.add(recoveryChannelDTO);
            }
        }
        return recoveryChannelDTOS;
    }

    /**
     * Handle client errors with specific http codes.
     *
     * @param className Class name
     * @param scenario  Recovery scenario
     * @param exception IdentityRecoveryClientException
     * @return WebApplicationException (NOTE: Returns null when the client error is for no user available or for
     * multiple users available
     */
    public static WebApplicationException handleClientExceptions(String className, String tenantDomain,
                                                                 String scenario, String correlationId,
                                                                 IdentityRecoveryClientException exception) {

        return handleClientExceptions(className, tenantDomain, scenario, StringUtils.EMPTY, correlationId, exception);
    }

    /**
     * Handle client errors with specific http codes.
     *
     * @param className Class name
     * @param scenario  Recovery scenario
     * @param code      Recovery code
     * @param exception IdentityRecoveryClientException
     * @return WebApplicationException (NOTE: Returns null when the client error is for no user available or for
     * multiple users available
     */
    public static WebApplicationException handleClientExceptions(String className, String tenantDomain, String scenario,
                                                                 String code, String correlationId,
                                                                 IdentityRecoveryClientException exception) {

        if (StringUtils.isEmpty(exception.getErrorCode())) {
            return buildConflictRequestResponseObject(className, exception.getMessage(), exception.getErrorCode(),
                    correlationId);
        }
        String errorCode = prependOperationScenarioToErrorCode(exception.getErrorCode(), scenario);

        if (clientErrorMap.containsKey(errorCode)) {
            String errorCategory = clientErrorMap.get(errorCode);

            // Throw errors according to exception category.
            switch (errorCategory) {
                case FORBIDDEN_ERROR_CATEGORY:
                    return buildForbiddenRequestResponseObject(className, exception.getMessage(), errorCode,
                            correlationId);
                case CONFLICT_REQUEST_ERROR_CATEGORY:
                    if (IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_MULTIPLE_MATCHING_USERS.getCode()
                            .equals(errorCode)) {
                        // If user notify is not enabled, throw a accepted response.
                        if (!Boolean.parseBoolean(IdentityUtil
                                .getProperty(IdentityRecoveryConstants.ConnectorConfig.NOTIFY_USER_EXISTENCE))) {
                            return new WebApplicationException(Response.accepted().build());
                        }
                    }
                    return buildConflictRequestResponseObject(className, exception.getMessage(),
                            exception.getErrorCode(), correlationId);
                case REQUEST_NOT_FOUND_ERROR_CATEGORY:
                    if (IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_NO_USER_FOUND.getCode().equals(errorCode)) {
                        // If user notify is not enabled, throw a accepted response.
                        if (!Boolean.parseBoolean(IdentityUtil
                                .getProperty(IdentityRecoveryConstants.ConnectorConfig.NOTIFY_USER_EXISTENCE))) {
                            return new WebApplicationException(Response.accepted().build());
                        }
                    }
                    return buildRequestNotFoundResponseObject(className, exception.getMessage(), errorCode,
                            correlationId);
                case REQUEST_NOT_ACCEPTABLE_ERROR_CATEGORY:
                    return buildRequestNotAcceptableResponseObject(className, exception.getMessage(), errorCode,
                            correlationId);
                case RETRY_ERROR_CATEGORY:
                    return buildRetryPasswordResetObject(className, tenantDomain, exception.getMessage(), errorCode,
                            code);
                default:
                    return buildConflictRequestResponseObject(className, exception.getMessage(), errorCode,
                            correlationId);
            }
        } else {
            return buildConflictRequestResponseObject(className, exception.getMessage(), errorCode, correlationId);
        }
    }

    /**
     * Logs the error, builds a internalServerErrorException with specified details and throws it.
     *
     * @param className     Class name
     * @param message       Error message
     * @param code          Error code
     * @param correlationId Correlation Id
     * @param throwable     Error
     * @return WebApplicationException
     */
    public static WebApplicationException buildInternalServerErrorResponse(String className, String message,
                                                                           String code, String correlationId,
                                                                           Throwable throwable) {

        if (StringUtils.isNotBlank(className)) {
            message = String.format("%s : %s - %s", LOG_MESSAGE_PREFIX, className, message);
        }
        if (throwable == null) {
            log.error(message);
        } else {
            log.error(message, throwable);
        }
        return buildInternalServerErrorErrorDTO(code, correlationId);
    }

    /**
     * Build API call information.
     *
     * @param type   Type of the API call
     * @param rel    API relation
     * @param apiUrl Url of the API
     * @param data   Additional data
     * @return APICallDTO {@link APICallDTO} which encapsulates the API name and the url
     */
    public static APICallDTO buildApiCallDTO(String type, String rel, String apiUrl, String data) {

        if (StringUtils.isNotEmpty(data)) {
            apiUrl = String.format(apiUrl, data);
        }
        APICallDTO apiCallDTO = new APICallDTO();
        apiCallDTO.setType(type);
        apiCallDTO.setRel(rel);
        apiCallDTO.setHref(apiUrl);
        return apiCallDTO;
    }

    /**
     * Build the relative url.
     *
     * @param tenantDomain Tenant Domain
     * @param endpoint     API endpoint
     * @return Url
     */
    public static String buildUri(String tenantDomain, String endpoint, String baseUrl) {

        String tenantQualifiedRelativePath =
                String.format(TENANT_CONTEXT_PATH_COMPONENT, tenantDomain) + baseUrl;
        return tenantQualifiedRelativePath + endpoint;
    }

    /**
     * Returns a new InternalServerErrorException.
     *
     * @param code          Code
     * @param correlationId Correlation Id
     * @return A new InternalServerErrorException with default details as a response DTO
     */
    private static InternalServerErrorException buildInternalServerErrorErrorDTO(String code, String correlationId) {

        ErrorResponseDTO errorDTO = buildErrorResponseDTO(Constants.STATUS_INTERNAL_SERVER_ERROR_MESSAGE_DEFAULT, code,
                Constants.STATUS_INTERNAL_SERVER_ERROR_DESCRIPTION_DEFAULT, correlationId);
        return new InternalServerErrorException(errorDTO);
    }

    /**
     * Returns a generic errorDTO.
     *
     * @param code          Error code
     * @param message       Specifies the error message
     * @param description   Error description
     * @param correlationId CorrelationID
     * @return A generic errorDTO with the specified details
     */
    private static ErrorResponseDTO buildErrorResponseDTO(String message, String code, String description,
                                                          String correlationId) {

        ErrorResponseDTO errorDTO = new ErrorResponseDTO();
        errorDTO.setCode(code);
        errorDTO.setMessage(message);
        errorDTO.setDescription(description);
        errorDTO.setTraceId(correlationId);
        return errorDTO;
    }

    /**
     * Returns a new PreconditionFailedException.
     *
     * @param className    Class name
     * @param tenantDomain Tenant domain
     * @param description  Description of the exception
     * @param code         Error code
     * @param resetCode    Reset code given to the user by confirmation API
     * @return A new PreconditionFailedException with the specified details as a response DTO
     */
    private static PreconditionFailedException buildRetryPasswordResetObject(String className, String tenantDomain,
                                                                             String description, String code,
                                                                             String resetCode) {

        // Build next API calls.
        ArrayList<APICallDTO> apiCallDTOArrayList = new ArrayList<>();
        apiCallDTOArrayList.add(RecoveryUtil
                .buildApiCallDTO(Constants.APICall.RESET_PASSWORD_API.getType(), Constants.RelationStates.NEXT_REL,
                        buildUri(tenantDomain, Constants.APICall.RESET_PASSWORD_API.getApiUrl(),
                                Constants.ACCOUNT_RECOVERY_ENDPOINT_BASEPATH), null));
        RetryErrorResponseDTO retryErrorResponseDTO = buildRetryErrorResponseDTO(
                Constants.STATUS_PRECONDITION_FAILED_MESSAGE_DEFAULT, code, description, resetCode,
                apiCallDTOArrayList);
        if (StringUtils.isNotBlank(className)) {
            description = String.format("%s : %s - %s", LOG_MESSAGE_PREFIX, className, description);
        }
        log.error(description);
        return new PreconditionFailedException(retryErrorResponseDTO);
    }

    /**
     * Build the RetryErrorDTO for not valid password scenario.
     *
     * @param message             Error message
     * @param description         Error description
     * @param code                Error code
     * @param resetCode           Password reset code
     * @param apiCallDTOArrayList Available APIs
     * @return RetryErrorResponseDTO
     */
    private static RetryErrorResponseDTO buildRetryErrorResponseDTO(String message, String description, String code,
                                                                    String resetCode,
                                                                    ArrayList<APICallDTO> apiCallDTOArrayList) {

        RetryErrorResponseDTO errorDTO = new RetryErrorResponseDTO();
        errorDTO.setCode(code);
        errorDTO.setMessage(message);
        errorDTO.setDescription(description);
        errorDTO.setResetCode(resetCode);
        errorDTO.setLinks(apiCallDTOArrayList);
        return errorDTO;
    }

    /**
     * Returns a new NotAcceptableException.
     *
     * @param className     Class name
     * @param description   Description of the exception
     * @param code          Error code
     * @param correlationId Correlation Id
     * @return A new NotAcceptableException with the specified details as a response DTO
     */
    private static NotAcceptableException buildRequestNotAcceptableResponseObject(String className, String description,
                                                                                  String code, String correlationId) {

        ErrorResponseDTO errorDTO = buildErrorResponseDTO(Constants.STATUS_METHOD_NOT_ACCPETED_MESSAGE_DEFAULT, code,
                description, correlationId);
        if (StringUtils.isNotBlank(className)) {
            description = String.format("%s : %s - %s", LOG_MESSAGE_PREFIX, className, description);
        }
        log.error(description);
        return new NotAcceptableException(errorDTO);
    }

    /**
     * Returns a new NotAcceptableException.
     *
     * @param className     Class name
     * @param description   Description of the exception
     * @param code          Error code
     * @param correlationId Correlation Id
     * @return A new NotAcceptableException with the specified details as a response DTO
     */
    private static NotFoundException buildRequestNotFoundResponseObject(String className, String description,
                                                                        String code, String correlationId) {

        ErrorResponseDTO errorDTO = buildErrorResponseDTO(Constants.STATUS_NOT_FOUND_MESSAGE_DEFAULT, code,
                description, correlationId);
        if (StringUtils.isNotBlank(className)) {
            description = String.format("%s : %s - %s", LOG_MESSAGE_PREFIX, className, description);
        }
        log.error(description);
        return new NotFoundException(errorDTO);
    }

    /**
     * Returns a new ConflictException.
     *
     * @param className     Classname
     * @param description   Description of the exception
     * @param code          Error code
     * @param correlationId CorrelationId
     * @return A new ConflictException with the specified details as a response DTO
     */
    private static ConflictException buildConflictRequestResponseObject(String className, String description,
                                                                        String code, String correlationId) {

        ErrorResponseDTO errorDTO = buildErrorResponseDTO(Constants.STATUS_CONFLICT_MESSAGE_DEFAULT, code,
                description, correlationId);
        if (StringUtils.isNotBlank(className)) {
            description = String.format("CorrelationIc: %s : %s : %s - %s", correlationId, LOG_MESSAGE_PREFIX,
                    className, description);
        }
        log.error(description);
        return new ConflictException(errorDTO);
    }

    /**
     * Returns a new ForbiddenException.
     *
     * @param className     Class name
     * @param description   Description of the exception
     * @param code          Error code
     * @param correlationId CorrelationId
     * @return A new ForbiddenException with the specified details as a response DTO
     */
    private static ForbiddenException buildForbiddenRequestResponseObject(String className, String description,
                                                                          String code, String correlationId) {

        ErrorResponseDTO errorDTO = buildErrorResponseDTO(Constants.STATUS_FORBIDDEN_MESSAGE_DEFAULT, code,
                description, correlationId);
        if (StringUtils.isNotBlank(className)) {
            description = String.format("%s : %s - %s", LOG_MESSAGE_PREFIX, className, description);
        }
        log.error(description);
        return new ForbiddenException(errorDTO);
    }

    /**
     * Prepend the operation scenario to the existing exception error code.
     * (Eg: USR-20045)
     *
     * @param exceptionErrorCode Existing error code.
     * @param scenario           Operation scenario
     * @return New error code with the scenario prepended
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

    /**
     * Generate the map which categorizes the exceptions for different http error groups.
     *
     * @return Grouped client error map
     */
    private static HashMap<String, String> generateClientErrorMap() {

        HashMap<String, String> clientErrorMap = new HashMap<>();

        // Errors for not enabling account recovery, user account locked, user account disabled.
        clientErrorMap
                .put(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_PASSWORD_RECOVERY_WITH_NOTIFICATIONS_NOT_ENABLED
                        .getCode(), FORBIDDEN_ERROR_CATEGORY);
        clientErrorMap.put(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_USERNAME_RECOVERY_NOT_ENABLED.getCode(),
                FORBIDDEN_ERROR_CATEGORY);
        clientErrorMap.put(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_DISABLED_ACCOUNT.getCode(),
                FORBIDDEN_ERROR_CATEGORY);
        clientErrorMap.put(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_LOCKED_ACCOUNT.getCode(),
                FORBIDDEN_ERROR_CATEGORY);
        clientErrorMap.put(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_PASSWORD_RECOVERY_NOT_ENABLED.getCode(),
                FORBIDDEN_ERROR_CATEGORY);

        // Tenant miss match error.
        clientErrorMap.put(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_USER_TENANT_DOMAIN_MISS_MATCH_WITH_CONTEXT
                .getCode(), CONFLICT_REQUEST_ERROR_CATEGORY);

        // Multiples users found error.
        clientErrorMap.put(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_MULTIPLE_MATCHING_USERS.getCode(),
                CONFLICT_REQUEST_ERROR_CATEGORY);

        // No user found error.
        clientErrorMap.put(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_NO_USER_FOUND.getCode(),
                REQUEST_NOT_FOUND_ERROR_CATEGORY);

        // No recovery code found and no verified channels found errors.
        clientErrorMap.put(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_NO_ACCOUNT_RECOVERY_DATA.getCode(),
                REQUEST_NOT_FOUND_ERROR_CATEGORY);
        clientErrorMap.put(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_NO_VERIFIED_CHANNELS_FOR_USER.getCode(),
                REQUEST_NOT_FOUND_ERROR_CATEGORY);

        // Invalid recovery codes errors.
        clientErrorMap.put(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_INVALID_RECOVERY_CODE.getCode(),
                REQUEST_NOT_ACCEPTABLE_ERROR_CATEGORY);
        clientErrorMap.put(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_INVALID_RESEND_CODE.getCode(),
                REQUEST_NOT_ACCEPTABLE_ERROR_CATEGORY);
        clientErrorMap.put(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_EXPIRED_RECOVERY_CODE.getCode(),
                REQUEST_NOT_ACCEPTABLE_ERROR_CATEGORY);

        // Password reset password history violation errors and password policy violation errors.
        clientErrorMap.put(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_PASSWORD_HISTORY_VIOLATION.getCode(),
                RETRY_ERROR_CATEGORY);
        clientErrorMap.put(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_PASSWORD_POLICY_VIOLATION.getCode(),
                RETRY_ERROR_CATEGORY);
        return clientErrorMap;
    }
}
