/*
 * Copyright (c) 2023-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.recovery.v2.impl.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.common.Util;
import org.wso2.carbon.identity.governance.service.notification.NotificationChannels;
import org.wso2.carbon.identity.recovery.IdentityRecoveryConstants;
import org.wso2.carbon.identity.recovery.IdentityRecoveryException;
import org.wso2.carbon.identity.recovery.dto.RecoveryChannelInfoDTO;
import org.wso2.carbon.identity.recovery.dto.RecoveryInformationDTO;
import org.wso2.carbon.identity.recovery.dto.UsernameRecoverDTO;

import org.wso2.carbon.identity.recovery.services.username.UsernameRecoveryManager;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.impl.core.utils.RecoveryUtil;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.APICall;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.AccountRecoveryType;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.InitRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.RecoveryChannel;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.RecoveryChannelInformation;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.RecoveryRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.UsernameRecoveryNotifyResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;

/**
 * Call internal OSGI services to perform username recovery.
 */
public class UsernameRecoveryService {

    private final UsernameRecoveryManager usernameRecoveryManager;

    private static final Log log = LogFactory.getLog(UsernameRecoveryService.class);

    public UsernameRecoveryService(UsernameRecoveryManager usernameRecoveryManager) {

        this.usernameRecoveryManager = usernameRecoveryManager;
    }

    /**
     * Initiate userName recovery from POST.
     *
     * @param initRequest {@link InitRequest} object which holds the information in the account recovery request
     * @return Response
     */
    public Response initiateUsernameRecovery(InitRequest initRequest) {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        Map<String, String> userClaims = RecoveryUtil.buildUserClaimsMap(initRequest.getClaims());
        try {
            // Get username recovery notification information.
            RecoveryInformationDTO recoveryInformationDTO = usernameRecoveryManager.initiate(userClaims, tenantDomain,
                    RecoveryUtil.buildPropertiesMap(initRequest.getProperties()));
            if (recoveryInformationDTO == null) {
                // If RecoveryChannelInfoDTO is null throw not found error.
                String message = "No recovery information username recovery request";
                log.debug(message);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            return Response.ok().entity(buildUsernameRecoveryInitResponse(recoveryInformationDTO, tenantDomain))
                    .build();
        } catch (IdentityRecoveryException e) {
            throw RecoveryUtil.handleIdentityRecoveryException(e, tenantDomain,
                    IdentityRecoveryConstants.USER_NAME_RECOVERY, Util.getCorrelation());
        }
    }

    /**
     * Send the recovery notifications to the user via user preferred channel given by the channelId param.
     *
     * @param recoveryRequest {@link RecoveryRequest} Object which holds the recovery information in the
     *                        username recovery request
     * @return Response
     */
    public Response recoverUsername(RecoveryRequest recoveryRequest) {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        String recoveryId = recoveryRequest.getRecoveryCode();
        String channelId = recoveryRequest.getChannelId();
        try {
            UsernameRecoverDTO usernameRecoverDTO = usernameRecoveryManager.notify(recoveryId, channelId, tenantDomain,
                    RecoveryUtil.buildPropertiesMap(recoveryRequest.getProperties()));
            if (usernameRecoverDTO == null) {
                if (log.isDebugEnabled()) {
                    String message = String.format("No recovery data object for recovery code : %s", recoveryId);
                    log.debug(message);
                }
                return Response.status(Response.Status.OK).build();
            }
            return buildUsernameRecoveryResponse(usernameRecoverDTO.getNotificationChannel(), usernameRecoverDTO);
        } catch (IdentityRecoveryException e) {
            throw RecoveryUtil.handleIdentityRecoveryException(e, tenantDomain,
                    IdentityRecoveryConstants.USER_NAME_RECOVERY, Util.getCorrelation());
        }
    }

    /**
     * Build the successful response according to the notification channel.
     *
     * @param notificationChannel Notification channel
     * @param usernameRecoverDTO  Object which holds the successful recovery information
     * @return Response
     */
    private Response buildUsernameRecoveryResponse(String notificationChannel, UsernameRecoverDTO usernameRecoverDTO) {

        UsernameRecoveryNotifyResponse usernameRecoveryNotifyResponse =
                new UsernameRecoveryNotifyResponse();
        usernameRecoveryNotifyResponse.setCode(usernameRecoverDTO.getCode());
        usernameRecoveryNotifyResponse.setMessage(usernameRecoverDTO.getMessage());
        usernameRecoveryNotifyResponse.setNotificationChannel(usernameRecoverDTO.getNotificationChannel());
        if (NotificationChannels.EXTERNAL_CHANNEL.getChannelType().equals(notificationChannel)) {
            usernameRecoveryNotifyResponse.setUsername(usernameRecoverDTO.getUsername());
            return Response.ok().entity(usernameRecoveryNotifyResponse).build();
        } else {
            return Response.accepted().entity(usernameRecoveryNotifyResponse).build();
        }
    }

    /**
     * Build a list of account recovery options available for successful user identification and channel retrieval.
     *
     * @param recoveryInformationDTO User recovery information object {@link RecoveryInformationDTO}
     * @param tenantDomain           Tenant domain
     * @return List of {@link AccountRecoveryType}
     */
    private List<AccountRecoveryType> buildUsernameRecoveryInitResponse(
            RecoveryInformationDTO recoveryInformationDTO, String tenantDomain) {

        RecoveryChannelInfoDTO recoveryChannelInfoDTO = recoveryInformationDTO.getRecoveryChannelInfoDTO();
        List<RecoveryChannel> channels = RecoveryUtil
                .buildRecoveryChannelInformation(recoveryChannelInfoDTO.getNotificationChannelDTOs());
        // Build Recovery Channel Information.
        RecoveryChannelInformation recoveryChannelInformation = new RecoveryChannelInformation();
        recoveryChannelInformation.setRecoveryCode(recoveryChannelInfoDTO.getRecoveryCode());
        recoveryChannelInformation.setChannels(channels);
        // Build next API calls.
        ArrayList<APICall> apiCallsArrayList = new ArrayList<>();
        apiCallsArrayList.add(RecoveryUtil
                .buildApiCall(APICalls.RECOVER_USERNAME_API.getType(), Constants.RelationStates.NEXT_REL,
                        RecoveryUtil.buildURIForBody(tenantDomain, APICalls.RECOVER_USERNAME_API.getApiUrl(),
                                Constants.ACCOUNT_RECOVERY_ENDPOINT_BASEPATH), null));
        // Build recovery type information.
        AccountRecoveryType accountRecoveryType = new AccountRecoveryType();
        accountRecoveryType.setMode(Constants.RECOVERY_WITH_NOTIFICATIONS);
        accountRecoveryType.setChannelInfo(recoveryChannelInformation);
        accountRecoveryType.setLinks(apiCallsArrayList);

        ArrayList<AccountRecoveryType> recoveryTypeDTOS = new ArrayList<>();
        recoveryTypeDTOS.add(accountRecoveryType);
        return recoveryTypeDTOS;
    }
}
