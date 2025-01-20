package org.wso2.carbon.identity.rest.api.user.push.v1.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.push.common.PushDeviceManagerServiceDataHolder;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.notification.push.device.handler.exception.PushDeviceHandlerException;
import org.wso2.carbon.identity.notification.push.device.handler.model.Device;
import org.wso2.carbon.identity.notification.push.device.handler.model.RegistrationDiscoveryData;
import org.wso2.carbon.identity.notification.push.device.handler.model.RegistrationRequest;
import org.wso2.carbon.identity.rest.api.user.push.v1.model.DeviceDTO;
import org.wso2.carbon.identity.rest.api.user.push.v1.model.DiscoveryDataDTO;
import org.wso2.carbon.identity.rest.api.user.push.v1.model.RegistrationRequestDTO;
import org.wso2.carbon.identity.rest.api.user.push.v1.model.RegistrationResponseDTO;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.identity.rest.api.user.push.v1.util.Util.handlePushDeviceHandlerException;

/**
 * Service class for push device management.
 */
public class PushDeviceManagementService {

    private static final Log log = LogFactory.getLog(PushDeviceManagementService.class);

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
            RegistrationDiscoveryData data = PushDeviceManagerServiceDataHolder.getDeviceHandlerService()
                    .getRegistrationDiscoveryData(username, tenantDomain);
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
            Device device = PushDeviceManagerServiceDataHolder.getDeviceHandlerService().getDevice(deviceId);
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

        try {
            User user = ContextLoader.getUserFromContext();
            String tenantDomain = user.getTenantDomain();
            String userId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUserId();
            Device device = PushDeviceManagerServiceDataHolder.getDeviceHandlerService().getDeviceByUserId(userId,
                    tenantDomain);
            List<DeviceDTO> deviceDTOList = new ArrayList<>();
            deviceDTOList.add(buildDeviceDTO(device));
            return deviceDTOList;
        } catch (PushDeviceHandlerException e) {
            throw handlePushDeviceHandlerException(e);
        }
    }

    /**
     * Remove device by device ID.
     *
     * @param deviceId Device ID.
     */
    public void removeDevice(String deviceId) {

        try {
            PushDeviceManagerServiceDataHolder.getDeviceHandlerService().unregisterDevice(deviceId);
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
            PushDeviceManagerServiceDataHolder.getDeviceHandlerService().unregisterDeviceMobile(deviceId, token);
        } catch (PushDeviceHandlerException e) {
            throw handlePushDeviceHandlerException(e);
        }
    }

    /**
     * Register a device.
     *
     * @param registrationRequestDTO Registration request DTO.
     * @return Registration response DTO.
     */
    public RegistrationResponseDTO registerDevice(RegistrationRequestDTO registrationRequestDTO) {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        RegistrationRequest request = buildRegistrationRequest(registrationRequestDTO);
        try {
            Device device = PushDeviceManagerServiceDataHolder.getDeviceHandlerService()
                    .registerDevice(request, tenantDomain);
            RegistrationResponseDTO responseDTO = new RegistrationResponseDTO();
            responseDTO.setDeviceId(device.getDeviceId());
            return responseDTO;
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
        discoveryDataDTO.setTenantPath(data.getTenantPath());
        if (StringUtils.isNotBlank(data.getOrganizationId())) {
            discoveryDataDTO.setOrganizationId(data.getOrganizationId());
            discoveryDataDTO.setOrganizationName(data.getOrganizationName());
            discoveryDataDTO.setOrganizationPath(data.getOrganizationPath());
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
