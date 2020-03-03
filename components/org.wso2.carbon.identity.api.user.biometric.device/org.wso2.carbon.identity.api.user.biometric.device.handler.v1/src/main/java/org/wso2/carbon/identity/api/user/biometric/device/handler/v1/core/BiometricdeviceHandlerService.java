package org.wso2.carbon.identity.api.user.biometric.device.handler.v1.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.wso2.carbon.identity.api.user.biometric.device.common.util.Constants;
import org.wso2.carbon.identity.api.user.biometric.device.common.util.HandleException;
import org.wso2.carbon.identity.api.user.biometric.device.handler.v1.model.DeviceDTO;
import org.wso2.carbon.identity.api.user.biometric.device.handler.v1.model.DiscoveryDataDTO;
import org.wso2.carbon.identity.api.user.biometric.device.handler.v1.model.RegistrationRequestDTO;
import org.wso2.carbon.identity.application.authenticator.biometric.device.handler.DeviceHandler;
import org.wso2.carbon.identity.application.authenticator.biometric.device.handler.exception.BiometricDeviceHandlerClientException;
import org.wso2.carbon.identity.application.authenticator.biometric.device.handler.exception.BiometricdeviceHandlerServerException;
import org.wso2.carbon.identity.application.authenticator.biometric.device.handler.impl.DeviceHandlerImpl;
import org.wso2.carbon.identity.application.authenticator.biometric.device.handler.model.Device;
import org.wso2.carbon.identity.application.authenticator.biometric.device.handler.model.DiscoveryData;
import org.wso2.carbon.identity.application.authenticator.biometric.device.handler.model.RegistrationRequest;
import org.wso2.carbon.user.api.UserStoreException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *.
 */
public class BiometricdeviceHandlerService {
    DeviceHandler deviceHandler;

    public String registerDevice(RegistrationRequestDTO registrationRequestDTO) {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        try {
            deviceHandler = new DeviceHandlerImpl();
            registrationRequest.setDeviceModel(registrationRequestDTO.getModel());
            registrationRequest.setDeviceName(registrationRequestDTO.getName());
            registrationRequest.setPublicKey((PublicKey) registrationRequestDTO.getPublickey());
            registrationRequest.setPushId(registrationRequestDTO.getPushId());
            registrationRequest.setSignature(registrationRequestDTO.getSignature());
            return deviceHandler.registerDevice(registrationRequest);

        } catch (BiometricDeviceHandlerClientException  e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_REGISTER_DEVICE_CLIENT_ERROR);
        } catch (BiometricdeviceHandlerServerException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_REGISTER_DEVICE_SERVER_ERROR);
        } catch (SignatureException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_INVALID_SIGNATURE);
        } catch (UnsupportedEncodingException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_INTERNAL_SERVER_ERROR);
        } catch (JsonProcessingException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE__JSON_PROCESSING_EXCEPTION);
        } catch (NoSuchAlgorithmException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_FAILED_SIGNATURE_VALIDATION);
        } catch (InvalidKeyException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_INVALID_SIGNATURE);
        } catch (UserStoreException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_USER_STORE_ERROR);
        } catch (SQLException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_DEVICE_HANDLER_SQL_EXCEPTION);
        }
    }
    public void unregisterDevice(String deviceId)  {
        try {
            deviceHandler = new DeviceHandlerImpl();
            deviceHandler.unregisterDevice(deviceId);
        } catch (BiometricDeviceHandlerClientException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_COOE_UNREGISTER_DEVICE_CLIENT_ERROR, deviceId);
        } catch (BiometricdeviceHandlerServerException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_COOE_UNREGISTER_DEVICE_SERVER_ERROR, deviceId);
        } catch (SQLException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_DEVICE_HANDLER_SQL_EXCEPTION);
        }

    }
    public void editDeviceName(String deviceId, String newDeviceName)  {
        try {
            deviceHandler = new DeviceHandlerImpl();
            deviceHandler.editDeviceName(deviceId, newDeviceName);
        } catch (BiometricDeviceHandlerClientException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_EDIT_DEVICE_NAME_CLIENT_ERROR, deviceId);
        } catch (BiometricdeviceHandlerServerException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_EDIT_DEVICE_NAME_SERVER_ERROR, deviceId);
        } catch (SQLException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_DEVICE_HANDLER_SQL_EXCEPTION);
        }

    }
    public DeviceDTO getDevice(String deviceId) {
        deviceHandler = new DeviceHandlerImpl();
        Device device = null;
        try {
            device = deviceHandler.getDevice(deviceId);
        } catch (BiometricDeviceHandlerClientException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_GET_DEVICE_CLIENT_ERROR, deviceId);
        } catch (SQLException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_DEVICE_HANDLER_SQL_EXCEPTION);
        } catch (BiometricdeviceHandlerServerException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_GET_DEVICE_SERVER_ERROR, deviceId);
        } catch (JsonProcessingException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE__JSON_PROCESSING_EXCEPTION);
        }
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setId(device.getDeviceId());
        deviceDTO.setName(device.getDeviceName());
        deviceDTO.setModel(device.getDeviceModel());
        deviceDTO.setPushId(device.getDeviceId());
        deviceDTO.setPublicKey(device.getPublicKey());
        deviceDTO.setRegistrationTime(device.getRegistrationTime());
        deviceDTO.setLastUsedTime(device.getLastUsedTime());
        return deviceDTO;
    }
    public ArrayList<DeviceDTO> listDevices()  {
        deviceHandler = new DeviceHandlerImpl();
        ArrayList<Device> devices = null;
        try {
            devices = deviceHandler.lisDevices();
        } catch (BiometricDeviceHandlerClientException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_LIST_DEVICE_CLIENT_ERROR);
        } catch (BiometricdeviceHandlerServerException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_LIST_DEVICE_SERVER_ERROR);
        } catch (JsonProcessingException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE__JSON_PROCESSING_EXCEPTION);
        } catch (SQLException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_DEVICE_HANDLER_SQL_EXCEPTION);
        } catch (UserStoreException e) {
            throw HandleException.handleException(e,
                    Constants.ErrorMessages.ERROR_CODE_USER_STORE_ERROR);
        }
        ArrayList<DeviceDTO> deviceDTOArrayList = new ArrayList<>();
        DeviceDTO deviceDTO = new DeviceDTO();
        if (devices != null) {
            for (Device device : devices) {
                deviceDTO.setId(device.getDeviceId());
                deviceDTO.setName(device.getDeviceName());
                deviceDTO.setModel(device.getDeviceModel());
                deviceDTO.setPushId(device.getDeviceId());
                deviceDTO.setPublicKey(device.getPublicKey());
                deviceDTO.setRegistrationTime(device.getRegistrationTime());
                deviceDTO.setLastUsedTime(device.getLastUsedTime());
                deviceDTOArrayList.add(deviceDTO);
            }
        }
        return deviceDTOArrayList;
    }
    public DiscoveryDataDTO getDiscoveryData() {
        deviceHandler = new DeviceHandlerImpl();
        DiscoveryData discoveryData;
        discoveryData = deviceHandler.getDiscoveryData();
        DiscoveryDataDTO discoveryDataDTO = new DiscoveryDataDTO();
        discoveryDataDTO.setUsername(discoveryData.getUsername());
        discoveryData.setTenantDomain(discoveryData.getTenantDomain());
        discoveryDataDTO.setUserStoreDomain(discoveryData.getUserStore());
        discoveryDataDTO.setChallenge(discoveryData.getChallenge());
        discoveryDataDTO.setRegistrationUrl(discoveryData.getRegistrationUrl());
        discoveryDataDTO.setAuthenticationUrl(discoveryData.getAuthenticationUrl());
        return discoveryDataDTO;
    }
}
