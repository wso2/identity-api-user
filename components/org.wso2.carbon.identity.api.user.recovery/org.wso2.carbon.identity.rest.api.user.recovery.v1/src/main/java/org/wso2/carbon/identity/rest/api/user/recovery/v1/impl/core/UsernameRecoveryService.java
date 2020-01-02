/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
import org.wso2.carbon.identity.recovery.dto.RecoveryChannelInfoDTO;
import org.wso2.carbon.identity.recovery.dto.RecoveryInformationDTO;
import org.wso2.carbon.identity.recovery.dto.UsernameRecoverDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.APICallDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.AccountRecoveryTypeDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.InitRequestDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.RecoveryChannelDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.RecoveryChannelInformationDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.RecoveryRequestDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.UsernameRecoveryExternalNotifyResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.utils.RecoveryUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;

/**
 * Call internal OSGI services to perform username recovery.
 */
public class UsernameRecoveryService {

    private static final Log log = LogFactory.getLog(UsernameRecoveryService.class);

    /**
     * Initiate userName recovery from POST.
     *
     * @param initRequestDTO {@link InitRequestDTO} object which holds the information in the account recovery
     *                       request
     * @return Response
     */
    public Response initiateUsernameRecovery(InitRequestDTO initRequestDTO) {

        String tenantDomain = resolveTenantDomain();
        Map<String, String> userClaims = RecoveryUtil.buildUserClaimsMap(initRequestDTO.getClaims());
        try {
            // Get username recovery notification information.
            RecoveryInformationDTO recoveryInformationDTO =
                    UserAccountRecoveryServiceDataHolder.getUsernameRecoveryManager().initiate(userClaims, tenantDomain,
                            RecoveryUtil.buildPropertiesMap(initRequestDTO.getProperties()));
            if (recoveryInformationDTO == null) {
                // If RecoveryChannelInfoDTO is null throw not found error.
                if (log.isDebugEnabled()) {
                    String message = "No recovery information username recovery request";
                    log.debug(message);
                }
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            return Response.ok().entity(buildUsernameRecoveryInitResponse(recoveryInformationDTO, tenantDomain))
                    .build();
        } catch (IdentityRecoveryClientException e) {
            throw RecoveryUtil
                    .handleClientExceptions(UsernameRecoveryService.class.getName(), tenantDomain,
                            IdentityRecoveryConstants.USER_NAME_RECOVERY, Util.getCorrelation(), e);
        } catch (IdentityRecoveryException e) {
            throw RecoveryUtil.buildInternalServerErrorResponse(UsernameRecoveryService.class.getName(),
                    Constants.SERVER_ERROR, e.getErrorCode(), Util.getCorrelation(), e);
        } catch (Throwable throwable) {
            throw RecoveryUtil.buildInternalServerErrorResponse(UsernameRecoveryService.class.getName(),
                    Constants.SERVER_ERROR,
                    IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_UNEXPECTED_ERROR.getCode(),
                    Util.getCorrelation(), throwable);
        }
    }

    /**
     * Send the recovery notifications to the user via user preferred channel given by the channelId param.
     *
     * @param recoveryRequestDTO {@link RecoveryRequestDTO} Object which holds the recovery information in the
     *                           username recovery request
     * @return Response
     */
    public Response recoverUsername(RecoveryRequestDTO recoveryRequestDTO) {

        String tenantDomain = resolveTenantDomain();
        String recoveryId = recoveryRequestDTO.getRecoveryCode();
        String channelId = recoveryRequestDTO.getChannelId();
        try {
            UsernameRecoverDTO usernameRecoverDTO = UserAccountRecoveryServiceDataHolder.getUsernameRecoveryManager().
                    notify(recoveryId, channelId, tenantDomain,
                            RecoveryUtil.buildPropertiesMap(recoveryRequestDTO.getProperties()));
            if (usernameRecoverDTO == null) {
                if (log.isDebugEnabled()) {
                    String message = String.format("No recovery data object for recovery code : %s", recoveryId);
                    log.debug(message);
                }
                return Response.status(Response.Status.OK).build();
            }
            return buildUsernameRecoveryResponse(usernameRecoverDTO.getNotificationChannel(), usernameRecoverDTO);
        } catch (IdentityRecoveryClientException e) {
            throw RecoveryUtil
                    .handleClientExceptions(UsernameRecoveryService.class.getName(), tenantDomain,
                            IdentityRecoveryConstants.USER_NAME_RECOVERY, StringUtils.EMPTY, Util.getCorrelation(), e);
        } catch (IdentityRecoveryException e) {
            throw RecoveryUtil.buildInternalServerErrorResponse(UsernameRecoveryService.class.getName(),
                    Constants.SERVER_ERROR, e.getErrorCode(), Util.getCorrelation(), e);
        } catch (Throwable throwable) {
            throw RecoveryUtil.buildInternalServerErrorResponse(UsernameRecoveryService.class.getName(),
                    Constants.SERVER_ERROR,
                    IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_UNEXPECTED_ERROR.getCode(),
                    Util.getCorrelation(), throwable);
        }
    }

    /**
     * Build the successful response according to the notification channel.
     *
     * @param notificationChannel Notification channel
     * @param usernameRecoverDTO  {@link} Objects which holds the successful recovery information
     * @return Response
     */
    private Response buildUsernameRecoveryResponse(String notificationChannel, UsernameRecoverDTO usernameRecoverDTO) {

        UsernameRecoveryExternalNotifyResponseDTO successfulUsernameRecoveryDTO =
                new UsernameRecoveryExternalNotifyResponseDTO();
        successfulUsernameRecoveryDTO.setCode(usernameRecoverDTO.getCode());
        successfulUsernameRecoveryDTO.setMessage(usernameRecoverDTO.getMessage());
        successfulUsernameRecoveryDTO.setNotificationChannel(usernameRecoverDTO.getNotificationChannel());
        if (NotificationChannels.EXTERNAL_CHANNEL.getChannelType().equals(notificationChannel)) {
            successfulUsernameRecoveryDTO.setUsername(usernameRecoverDTO.getUsername());
            return Response.ok().entity(successfulUsernameRecoveryDTO).build();
        } else {
            return Response.accepted().entity(successfulUsernameRecoveryDTO).build();
        }
    }

    /**
     * Build a list of account recovery options available for successful user identification and channel retrieval.
     *
     * @param recoveryInformationDTO User recovery information object {@link RecoveryInformationDTO}
     * @param tenantDomain           Tenant domain
     * @return List of {@link AccountRecoveryTypeDTO}
     */
    private List<AccountRecoveryTypeDTO> buildUsernameRecoveryInitResponse(
            RecoveryInformationDTO recoveryInformationDTO, String tenantDomain) {

        RecoveryChannelInfoDTO recoveryChannelInfoDTO = recoveryInformationDTO.getRecoveryChannelInfoDTO();
        List<RecoveryChannelDTO> channels = RecoveryUtil
                .buildRecoveryChannelInformation(recoveryChannelInfoDTO.getNotificationChannelDTOs());
        // Build Recovery Channel Information.
        RecoveryChannelInformationDTO recoveryChannelInformationDTO = new RecoveryChannelInformationDTO();
        recoveryChannelInformationDTO.setRecoveryCode(recoveryChannelInfoDTO.getRecoveryCode());
        recoveryChannelInformationDTO.setChannels(channels);
        // Build next API calls.
        ArrayList<APICallDTO> apiCallDTOArrayList = new ArrayList<>();
        apiCallDTOArrayList.add(RecoveryUtil
                .buildApiCallDTO(Constants.APICall.RECOVER_USERNAME_API.getType(), Constants.RelationStates.NEXT_REL,
                        RecoveryUtil.buildUri(tenantDomain, Constants.APICall.RECOVER_USERNAME_API.getApiUrl(),
                                Constants.ACCOUNT_RECOVERY_ENDPOINT_BASEPATH), null));
        // Build recovery type information.
        AccountRecoveryTypeDTO accountRecoveryTypeDTO = new AccountRecoveryTypeDTO();
        accountRecoveryTypeDTO.setMode(Constants.RECOVERY_WITH_NOTIFICATIONS);
        accountRecoveryTypeDTO.setChannelInfo(recoveryChannelInformationDTO);
        accountRecoveryTypeDTO.setLinks(apiCallDTOArrayList);

        ArrayList<AccountRecoveryTypeDTO> recoveryTypeDTOS = new ArrayList<>();
        recoveryTypeDTOS.add(accountRecoveryTypeDTO);
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
