/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.push.v1.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.notification.push.device.handler.DeviceHandlerService;
import org.wso2.carbon.identity.notification.push.device.handler.exception.PushDeviceHandlerException;
import org.wso2.carbon.identity.notification.push.device.handler.model.Device;
import org.wso2.carbon.identity.notification.push.device.handler.model.RegistrationDiscoveryData;
import org.wso2.carbon.identity.notification.push.device.handler.model.RegistrationRequest;
import org.wso2.carbon.identity.rest.api.user.push.v1.model.DeviceDTO;
import org.wso2.carbon.identity.rest.api.user.push.v1.model.DiscoveryDataDTO;
import org.wso2.carbon.identity.rest.api.user.push.v1.model.RegistrationRequestDTO;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.identity.notification.push.device.handler.constant.PushDeviceHandlerConstants.ErrorMessages.ERROR_CODE_DEVICE_NOT_FOUND_FOR_USER_ID;
import static org.wso2.carbon.identity.rest.api.user.push.v1.util.Util.handlePushDeviceHandlerException;

/**
 * Service class for push device management.
 */
public class PushDeviceManagementService {

    private final DeviceHandlerService deviceHandlerService;
    private static final Log log = LogFactory.getLog(PushDeviceManagementService.class);

    /**
     * Constructor for PushDeviceManagementService.
     *
     * @param deviceHandlerService Device handler service.
     */
    public PushDeviceManagementService(DeviceHandlerService deviceHandlerService) {

        this.deviceHandlerService = deviceHandlerService;
    }

    /**
     * Get registration discovery data.
     *
     * @return Registration discovery data.
     */
    public String getRegistrationDiscoveryData() {

        User user = ContextLoader.getUserFromContext();
        String username = user.toFullQualifiedUsername();
        String tenantDomain = user.getTenantDomain();
        try {
            RegistrationDiscoveryData data = deviceHandlerService.getRegistrationDiscoveryData(username, tenantDomain);
            DiscoveryDataDTO discoveryDataDTO = buildDiscoveryDataDTO(data);
            Gson gson = new GsonBuilder().create();
            return gson.toJson(discoveryDataDTO);
        } catch (PushDeviceHandlerException e) {
            throw handlePushDeviceHandlerException(e);
        }
    }

    /**
     * Get device by device ID.
     *
     * @param deviceId Device ID.
     * @return Device.
     */
    public DeviceDTO getDevice(String deviceId) {

        try {
            Device device = deviceHandlerService.getDevice(deviceId);
            return buildDeviceDTO(device);
        } catch (PushDeviceHandlerException e) {
            throw handlePushDeviceHandlerException(e);
        }
    }

    /**
     * Get device by user ID.
     *
     * @return Device.
     */
    public List<DeviceDTO> getDeviceByUserId() {

        List<DeviceDTO> deviceDTOList = new ArrayList<>();
        try {
            User user = ContextLoader.getUserFromContext();
            String tenantDomain = user.getTenantDomain();
            String userId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUserId();
            Device device = deviceHandlerService.getDeviceByUserId(userId, tenantDomain);
            deviceDTOList.add(buildDeviceDTO(device));
        } catch (PushDeviceHandlerException e) {
            if (!ERROR_CODE_DEVICE_NOT_FOUND_FOR_USER_ID.getCode().equals(e.getErrorCode())) {
                throw handlePushDeviceHandlerException(e);
            }
        }
        return deviceDTOList;
    }

    /**
     * Remove device by device ID.
     *
     * @param deviceId Device ID.
     */
    public void removeDevice(String deviceId) {

        try {
            deviceHandlerService.unregisterDevice(deviceId);
        } catch (PushDeviceHandlerException e) {
            throw handlePushDeviceHandlerException(e);
        }
    }

    /**
     * Remove device from mobile.
     *
     * @param deviceId Device ID.
     * @param token    Token.
     */
    public void removeDeviceFromMobile(String deviceId, String token) {

        try {
            deviceHandlerService.unregisterDeviceMobile(deviceId, token);
        } catch (PushDeviceHandlerException e) {
            throw handlePushDeviceHandlerException(e);
        }
    }

    /**
     * Register a device.
     *
     * @param registrationRequestDTO Registration request DTO.
     */
    public void registerDevice(RegistrationRequestDTO registrationRequestDTO) {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        RegistrationRequest request = buildRegistrationRequest(registrationRequestDTO);
        try {
            deviceHandlerService.registerDevice(request, tenantDomain);
        } catch (PushDeviceHandlerException e) {
            throw handlePushDeviceHandlerException(e);
        }
    }

    /**
     * Build discovery data DTO.
     *
     * @param data Registration discovery data.
     * @return Discovery data DTO.
     */
    private DiscoveryDataDTO buildDiscoveryDataDTO(RegistrationDiscoveryData data) {

        DiscoveryDataDTO discoveryDataDTO = new DiscoveryDataDTO();
        discoveryDataDTO.setDeviceId(data.getDeviceId());
        discoveryDataDTO.setUsername(data.getUsername());
        discoveryDataDTO.setHost(data.getHost());
        discoveryDataDTO.setTenantDomain(data.getTenantDomain());
        if (StringUtils.isNotBlank(data.getOrganizationId())) {
            discoveryDataDTO.setOrganizationId(data.getOrganizationId());
            discoveryDataDTO.setOrganizationName(data.getOrganizationName());
        }
        discoveryDataDTO.setChallenge(data.getChallenge());
        return discoveryDataDTO;
    }

    /**
     * Build device DTO.
     *
     * @param device Device.
     * @return Device DTO.
     */
    private DeviceDTO buildDeviceDTO(Device device) {

        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceId(device.getDeviceId());
        deviceDTO.setName(device.getDeviceName());
        deviceDTO.setModel(device.getDeviceModel());
        deviceDTO.setProvider(device.getProvider());
        return deviceDTO;
    }

    /**
     * Build registration request.
     *
     * @param dto Registration request DTO.
     * @return Registration request.
     */
    private RegistrationRequest buildRegistrationRequest(RegistrationRequestDTO dto) {

        RegistrationRequest request = new RegistrationRequest();
        request.setDeviceId(dto.getDeviceId());
        request.setDeviceName(dto.getName());
        request.setDeviceModel(dto.getModel());
        request.setDeviceToken(dto.getDeviceToken());
        request.setPublicKey(dto.getPublicKey());
        request.setSignature(dto.getSignature());
        return request;
    }
}
