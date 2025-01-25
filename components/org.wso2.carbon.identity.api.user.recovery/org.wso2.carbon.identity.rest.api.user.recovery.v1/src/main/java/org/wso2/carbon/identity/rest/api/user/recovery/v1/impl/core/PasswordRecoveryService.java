/*
 * Copyright (c) 2020-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.common.Util;
import org.wso2.carbon.identity.governance.service.notification.NotificationChannels;
import org.wso2.carbon.identity.recovery.IdentityRecoveryClientException;
import org.wso2.carbon.identity.recovery.IdentityRecoveryConstants;
import org.wso2.carbon.identity.recovery.IdentityRecoveryException;
import org.wso2.carbon.identity.recovery.IdentityRecoveryServerException;
import org.wso2.carbon.identity.recovery.dto.PasswordRecoverDTO;
import org.wso2.carbon.identity.recovery.dto.PasswordResetCodeDTO;
import org.wso2.carbon.identity.recovery.dto.RecoveryChannelInfoDTO;
import org.wso2.carbon.identity.recovery.dto.RecoveryInformationDTO;
import org.wso2.carbon.identity.recovery.dto.ResendConfirmationDTO;
import org.wso2.carbon.identity.recovery.dto.SuccessfulPasswordResetDTO;
import org.wso2.carbon.identity.recovery.services.password.PasswordRecoveryManager;
import org.wso2.carbon.identity.recovery.util.Utils;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.utils.RecoveryUtil;

import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.APICall;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.AccountRecoveryType;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.ConfirmRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.InitRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.PasswordRecoveryExternalNotifyResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.PasswordRecoveryInternalNotifyResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.PasswordResetResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.RecoveryChannel;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.RecoveryChannelInformation;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.RecoveryRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.ResendConfirmationCodeExternalResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.ResendConfirmationCodeInternalResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.ResendConfirmationRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.ResetCodeResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.ResetRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.ws.rs.core.Response;

/**
 * Call internal OSGI services to perform password recovery.
 */
public class PasswordRecoveryService {

    private final PasswordRecoveryManager passwordRecoveryManager;

    private static final Log log = LogFactory.getLog(PasswordRecoveryService.class.getName());

    public PasswordRecoveryService(PasswordRecoveryManager passwordRecoveryManager) {

        this.passwordRecoveryManager = passwordRecoveryManager;
    }

    /**
     * Initiate Password Recovery from POST.
     *
     * @param initRequest {@link InitRequest} object which holds the information in the account recovery
     *                    request
     * @return Response
     */
    public Response initiatePasswordRecovery(InitRequest initRequest) {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        Map<String, String> userClaims = RecoveryUtil.buildUserClaimsMap(initRequest.getClaims());
        try {

            boolean isNotificationBasedRecoveryEnabled = isNotificationBasedRecoveryEnabled(tenantDomain);
            boolean isQuestionBasedRecoveryAllowedForUser = isQuestionBasedRecoveryEnabled(tenantDomain);
            ArrayList<AccountRecoveryType> accountRecoveryTypes = new ArrayList<>();
            List<Object> passwordRecoveryManagerList = PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiServices(PasswordRecoveryManager.class, null);

            if (!isNotificationBasedRecoveryEnabled && !isQuestionBasedRecoveryAllowedForUser) {
                if (log.isDebugEnabled()) {
                    log.debug("User password recovery is not enabled for the tenant: " + tenantDomain);
                }
                throw Utils.handleClientException(
                        IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_PASSWORD_RECOVERY_NOT_ENABLED,
                        null);
            }

            for (Object passwordRecoveryManager : passwordRecoveryManagerList) {
                RecoveryInformationDTO recoveryInformationDTO = ((PasswordRecoveryManager) passwordRecoveryManager)
                        .initiate(userClaims, tenantDomain,
                                RecoveryUtil.buildPropertiesMap(initRequest.getProperties()));
                Optional<AccountRecoveryType> recoveryType =
                        buildPasswordRecoveryInitResponse(tenantDomain, recoveryInformationDTO);
                recoveryType.ifPresent(accountRecoveryTypes::add);
            }

            if (accountRecoveryTypes.isEmpty()) {
                log.debug("No recovery information for password recovery request");
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok().entity(accountRecoveryTypes).build();
        } catch (IdentityRecoveryClientException e) {
            throw RecoveryUtil.handleClientExceptions(PasswordRecoveryService.class.getName(), tenantDomain,
                    IdentityRecoveryConstants.PASSWORD_RECOVERY_SCENARIO, Util.getCorrelation(), e);
        } catch (IdentityRecoveryException e) {
            throw RecoveryUtil.buildInternalServerErrorResponse(PasswordRecoveryService.class.getName(),
                    Constants.SERVER_ERROR, e.getErrorCode(), Util.getCorrelation(), e);
        }
    }

    /**
     * Send the recovery notifications to the user via user preferred channel given by the channelId param.
     *
     * @param recoveryRequest {@link RecoveryRequest} Object which holds the recovery information in the
     *                        password recovery request
     * @return Response
     */
    public Response recoverPassword(RecoveryRequest recoveryRequest) {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        String recoveryId = recoveryRequest.getRecoveryCode();
        String channelId = recoveryRequest.getChannelId();
        try {
            PasswordRecoverDTO passwordRecoverDTO = passwordRecoveryManager.notify(recoveryId, channelId, tenantDomain,
                    RecoveryUtil.buildPropertiesMap(recoveryRequest.getProperties()));
            if (passwordRecoverDTO == null) {
                if (log.isDebugEnabled()) {
                    String message = String
                            .format("No password recovery data object for recovery code : %s", recoveryId);
                    log.debug(message);
                }
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            return buildPasswordRecoveryResponse(tenantDomain, passwordRecoverDTO.getNotificationChannel(),
                    passwordRecoverDTO);
        } catch (IdentityRecoveryClientException e) {
            throw RecoveryUtil.handleClientExceptions(PasswordRecoveryService.class.getName(), tenantDomain,
                    IdentityRecoveryConstants.PASSWORD_RECOVERY_SCENARIO, Util.getCorrelation(), e);
        } catch (IdentityRecoveryException e) {
            throw RecoveryUtil.buildInternalServerErrorResponse(PasswordRecoveryService.class.getName(),
                    Constants.SERVER_ERROR, e.getErrorCode(), Util.getCorrelation(), e);
        }
    }

    /**
     * Validate the recovery code given for verify password reset.
     *
     * @param confirmRequest {@link ConfirmRequest}Confirmation code object received for password recovery
     * @return Response
     */
    public Response confirmRecovery(ConfirmRequest confirmRequest) {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        try {
            PasswordResetCodeDTO passwordResetCodeDTO = passwordRecoveryManager.confirm(confirmRequest
                    .getConfirmationCode(), tenantDomain, RecoveryUtil.buildPropertiesMap(confirmRequest
                    .getProperties()));
            return Response.ok().entity(buildResetCodeResponse(tenantDomain, passwordResetCodeDTO)).build();
        } catch (IdentityRecoveryClientException e) {
            throw RecoveryUtil.handleClientExceptions(PasswordRecoveryService.class.getName(), tenantDomain,
                    IdentityRecoveryConstants.PASSWORD_RECOVERY_SCENARIO, Util.getCorrelation(), e);
        } catch (IdentityRecoveryException e) {
            throw RecoveryUtil.buildInternalServerErrorResponse(PasswordRecoveryService.class.getName(),
                    Constants.SERVER_ERROR, e.getErrorCode(), Util.getCorrelation(), e);
        }
    }

    /**
     * Update the password of the user with the new given password.
     *
     * @param resetRequest {@link ResetRequest} Object which holds the password reset code and password reset
     *                     information
     * @return Response
     */
    public Response resetPassword(ResetRequest resetRequest) {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        char[] password = resetRequest.getPassword().toCharArray();
        try {
            SuccessfulPasswordResetDTO successfulPasswordResetDTO = passwordRecoveryManager.reset(resetRequest
                    .getResetCode(), password, RecoveryUtil.buildPropertiesMap(resetRequest.getProperties()));
            return Response.ok().entity(buildPasswordResetResponse(successfulPasswordResetDTO)).build();
        } catch (IdentityRecoveryClientException e) {
            // Send the reset code again for a retry attempt.
            throw RecoveryUtil.handleClientExceptions(PasswordRecoveryService.class.getName(), tenantDomain,
                    IdentityRecoveryConstants.PASSWORD_RECOVERY_SCENARIO, resetRequest.getResetCode(),
                    Util.getCorrelation(), e);
        } catch (IdentityRecoveryException e) {
            throw RecoveryUtil.buildInternalServerErrorResponse(PasswordRecoveryService.class.getName(),
                    Constants.SERVER_ERROR, e.getErrorCode(), Util.getCorrelation(), e);
        }
    }

    /**
     * Resend the recovery confirmation code to the user.
     *
     * @param resendConfirmationRequest ResendConfirmationRequest {@link ResendConfirmationRequest} object
     *                                  which wraps the resend request
     * @return Response
     */
    public Response resendConfirmation(ResendConfirmationRequest resendConfirmationRequest) {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        String resendCode = resendConfirmationRequest.getResendCode();
        Map<String, String> properties = RecoveryUtil.buildPropertiesMap(resendConfirmationRequest.getProperties());
        try {
            ResendConfirmationDTO resendConfirmationDTO = passwordRecoveryManager.resend(tenantDomain, resendCode,
                    properties);
            if (resendConfirmationDTO == null) {
                if (log.isDebugEnabled()) {
                    log.debug("No ResendConfirmationDTO data for resend code :" + resendCode);
                }
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            return buildResendConfirmationResponse(tenantDomain, resendConfirmationDTO);
        } catch (IdentityRecoveryClientException e) {
            throw RecoveryUtil.handleClientExceptions(PasswordRecoveryService.class.getName(), tenantDomain,
                    IdentityRecoveryConstants.PASSWORD_RECOVERY_SCENARIO, Util.getCorrelation(), e);
        } catch (IdentityRecoveryException e) {
            throw RecoveryUtil.buildInternalServerErrorResponse(PasswordRecoveryService.class.getName(),
                    Constants.SERVER_ERROR, e.getErrorCode(), Util.getCorrelation(), e);
        }
    }

    /**
     * Build Resend confirmation code response according to the notification channel.
     *
     * @param tenantDomain          Tenant domain in the request.
     * @param resendConfirmationDTO ResendConfirmationDTO
     * @return Response
     */
    private Response buildResendConfirmationResponse(String tenantDomain,
                                                     ResendConfirmationDTO resendConfirmationDTO) {

        ArrayList<APICall> apiCallsArrayList = new ArrayList<>();
        // Add confirm API call information.
        apiCallsArrayList.add(RecoveryUtil.buildApiCall(Constants.APICall.CONFIRM_PASSWORD_RECOVERY_API.getType(),
                Constants.RelationStates.NEXT_REL, RecoveryUtil
                        .buildURIForBody(tenantDomain, Constants.APICall.CONFIRM_PASSWORD_RECOVERY_API.getApiUrl(),
                                Constants.ACCOUNT_RECOVERY_ENDPOINT_BASEPATH), null));
        // Add resend confirmation code API call information.
        apiCallsArrayList.add(RecoveryUtil.buildApiCall(Constants.APICall.RESEND_CONFIRMATION_API.getType(),
                Constants.RelationStates.RESEND_REL, RecoveryUtil
                        .buildURIForBody(tenantDomain, Constants.APICall.RESEND_CONFIRMATION_API.getApiUrl(),
                                Constants.ACCOUNT_RECOVERY_ENDPOINT_BASEPATH), null));
        if (NotificationChannels.EXTERNAL_CHANNEL.getChannelType()
                .equals(resendConfirmationDTO.getNotificationChannel())) {
            ResendConfirmationCodeExternalResponse resendConfirmationCodeExternalResponse =
                    new ResendConfirmationCodeExternalResponse();
            resendConfirmationCodeExternalResponse.setCode(resendConfirmationDTO.getSuccessCode());
            resendConfirmationCodeExternalResponse.setMessage(resendConfirmationDTO.getSuccessMessage());
            resendConfirmationCodeExternalResponse
                    .setNotificationChannel(resendConfirmationDTO.getNotificationChannel());
            resendConfirmationCodeExternalResponse.setResendCode(resendConfirmationDTO.getResendCode());
            resendConfirmationCodeExternalResponse
                    .setConfirmationCode(resendConfirmationDTO.getExternalConfirmationCode());
            resendConfirmationCodeExternalResponse.setLinks(apiCallsArrayList);
            return Response.ok().entity(resendConfirmationCodeExternalResponse).build();
        } else {
            ResendConfirmationCodeInternalResponse resendConfirmationCodeInternalResponse =
                    new ResendConfirmationCodeInternalResponse();
            resendConfirmationCodeInternalResponse.setCode(resendConfirmationDTO.getSuccessCode());
            resendConfirmationCodeInternalResponse.setMessage(resendConfirmationDTO.getSuccessMessage());
            resendConfirmationCodeInternalResponse
                    .setNotificationChannel(resendConfirmationDTO.getNotificationChannel());
            resendConfirmationCodeInternalResponse.setResendCode(resendConfirmationDTO.getResendCode());
            resendConfirmationCodeInternalResponse.setLinks(apiCallsArrayList);
            return Response.accepted().entity(resendConfirmationCodeInternalResponse).build();
        }
    }

    /**
     * Build successful response for successful password update.
     *
     * @param successfulPasswordResetDTO SuccessfulPasswordResetDTO object
     * @return PasswordResetResponse
     */
    private PasswordResetResponse buildPasswordResetResponse(
            SuccessfulPasswordResetDTO successfulPasswordResetDTO) {

        PasswordResetResponse passwordResetResponseDTO = new PasswordResetResponse();
        passwordResetResponseDTO.setCode(successfulPasswordResetDTO.getSuccessCode());
        passwordResetResponseDTO.setMessage(successfulPasswordResetDTO.getMessage());
        return passwordResetResponseDTO;
    }

    /**
     * Build the ResetCodeResponse for successful confirmation code validation.
     *
     * @param tenantDomain         Tenant Domain
     * @param passwordResetCodeDTO {@link PasswordResetCodeDTO}PasswordResetCodeDTO
     * @return ResetCodeResponseDTO {@link ResetCodeResponse}object with a password reset code
     */
    private ResetCodeResponse buildResetCodeResponse(String tenantDomain,
                                                     PasswordResetCodeDTO passwordResetCodeDTO) {

        // Build next API calls list.
        ArrayList<APICall> apiCallsArrayList = new ArrayList<>();
        apiCallsArrayList.add(RecoveryUtil
                .buildApiCall(Constants.APICall.RESET_PASSWORD_API.getType(), Constants.RelationStates.NEXT_REL,
                        RecoveryUtil.buildURIForBody(tenantDomain, Constants.APICall.RESET_PASSWORD_API.getApiUrl(),
                                Constants.ACCOUNT_RECOVERY_ENDPOINT_BASEPATH), null));
        ResetCodeResponse resetCodeResponseDTO = new ResetCodeResponse();
        resetCodeResponseDTO.setResetCode(passwordResetCodeDTO.getPasswordResetCode());
        resetCodeResponseDTO.setLinks(apiCallsArrayList);
        return resetCodeResponseDTO;
    }

    /**
     * Build the successful response according to the notification channel.
     *
     * @param tenantDomain        Tenant domain
     * @param notificationChannel Notification channel
     * @param passwordRecoverDTO  {@link PasswordRecoverDTO} PasswordRecoverDTO
     * @return Response
     */
    private Response buildPasswordRecoveryResponse(String tenantDomain, String notificationChannel,
                                                   PasswordRecoverDTO passwordRecoverDTO) {

        // Build next API calls.
        ArrayList<APICall> apiCallsArrayList = new ArrayList<>();
        apiCallsArrayList.add(RecoveryUtil.buildApiCall(Constants.APICall.CONFIRM_PASSWORD_RECOVERY_API.getType(),
                Constants.RelationStates.NEXT_REL, RecoveryUtil
                        .buildURIForBody(tenantDomain, Constants.APICall.CONFIRM_PASSWORD_RECOVERY_API.getApiUrl(),
                                Constants.ACCOUNT_RECOVERY_ENDPOINT_BASEPATH), null));
        // Add resend confirmation code API call information.
        apiCallsArrayList.add(RecoveryUtil.buildApiCall(Constants.APICall.RESEND_CONFIRMATION_API.getType(),
                Constants.RelationStates.RESEND_REL, RecoveryUtil
                        .buildURIForBody(tenantDomain, Constants.APICall.RESEND_CONFIRMATION_API.getApiUrl(),
                                Constants.ACCOUNT_RECOVERY_ENDPOINT_BASEPATH), null));
        if (NotificationChannels.EXTERNAL_CHANNEL.getChannelType().equals(notificationChannel)) {
            return Response.ok()
                    .entity(buildPasswordRecoveryExternalResponse(passwordRecoverDTO, apiCallsArrayList)).build();
        } else {
            return Response.accepted()
                    .entity(buildPasswordRecoveryInternalResponse(passwordRecoverDTO, apiCallsArrayList)).build();
        }
    }

    /**
     * Build PasswordRecoveryExternalNotifyResponse for successful password recovery.
     *
     * @param passwordRecoverDTO {@link PasswordRecoverDTO}PasswordRecoverDTO object
     * @param apiCallsArrayList  List of available API calls
     * @return {@link PasswordRecoveryExternalNotifyResponse} Password recovery external notify response
     */
    private PasswordRecoveryExternalNotifyResponse buildPasswordRecoveryExternalResponse(
            PasswordRecoverDTO passwordRecoverDTO, ArrayList<APICall> apiCallsArrayList) {

        PasswordRecoveryExternalNotifyResponse passwordRecoveryResponse =
                new PasswordRecoveryExternalNotifyResponse();
        passwordRecoveryResponse.setCode(passwordRecoverDTO.getCode());
        passwordRecoveryResponse.setMessage(passwordRecoverDTO.getMessage());
        passwordRecoveryResponse.setNotificationChannel(passwordRecoverDTO.getNotificationChannel());
        passwordRecoveryResponse.setConfirmationCode(passwordRecoverDTO.getConfirmationCode());
        passwordRecoveryResponse.setResendCode(passwordRecoverDTO.getResendCode());
        passwordRecoveryResponse.setLinks(apiCallsArrayList);
        return passwordRecoveryResponse;
    }

    /**
     * Build PasswordRecoveryInternalNotifyResponse for successful password recovery.
     *
     * @param passwordRecoverDTO {@link PasswordRecoverDTO}PasswordRecoverDTO object
     * @param apiCallsArrayList  List of available API calls
     * @return {@link PasswordRecoveryInternalNotifyResponse}
     */
    private PasswordRecoveryInternalNotifyResponse buildPasswordRecoveryInternalResponse(
            PasswordRecoverDTO passwordRecoverDTO, ArrayList<APICall> apiCallsArrayList) {

        PasswordRecoveryInternalNotifyResponse passwordRecoveryResponse =
                new PasswordRecoveryInternalNotifyResponse();
        passwordRecoveryResponse.setCode(passwordRecoverDTO.getCode());
        passwordRecoveryResponse.setMessage(passwordRecoverDTO.getMessage());
        passwordRecoveryResponse.setNotificationChannel(passwordRecoverDTO.getNotificationChannel());
        passwordRecoveryResponse.setLinks(apiCallsArrayList);
        passwordRecoveryResponse.setResendCode(passwordRecoverDTO.getResendCode());
        return passwordRecoveryResponse;
    }

    /**
     * Build recovery information for recover with notifications.
     *
     * @param recoveryType               Recovery type
     * @param recoveryChannelInformation RecoveryChannelInformation which wraps recovery channel information
     * @param apiCallsArrayList          Available API calls
     * @return AccountRecoveryType which wraps recovery options available for the user
     */
    private AccountRecoveryType buildAccountRecoveryType(String recoveryType,
                                                         RecoveryChannelInformation recoveryChannelInformation,
                                                         ArrayList<APICall> apiCallsArrayList) {

        AccountRecoveryType accountRecoveryType = new AccountRecoveryType();
        accountRecoveryType.setMode(recoveryType);
        accountRecoveryType.setChannelInfo(recoveryChannelInformation);
        accountRecoveryType.setLinks(apiCallsArrayList);
        return accountRecoveryType;
    }

    /**
     * Method to build recovery channel information.
     *
     * @param recoveryInformationDTO RecoveryInformation with recovery information
     * @return RecoveryChannelInformation
     */
    private RecoveryChannelInformation buildRecoveryChannelInformation(
            RecoveryInformationDTO recoveryInformationDTO) {

        RecoveryChannelInfoDTO recoveryChannelInfoDTO = recoveryInformationDTO.getRecoveryChannelInfoDTO();
        List<RecoveryChannel> channels = RecoveryUtil
                .buildRecoveryChannelInformation(recoveryChannelInfoDTO.getNotificationChannelDTOs());
        RecoveryChannelInformation recoveryChannelInformation = new RecoveryChannelInformation();
        recoveryChannelInformation.setRecoveryCode(recoveryChannelInfoDTO.getRecoveryCode());
        recoveryChannelInformation.setChannels(channels);
        return recoveryChannelInformation;
    }

    /*
    * Return AccountRecoveryType object with recovery options available for the user.
    * @param tenantDomain Tenant domain
    * @param recoveryInformationDTO RecoveryInformationDTO which wraps the password recovery information
    * @return AccountRecoveryType object with recovery options available for the user.
     */
    private Optional<AccountRecoveryType> buildPasswordRecoveryInitResponse(String tenantDomain,
                                                               RecoveryInformationDTO recoveryInformationDTO) {

        if (recoveryInformationDTO == null) {
            return Optional.empty();
        }

        AccountRecoveryType accountRecoveryType = null;
        boolean isNotificationBasedRecoveryEnabled = recoveryInformationDTO.isNotificationBasedRecoveryEnabled();
        boolean isQuestionBasedRecoveryAllowedForUser = recoveryInformationDTO.isQuestionBasedRecoveryAllowedForUser();

        if (isNotificationBasedRecoveryEnabled) {
            // Build next API calls list.
            ArrayList<APICall> apiCallsArrayList = new ArrayList<>();
            apiCallsArrayList.add(RecoveryUtil.buildApiCall(Constants.APICall.RECOVER_PASSWORD_API.getType(),
                    Constants.RelationStates.NEXT_REL, RecoveryUtil
                            .buildURIForBody(tenantDomain, Constants.APICall.RECOVER_PASSWORD_API.getApiUrl(),
                                    Constants.ACCOUNT_RECOVERY_ENDPOINT_BASEPATH), null));
            RecoveryChannelInformation recoveryChannelInformation = buildRecoveryChannelInformation(
                    recoveryInformationDTO);
            // Build recovery information for recover with notifications.
            accountRecoveryType = buildAccountRecoveryType(
                    Constants.RECOVERY_WITH_NOTIFICATIONS, recoveryChannelInformation, apiCallsArrayList);
        }
        if (isQuestionBasedRecoveryAllowedForUser) {
            // Build next API calls list.
            ArrayList<APICall> apiCallsArrayList = new ArrayList<>();
            apiCallsArrayList.add(RecoveryUtil
                    .buildApiCall(Constants.APICall.RECOVER_WITH_SECURITY_QUESTIONS_API.getType(),
                            Constants.RelationStates.NEXT_REL, RecoveryUtil.buildURIForBody(tenantDomain,
                                    Constants.APICall.RECOVER_WITH_SECURITY_QUESTIONS_API.getApiUrl(),
                                    Constants.CHALLENGE_QUESTIONS_ENDPOINT_BASEPATH),
                            recoveryInformationDTO.getUsername()));
            // Build recovery information for recover with security questions.
            accountRecoveryType = buildAccountRecoveryType(
                    Constants.RECOVER_WITH_CHALLENGE_QUESTIONS, null, apiCallsArrayList);
        }
        return Optional.ofNullable(accountRecoveryType);
    }

    /*
     * Check whether the challenge question based recovery is enabled.
     * @param tenantDomain Tenant domain
     * @return true if challenge question based recovery is enabled.
     */
    private boolean isQuestionBasedRecoveryEnabled(String tenantDomain) throws IdentityRecoveryServerException {

        // Check whether the challenge question based recovery is enabled.
        try {
            return Boolean.parseBoolean(
                    Utils.getRecoveryConfigs(IdentityRecoveryConstants.ConnectorConfig.QUESTION_BASED_PW_RECOVERY,
                            tenantDomain));
        } catch (IdentityRecoveryServerException e) {
            // Prepend scenario to the thrown exception.
            String errorCode = Utils
                    .prependOperationScenarioToErrorCode(IdentityRecoveryConstants.PASSWORD_RECOVERY_SCENARIO,
                            e.getErrorCode());
            throw Utils.handleServerException(errorCode, e.getMessage(), null);
        }
    }

    /*
     * Check whether the notification based recovery is enabled.
     * @param tenantDomain Tenant domain
     * @return true if notification based recovery is enabled.
     */
    private boolean isNotificationBasedRecoveryEnabled(String tenantDomain) throws IdentityRecoveryServerException {

        // Check whether the challenge question based recovery is enabled.
        try {
            return Boolean.parseBoolean(
                    Utils.getRecoveryConfigs(IdentityRecoveryConstants.ConnectorConfig.NOTIFICATION_BASED_PW_RECOVERY,
                            tenantDomain));
        } catch (IdentityRecoveryServerException e) {
            // Prepend scenario to the thrown exception.
            String errorCode = Utils
                    .prependOperationScenarioToErrorCode(IdentityRecoveryConstants.PASSWORD_RECOVERY_SCENARIO,
                            e.getErrorCode());
            throw Utils.handleServerException(errorCode, e.getMessage(), null);
        }
    }
}
