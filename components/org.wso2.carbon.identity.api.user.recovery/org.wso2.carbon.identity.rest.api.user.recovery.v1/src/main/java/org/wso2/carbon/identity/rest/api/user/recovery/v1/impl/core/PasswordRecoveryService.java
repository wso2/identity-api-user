/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.identity.api.user.common.Util;
import org.wso2.carbon.identity.api.user.recovery.commons.UserAccountRecoveryServiceDataHolder;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.governance.service.notification.NotificationChannels;
import org.wso2.carbon.identity.recovery.IdentityRecoveryClientException;
import org.wso2.carbon.identity.recovery.IdentityRecoveryConstants;
import org.wso2.carbon.identity.recovery.IdentityRecoveryException;
import org.wso2.carbon.identity.recovery.dto.PasswordRecoverDTO;
import org.wso2.carbon.identity.recovery.dto.PasswordResetCodeDTO;
import org.wso2.carbon.identity.recovery.dto.RecoveryChannelInfoDTO;
import org.wso2.carbon.identity.recovery.dto.RecoveryInformationDTO;
import org.wso2.carbon.identity.recovery.dto.ResendConfirmationDTO;
import org.wso2.carbon.identity.recovery.dto.SuccessfulPasswordResetDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.APICallDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.AccountRecoveryTypeDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.ConfirmRequestDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.InitRequestDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.PasswordRecoveryExternalNotifyResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.PasswordRecoveryInternalNotifyResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.PasswordResetResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.RecoveryChannelDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.RecoveryChannelInformationDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.RecoveryRequestDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.ResendConfirmationCodeResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.ResendConfirmationRequestDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.ResetCodeResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.ResetRequestDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.utils.RecoveryUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;

/**
 * Call internal OSGI services to perform password recovery.
 */
public class PasswordRecoveryService {

    private static final Log log = LogFactory.getLog(PasswordRecoveryService.class.getName());

    /**
     * Initiate Password Recovery from POST.
     *
     * @param initRequestDTO {@link InitRequestDTO} object which holds the information in the account recovery
     *                       request
     * @return Response
     */
    public Response initiatePasswordRecovery(InitRequestDTO initRequestDTO) {

        String tenantDomain = resolveTenantDomain();
        Map<String, String> userClaims = RecoveryUtil.buildUserClaimsMap(initRequestDTO.getClaims());
        try {
            // Get password recovery notification information.
            RecoveryInformationDTO recoveryInformationDTO =
                    UserAccountRecoveryServiceDataHolder.getPasswordRecoveryManager().initiate(userClaims, tenantDomain,
                            RecoveryUtil.buildPropertiesMap(initRequestDTO.getProperties()));
            // If RecoveryChannelInfoDTO is null throw not found error.
            if (recoveryInformationDTO == null) {
                if (log.isDebugEnabled()) {
                    String message = "No recovery information for password recovery request";
                    log.debug(message);
                }
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            return Response.ok().entity(buildPasswordRecoveryInitResponse(tenantDomain, recoveryInformationDTO))
                    .build();
        } catch (IdentityRecoveryClientException e) {
            throw RecoveryUtil.handleClientExceptions(PasswordRecoveryService.class.getName(), tenantDomain,
                    IdentityRecoveryConstants.PASSWORD_RECOVERY_SCENARIO, Util.getCorrelation(), e);
        } catch (IdentityRecoveryException e) {
            throw RecoveryUtil.buildInternalServerErrorResponse(PasswordRecoveryService.class.getName(),
                    Constants.SERVER_ERROR, e.getErrorCode(), Util.getCorrelation(), e);
        } catch (Throwable throwable) {
            throw RecoveryUtil
                    .buildInternalServerErrorResponse(PasswordRecoveryService.class.getName(),
                            Constants.SERVER_ERROR,
                            IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_UNEXPECTED_ERROR.getCode(), Util
                                    .getCorrelation(), throwable);
        }
    }

    /**
     * Send the recovery notifications to the user via user preferred channel given by the channelId param.
     *
     * @param recoveryRequestDTO {@link RecoveryRequestDTO} Object which holds the recovery information in the
     *                           password recovery request
     * @return Response
     */
    public Response recoverPassword(RecoveryRequestDTO recoveryRequestDTO) {

        String tenantDomain = resolveTenantDomain();
        String recoveryId = recoveryRequestDTO.getRecoveryCode();
        String channelId = recoveryRequestDTO.getChannelId();
        try {
            PasswordRecoverDTO passwordRecoverDTO = UserAccountRecoveryServiceDataHolder.getPasswordRecoveryManager()
                    .notify(recoveryId, channelId, tenantDomain,
                            RecoveryUtil.buildPropertiesMap(recoveryRequestDTO.getProperties()));
            if (passwordRecoverDTO == null) {
                if (log.isDebugEnabled()) {
                    String message = String
                            .format("No password recovery data object for recovery code : %s", recoveryId);
                    log.debug(message);
                }
                return Response.status(Response.Status.OK).build();
            }
            return buildPasswordRecoveryResponse(tenantDomain, passwordRecoverDTO.getNotificationChannel(),
                    passwordRecoverDTO);
        } catch (IdentityRecoveryClientException e) {
            throw RecoveryUtil.handleClientExceptions(PasswordRecoveryService.class.getName(), tenantDomain,
                    IdentityRecoveryConstants.PASSWORD_RECOVERY_SCENARIO, Util.getCorrelation(), e);
        } catch (IdentityRecoveryException e) {
            throw RecoveryUtil.buildInternalServerErrorResponse(PasswordRecoveryService.class.getName(),
                    Constants.SERVER_ERROR, e.getErrorCode(), Util.getCorrelation(), e);
        } catch (Throwable throwable) {
            throw RecoveryUtil
                    .buildInternalServerErrorResponse(PasswordRecoveryService.class.getName(),
                            Constants.SERVER_ERROR,
                            IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_UNEXPECTED_ERROR.getCode(),
                            Util.getCorrelation(), throwable);
        }
    }

    /**
     * Validate the recovery code given for verify password reset.
     *
     * @param confirmRequestDTO {@link ConfirmRequestDTO}Confirmation code object received for password recovery
     * @return Response
     */
    public Response confirmRecovery(ConfirmRequestDTO confirmRequestDTO) {

        String tenantDomain = resolveTenantDomain();
        try {
            PasswordResetCodeDTO passwordResetCodeDTO =
                    UserAccountRecoveryServiceDataHolder.getPasswordRecoveryManager()
                            .confirm(confirmRequestDTO.getConfirmationCode(), tenantDomain,
                                    RecoveryUtil.buildPropertiesMap(confirmRequestDTO.getProperties()));
            return Response.ok().entity(buildResetCodeResponseDTO(tenantDomain, passwordResetCodeDTO)).build();
        } catch (IdentityRecoveryClientException e) {
            throw RecoveryUtil.handleClientExceptions(PasswordRecoveryService.class.getName(), tenantDomain,
                    IdentityRecoveryConstants.PASSWORD_RECOVERY_SCENARIO, Util.getCorrelation(), e);
        } catch (IdentityRecoveryException e) {
            throw RecoveryUtil.buildInternalServerErrorResponse(PasswordRecoveryService.class.getName(),
                    Constants.SERVER_ERROR, e.getErrorCode(), Util.getCorrelation(), e);
        } catch (Throwable throwable) {
            throw RecoveryUtil
                    .buildInternalServerErrorResponse(PasswordRecoveryService.class.getName(), Constants.SERVER_ERROR,
                            IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_UNEXPECTED_ERROR.getCode(),
                            Util.getCorrelation(), throwable);
        }
    }

    /**
     * Update the password of the user with the new given password.
     *
     * @param requestDTO {@link ResetRequestDTO} Object which holds the password reset code and password reset
     *                   information
     * @return Response
     */
    public Response resetPassword(ResetRequestDTO requestDTO) {

        String tenantDomain = resolveTenantDomain();
        char[] password = requestDTO.getPassword().toCharArray();
        try {
            SuccessfulPasswordResetDTO successfulPasswordResetDTO =
                    UserAccountRecoveryServiceDataHolder.getPasswordRecoveryManager()
                            .reset(requestDTO.getResetCode(), password,
                                    RecoveryUtil.buildPropertiesMap(requestDTO.getProperties()));
            return Response.ok().entity(buildPasswordResetResponseDTO(successfulPasswordResetDTO)).build();
        } catch (IdentityRecoveryClientException e) {
            // Send the reset code again for a retry attempt.
            throw RecoveryUtil.handleClientExceptions(PasswordRecoveryService.class.getName(), tenantDomain,
                    IdentityRecoveryConstants.PASSWORD_RECOVERY_SCENARIO, requestDTO.getResetCode(),
                    Util.getCorrelation(), e);
        } catch (IdentityRecoveryException e) {
            throw RecoveryUtil.buildInternalServerErrorResponse(PasswordRecoveryService.class.getName(),
                    Constants.SERVER_ERROR, e.getErrorCode(), Util.getCorrelation(), e);
        } catch (Throwable throwable) {
            throw RecoveryUtil
                    .buildInternalServerErrorResponse(PasswordRecoveryService.class.getName(), Constants.SERVER_ERROR,
                            IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_UNEXPECTED_ERROR.getCode(),
                            Util.getCorrelation(), throwable);
        }
    }

    /**
     * Resend the recovery confirmation code to the user.
     *
     * @param resendConfirmationRequest ResendConfirmationRequestDTO {@link ResendConfirmationRequestDTO} object
     *                                  which wraps the resend request
     * @return Response
     */
    public Response resendConfirmation(ResendConfirmationRequestDTO resendConfirmationRequest) {

        String tenantDomain = resolveTenantDomain();
        String resendCode = resendConfirmationRequest.getResendCode();
        Map<String, String> properties = RecoveryUtil.buildPropertiesMap(resendConfirmationRequest.getProperties());
        try {
            ResendConfirmationDTO resendConfirmationDTO =
                    UserAccountRecoveryServiceDataHolder.getPasswordRecoveryManager()
                            .resend(tenantDomain, resendCode, properties);
            if (resendConfirmationDTO == null) {
                if (log.isDebugEnabled()) {
                    log.debug("No ResendConfirmationDTO data for resend code :" + resendCode);
                }
                return Response.status(Response.Status.OK).build();
            }
            return Response.ok().entity(buildResendConfirmationCodeResponseDTO(tenantDomain, resendConfirmationDTO))
                    .build();
        } catch (IdentityRecoveryClientException e) {
            throw RecoveryUtil.handleClientExceptions(PasswordRecoveryService.class.getName(), tenantDomain,
                    IdentityRecoveryConstants.PASSWORD_RECOVERY_SCENARIO, Util.getCorrelation(), e);
        } catch (IdentityRecoveryException e) {
            throw RecoveryUtil.buildInternalServerErrorResponse(PasswordRecoveryService.class.getName(),
                    Constants.SERVER_ERROR, e.getErrorCode(), Util.getCorrelation(), e);
        } catch (Throwable throwable) {
            throw RecoveryUtil
                    .buildInternalServerErrorResponse(PasswordRecoveryService.class.getName(), Constants.SERVER_ERROR,
                            IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_UNEXPECTED_ERROR.getCode(),
                            Util.getCorrelation(), throwable);
        }
    }

    /**
     * Build ResendConfirmationCodeResponseDTO for a successful resend operation.
     *
     * @param tenantDomain          Tenant domain
     * @param resendConfirmationDTO ResendConfirmationDTO
     * @return ResendConfirmationCodeResponseDTO
     */
    private ResendConfirmationCodeResponseDTO buildResendConfirmationCodeResponseDTO(String tenantDomain,
                                                                         ResendConfirmationDTO resendConfirmationDTO) {

        ArrayList<APICallDTO> apiCallDTOArrayList = new ArrayList<>();

        // Add confirm API call information.
        apiCallDTOArrayList.add(RecoveryUtil.buildApiCallDTO(Constants.APICall.CONFIRM_PASSWORD_RECOVERY_API.getType(),
                Constants.RelationStates.NEXT_REL, RecoveryUtil
                        .buildUri(tenantDomain, Constants.APICall.CONFIRM_PASSWORD_RECOVERY_API.getApiUrl(),
                                Constants.ACCOUNT_RECOVERY_ENDPOINT_BASEPATH), null));
        // Add resend confirmation code API call information.
        apiCallDTOArrayList.add(RecoveryUtil.buildApiCallDTO(Constants.APICall.RESEND_CONFIRMATION_API.getType(),
                Constants.RelationStates.RESEND_REL, RecoveryUtil
                        .buildUri(tenantDomain, Constants.APICall.RESEND_CONFIRMATION_API.getApiUrl(),
                                Constants.ACCOUNT_RECOVERY_ENDPOINT_BASEPATH), null));

        ResendConfirmationCodeResponseDTO resendConfirmationCodeResponseDTO = new ResendConfirmationCodeResponseDTO();
        resendConfirmationCodeResponseDTO.setCode(resendConfirmationDTO.getSuccessCode());
        resendConfirmationCodeResponseDTO.setMessage(resendConfirmationDTO.getSuccessMessage());
        resendConfirmationCodeResponseDTO.setNotificationChannel(resendConfirmationDTO.getNotificationChannel());
        resendConfirmationCodeResponseDTO.setResendCode(resendConfirmationDTO.getResendCode());
        resendConfirmationCodeResponseDTO.setLinks(apiCallDTOArrayList);
        return resendConfirmationCodeResponseDTO;
    }

    /**
     * Build successful response for successful password update.
     *
     * @param successfulPasswordResetDTO SuccessfulPasswordResetDTO object
     * @return SuccessfulPasswordResetDTO
     */
    private PasswordResetResponseDTO buildPasswordResetResponseDTO(
            SuccessfulPasswordResetDTO successfulPasswordResetDTO) {

        PasswordResetResponseDTO passwordResetResponseDTO = new PasswordResetResponseDTO();
        passwordResetResponseDTO.setCode(successfulPasswordResetDTO.getSuccessCode());
        passwordResetResponseDTO.setMessage(successfulPasswordResetDTO.getMessage());
        return passwordResetResponseDTO;
    }

    /**
     * Build the ResetCodeResponseDTO for successful confirmation code validation.
     *
     * @param tenantDomain         Tenant Domain
     * @param passwordResetCodeDTO {@link PasswordResetCodeDTO}PasswordResetCodeDTO
     * @return ResetCodeResponseDTO {@link ResetCodeResponseDTO}object with a password reset code
     */
    private ResetCodeResponseDTO buildResetCodeResponseDTO(String tenantDomain,
                                                           PasswordResetCodeDTO passwordResetCodeDTO) {

        // Build next API calls list.
        ArrayList<APICallDTO> apiCallDTOArrayList = new ArrayList<>();
        apiCallDTOArrayList.add(RecoveryUtil
                .buildApiCallDTO(Constants.APICall.RESET_PASSWORD_API.getType(), Constants.RelationStates.NEXT_REL,
                        RecoveryUtil.buildUri(tenantDomain, Constants.APICall.RESET_PASSWORD_API.getApiUrl(),
                                Constants.ACCOUNT_RECOVERY_ENDPOINT_BASEPATH), null));
        ResetCodeResponseDTO resetCodeResponseDTO = new ResetCodeResponseDTO();
        resetCodeResponseDTO.setResetCode(passwordResetCodeDTO.getPasswordResetCode());
        resetCodeResponseDTO.setLinks(apiCallDTOArrayList);
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
        ArrayList<APICallDTO> apiCallDTOArrayList = new ArrayList<>();
        apiCallDTOArrayList.add(RecoveryUtil.buildApiCallDTO(Constants.APICall.CONFIRM_PASSWORD_RECOVERY_API.getType(),
                Constants.RelationStates.NEXT_REL, RecoveryUtil
                        .buildUri(tenantDomain, Constants.APICall.CONFIRM_PASSWORD_RECOVERY_API.getApiUrl(),
                                Constants.ACCOUNT_RECOVERY_ENDPOINT_BASEPATH), null));

        if (NotificationChannels.EXTERNAL_CHANNEL.getChannelType().equals(notificationChannel)) {
            return Response.ok()
                    .entity(buildPasswordRecoveryExternalResponseDTO(passwordRecoverDTO, apiCallDTOArrayList)).build();
        } else {

            // Add resend confirmation code API call information.
            apiCallDTOArrayList.add(RecoveryUtil.buildApiCallDTO(Constants.APICall.RESEND_CONFIRMATION_API.getType(),
                    Constants.RelationStates.RESEND_REL, RecoveryUtil
                            .buildUri(tenantDomain, Constants.APICall.RESEND_CONFIRMATION_API.getApiUrl(),
                                    Constants.ACCOUNT_RECOVERY_ENDPOINT_BASEPATH), null));
            return Response.accepted()
                    .entity(buildPasswordRecoveryInternalResponseDTO(passwordRecoverDTO, apiCallDTOArrayList)).build();
        }
    }

    /**
     * Build PasswordRecoveryExternalNotifyResponseDTO for successful password recovery.
     *
     * @param passwordRecoverDTO  {@link PasswordRecoverDTO}PasswordRecoverDTO object
     * @param apiCallDTOArrayList List of available API calls
     * @return {@link PasswordRecoveryExternalNotifyResponseDTO} Password recovery external notify response
     */
    private PasswordRecoveryExternalNotifyResponseDTO buildPasswordRecoveryExternalResponseDTO(
            PasswordRecoverDTO passwordRecoverDTO, ArrayList<APICallDTO> apiCallDTOArrayList) {

        PasswordRecoveryExternalNotifyResponseDTO passwordRecoveryResponseDTO =
                new PasswordRecoveryExternalNotifyResponseDTO();
        passwordRecoveryResponseDTO.setCode(passwordRecoverDTO.getCode());
        passwordRecoveryResponseDTO.setMessage(passwordRecoverDTO.getMessage());
        passwordRecoveryResponseDTO.setNotificationChannel(passwordRecoverDTO.getNotificationChannel());
        passwordRecoveryResponseDTO.setConfirmationCode(passwordRecoverDTO.getConfirmationCode());
        passwordRecoveryResponseDTO.setLinks(apiCallDTOArrayList);
        return passwordRecoveryResponseDTO;
    }

    /**
     * Build PasswordRecoveryInternalNotifyResponseDTO for successful password recovery.
     *
     * @param passwordRecoverDTO  {@link PasswordRecoverDTO}PasswordRecoverDTO object
     * @param apiCallDTOArrayList List of available API calls
     * @return {@link PasswordRecoveryInternalNotifyResponseDTO}
     */
    private PasswordRecoveryInternalNotifyResponseDTO buildPasswordRecoveryInternalResponseDTO(
            PasswordRecoverDTO passwordRecoverDTO, ArrayList<APICallDTO> apiCallDTOArrayList) {

        PasswordRecoveryInternalNotifyResponseDTO passwordRecoveryResponseDTO =
                new PasswordRecoveryInternalNotifyResponseDTO();
        passwordRecoveryResponseDTO.setCode(passwordRecoverDTO.getCode());
        passwordRecoveryResponseDTO.setMessage(passwordRecoverDTO.getMessage());
        passwordRecoveryResponseDTO.setNotificationChannel(passwordRecoverDTO.getNotificationChannel());
        passwordRecoveryResponseDTO.setLinks(apiCallDTOArrayList);
        passwordRecoveryResponseDTO.setResendCode(passwordRecoverDTO.getResendCode());
        return passwordRecoveryResponseDTO;
    }

    /**
     * Build recovery information for recover with notifications.
     *
     * @param recoveryType                  Recovery type
     * @param recoveryChannelInformationDTO RecoveryChannelInformationDTO which wraps recovery channel information
     * @param apiCallDTOArrayList           Available API calls
     * @return AccountRecoveryTypeDTO which wraps recovery options available for the user
     */
    private AccountRecoveryTypeDTO buildAccountRecoveryType(String recoveryType,
                                                            RecoveryChannelInformationDTO recoveryChannelInformationDTO,
                                                            ArrayList<APICallDTO> apiCallDTOArrayList) {

        AccountRecoveryTypeDTO accountRecoveryTypeDTO = new AccountRecoveryTypeDTO();
        accountRecoveryTypeDTO.setMode(recoveryType);
        accountRecoveryTypeDTO.setChannelInfo(recoveryChannelInformationDTO);
        accountRecoveryTypeDTO.setLinks(apiCallDTOArrayList);
        return accountRecoveryTypeDTO;
    }

    /**
     * Method to build recovery channel information.
     *
     * @param recoveryInformationDTO RecoveryInformationDTO with recovery information
     * @return RecoveryChannelInformationDTO
     */
    private RecoveryChannelInformationDTO buildRecoveryChannelInformationDTO(
            RecoveryInformationDTO recoveryInformationDTO) {

        RecoveryChannelInfoDTO recoveryChannelInfoDTO = recoveryInformationDTO.getRecoveryChannelInfoDTO();
        List<RecoveryChannelDTO> channels = RecoveryUtil
                .buildRecoveryChannelInformation(recoveryChannelInfoDTO.getNotificationChannelDTOs());
        RecoveryChannelInformationDTO recoveryChannelInformationDTO = new RecoveryChannelInformationDTO();
        recoveryChannelInformationDTO.setRecoveryCode(recoveryChannelInfoDTO.getRecoveryCode());
        recoveryChannelInformationDTO.setChannels(channels);
        return recoveryChannelInformationDTO;
    }

    /**
     * Build a list of account recovery options available for a successful password recovery.
     *
     * @param tenantDomain           Tenant domain
     * @param recoveryInformationDTO RecoveryInformationDTO which wraps the password recovery information
     * @return List of {@link AccountRecoveryTypeDTO}
     */
    private List<AccountRecoveryTypeDTO> buildPasswordRecoveryInitResponse(String tenantDomain,
                                                                    RecoveryInformationDTO recoveryInformationDTO) {

        ArrayList<AccountRecoveryTypeDTO> recoveryTypeDTOS = new ArrayList<>();
        boolean isNotificationBasedRecoveryEnabled = recoveryInformationDTO.isNotificationBasedRecoveryEnabled();
        boolean isQuestionBasedRecoveryEnabled = recoveryInformationDTO.isQuestionBasedRecoveryEnabled();
        if (isNotificationBasedRecoveryEnabled) {
            // Build next API calls list.
            ArrayList<APICallDTO> apiCallDTOArrayList = new ArrayList<>();
            apiCallDTOArrayList.add(RecoveryUtil.buildApiCallDTO(Constants.APICall.RECOVER_PASSWORD_API.getType(),
                    Constants.RelationStates.NEXT_REL, RecoveryUtil
                            .buildUri(tenantDomain, Constants.APICall.RECOVER_PASSWORD_API.getApiUrl(),
                                    Constants.ACCOUNT_RECOVERY_ENDPOINT_BASEPATH), null));
            RecoveryChannelInformationDTO recoveryChannelInformationDTO = buildRecoveryChannelInformationDTO(
                    recoveryInformationDTO);
            // Build recovery information for recover with notifications.
            AccountRecoveryTypeDTO accountRecoveryTypeDTO = buildAccountRecoveryType(
                    Constants.RECOVERY_WITH_NOTIFICATIONS, recoveryChannelInformationDTO, apiCallDTOArrayList);
            recoveryTypeDTOS.add(accountRecoveryTypeDTO);
        }
        if (isQuestionBasedRecoveryEnabled) {
            // Build next API calls list.
            ArrayList<APICallDTO> apiCallDTOArrayList = new ArrayList<>();
            apiCallDTOArrayList.add(RecoveryUtil
                    .buildApiCallDTO(Constants.APICall.RECOVER_WITH_SECURITY_QUESTIONS_API.getType(),
                            Constants.RelationStates.NEXT_REL, RecoveryUtil.buildUri(tenantDomain,
                                    Constants.APICall.RECOVER_WITH_SECURITY_QUESTIONS_API.getApiUrl(),
                                    Constants.CHALLENGE_QUESTIONS_ENDPOINT_BASEPATH),
                            recoveryInformationDTO.getUsername()));
            // Build recovery information for recover with security questions.
            AccountRecoveryTypeDTO accountRecoveryTypeDTO = buildAccountRecoveryType(
                    Constants.RECOVER_WITH_CHALLENGE_QUESTIIONS, null, apiCallDTOArrayList);
            recoveryTypeDTOS.add(accountRecoveryTypeDTO);
        }
        return recoveryTypeDTOS;
    }

    /**
     * Resolve the tenant domain of the request.
     *
     * @return Tenant domain
     */
    private String resolveTenantDomain() {

        String tenantDomain = StringUtils.EMPTY;
        if (IdentityUtil.threadLocalProperties.get() != null) {
            if (IdentityUtil.threadLocalProperties.get().get(Constants.TENANT_NAME_FROM_CONTEXT) != null) {
                tenantDomain = (String) IdentityUtil.threadLocalProperties.get()
                        .get(Constants.TENANT_NAME_FROM_CONTEXT);
            }
        }
        if (StringUtils.isBlank(tenantDomain)) {
            tenantDomain = MultitenantConstants.SUPER_TENANT_DOMAIN_NAME;
            if (log.isDebugEnabled()) {
                log.debug("Tenant domain is not in the request. Therefore, domain set to : "
                        + MultitenantConstants.SUPER_TENANT_DOMAIN_NAME);
            }
        }
        return tenantDomain;
    }
}
