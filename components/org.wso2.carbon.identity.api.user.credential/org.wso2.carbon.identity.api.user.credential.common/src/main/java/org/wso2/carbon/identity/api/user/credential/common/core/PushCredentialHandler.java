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

package org.wso2.carbon.identity.api.user.credential.common.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.credential.common.CredentialHandler;
import org.wso2.carbon.identity.api.user.credential.common.CredentialManagementServiceDataHolder;
import org.wso2.carbon.identity.api.user.credential.common.dto.CredentialDTO;
import org.wso2.carbon.identity.api.user.credential.common.dto.CredentialGroupDTO;
import org.wso2.carbon.identity.api.user.credential.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.api.user.credential.common.utils.CredentialManagementUtils;
import org.wso2.carbon.identity.notification.push.device.handler.DeviceHandlerService;
import org.wso2.carbon.identity.notification.push.device.handler.exception.PushDeviceHandlerClientException;
import org.wso2.carbon.identity.notification.push.device.handler.exception.PushDeviceHandlerException;
import org.wso2.carbon.identity.notification.push.device.handler.model.Device;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.CredentialTypes.PUSH_AUTH;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_DELETE_PUSH_AUTH_DEVICE_CLIENT_FAILURE;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_DELETE_PUSH_AUTH_DEVICE_FAILURE;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_GET_CREDENTIALS_CLIENT_FAILURE;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_GET_PUSH_AUTH_DEVICE_FAILURE;
import static org.wso2.carbon.identity.notification.push.device.handler.constant.PushDeviceHandlerConstants.ErrorMessages.ERROR_CODE_DEVICE_NOT_FOUND_FOR_USER_ID;

/**
 * Credential handler implementation for Push Authentication.
 */
public class PushCredentialHandler implements CredentialHandler {

    private static final Log LOG = LogFactory.getLog(PushCredentialHandler.class);

    private final DeviceHandlerService deviceHandler;

    public PushCredentialHandler() {

        this.deviceHandler = CredentialManagementServiceDataHolder.getDeviceHandlerService();
    }

    @Override
    public CredentialGroupDTO getCredentials(String userId) throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving push authentication credential for user ID: " + userId);
        }
        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            // NOTE: This need to be updated when bringing the multiple device support.
            List<Device> pushCredentials = Collections.singletonList(deviceHandler
                    .getDeviceByUserId(userId, tenantDomain));
            List<CredentialDTO> credentialDTOs = pushCredentials.stream()
                    .map(this::mapPushToCredentialDTO).collect(Collectors.toList());

            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved push authentication credential for user ID: " + userId);
            }
            return new CredentialGroupDTO.Builder()
                    .type(PUSH_AUTH)
                    .isConfigured(!credentialDTOs.isEmpty())
                    .credentials(credentialDTOs)
                    .build();
        } catch (PushDeviceHandlerClientException e) {
            if (ERROR_CODE_DEVICE_NOT_FOUND_FOR_USER_ID.getCode().equals(e.getErrorCode())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No push authentication devices found for user ID: " + userId);
                }
                return new CredentialGroupDTO.Builder()
                        .type(PUSH_AUTH)
                        .isConfigured(false)
                        .build();
            }
            throw CredentialManagementUtils.handleClientException(
                    ERROR_CODE_GET_CREDENTIALS_CLIENT_FAILURE, e, PUSH_AUTH.apiValue(), userId);
        } catch (PushDeviceHandlerException e) {
            throw CredentialManagementUtils.handleServerException(
                    ERROR_CODE_GET_PUSH_AUTH_DEVICE_FAILURE, e, userId);
        }
    }

    @Override
    public void deleteCredentialById(String userId, String credentialId) throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting push authentication credential for user ID: " + userId);
        }
        try {
            deviceHandler.unregisterDevice(credentialId);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully deleted push authentication credential for user ID: " + userId);
            }
        } catch (PushDeviceHandlerClientException e) {
            throw CredentialManagementUtils.handleClientException(
                    ERROR_CODE_DELETE_PUSH_AUTH_DEVICE_CLIENT_FAILURE, e, credentialId, userId);
        } catch (PushDeviceHandlerException e) {
            throw CredentialManagementUtils.handleServerException(
                    ERROR_CODE_DELETE_PUSH_AUTH_DEVICE_FAILURE, e, credentialId, userId);
        }
    }

    private CredentialDTO mapPushToCredentialDTO(Device credential) {

        return new CredentialDTO.Builder()
                .credentialId(credential.getDeviceId())
                .displayName(credential.getDeviceName())
                .build();
    }
}
