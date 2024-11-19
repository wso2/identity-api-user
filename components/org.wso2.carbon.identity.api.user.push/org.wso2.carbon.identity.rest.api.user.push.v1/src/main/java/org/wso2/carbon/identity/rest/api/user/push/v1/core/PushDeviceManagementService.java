package org.wso2.carbon.identity.rest.api.user.push.v1.core;

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
    public DiscoveryDataDTO getRegistrationDiscoveryData() {

        User user = ContextLoader.getUserFromContext();
        String username = user.toFullQualifiedUsername();
        String tenantDomain = user.getTenantDomain();
        try {
            RegistrationDiscoveryData data = PushDeviceManagerServiceDataHolder.getDeviceHandlerService()
                    .getRegistrationDiscoveryData(username, tenantDomain);
            return buildDiscoveryDataDTO(data);
        } catch (PushDeviceHandlerException e) {
            throw handlePushDeviceHandlerException(e);
        }
    }

    public DeviceDTO getDevice(String deviceId) {

        try {
            Device device = PushDeviceManagerServiceDataHolder.getDeviceHandlerService().getDevice(deviceId);
            return buildDeviceDTO(device);
        } catch (PushDeviceHandlerException e) {
            throw handlePushDeviceHandlerException(e);
        }
    }

    public void removeDevice(String deviceId) {

        try {
            PushDeviceManagerServiceDataHolder.getDeviceHandlerService().unregisterDevice(deviceId);
        } catch (PushDeviceHandlerException e) {
            throw handlePushDeviceHandlerException(e);
        }
    }

    public void removeDeviceFromMobile(String deviceId, String token) {

        try {
            PushDeviceManagerServiceDataHolder.getDeviceHandlerService().unregisterDeviceMobile(deviceId, token);
        } catch (PushDeviceHandlerException e) {
            throw handlePushDeviceHandlerException(e);
        }
    }

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

    private DiscoveryDataDTO buildDiscoveryDataDTO(RegistrationDiscoveryData data) {

        DiscoveryDataDTO discoveryDataDTO = new DiscoveryDataDTO();
        discoveryDataDTO.setDid(data.getDeviceId());
        discoveryDataDTO.setUn(data.getUsername());
        discoveryDataDTO.setTd(data.getTenantDomain());
        if (StringUtils.isNotBlank(data.getOrganizationName())) {
            discoveryDataDTO.setTd(data.getOrganizationName());
        }
        discoveryDataDTO.setChg(data.getChallenge());
        discoveryDataDTO.setRe(data.getRegistrationEndpoint());
        discoveryDataDTO.setAe(data.getAuthenticationEndpoint());
        discoveryDataDTO.setRde(data.getRemoveDeviceEndpoint());
        return discoveryDataDTO;
    }

    private DeviceDTO buildDeviceDTO(Device device) {

        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceId(device.getDeviceId());
        deviceDTO.setName(device.getDeviceName());
        deviceDTO.setModel(device.getDeviceModel());
        deviceDTO.setProvider(device.getProvider());
        return deviceDTO;
    }

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
