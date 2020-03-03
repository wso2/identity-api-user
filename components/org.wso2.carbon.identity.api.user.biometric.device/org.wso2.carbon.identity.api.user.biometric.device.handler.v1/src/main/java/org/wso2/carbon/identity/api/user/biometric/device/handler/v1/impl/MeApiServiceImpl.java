/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.user.biometric.device.handler.v1.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.biometric.device.handler.v1.MeApiService;
import org.wso2.carbon.identity.api.user.biometric.device.handler.v1.core.BiometricdeviceHandlerService;
import org.wso2.carbon.identity.api.user.biometric.device.handler.v1.model.PatchDTO;
import org.wso2.carbon.identity.api.user.biometric.device.handler.v1.model.RegistrationRequestDTO;
import java.text.MessageFormat;
import javax.ws.rs.core.Response;


/**
 * Implementation class of Biometric device Handler User APIs.
 */
public class MeApiServiceImpl implements MeApiService {
    private static final Log log = LogFactory.getLog(MeApiServiceImpl.class);

    @Autowired
    private BiometricdeviceHandlerService deviceHandlerService;

    @Override
    public Response meBiometricDeviceDeviceIdDelete(String deviceId) {
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Removing device : {0} ", deviceId));
        }
            deviceHandlerService = new BiometricdeviceHandlerService();
            deviceHandlerService.unregisterDevice(deviceId);
            return Response.noContent().build();
    }

    @Override
    public Response meBiometricDeviceDeviceIdGet(String deviceId) {
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Fetching data of device : {0}", deviceId));
        }
            deviceHandlerService = new BiometricdeviceHandlerService();
            deviceHandlerService.getDevice(deviceId);
            return Response.noContent().build();
    }

    @Override
    public Response meBiometricDeviceDeviceIdPatch(String deviceId, PatchDTO patch) {
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("The device name could not be modified of device : {0} ", deviceId));
        }
            deviceHandlerService = new BiometricdeviceHandlerService();
            deviceHandlerService.editDeviceName(deviceId, patch.getValue());

        return Response.ok().build();
    }

    @Override
    public Response meBiometricDeviceDevicesGet() {
        if (log.isDebugEnabled()) {
            log.debug("Retrieving all devices of user ");
        }
            deviceHandlerService = new BiometricdeviceHandlerService();
            return Response.ok().entity(deviceHandlerService.listDevices()).build();
    }

    @Override
    public Response meBiometricDevicePost(RegistrationRequestDTO registrationRequest) {
        String deviceId;
        if (log.isDebugEnabled() && registrationRequest != null) {
            log.debug("Received registration request from mobile device");
        }
            deviceHandlerService = new BiometricdeviceHandlerService();
            deviceId = deviceHandlerService.registerDevice(registrationRequest);
            return Response.ok().entity(deviceId).build();
    }

    @Override
    public Response meBiometricdeviceDiscoveryDataGet() {
        if (log.isDebugEnabled()) {
            log.debug("Fetching data to generate QR code");
        }
            deviceHandlerService = new BiometricdeviceHandlerService();
            return Response.ok().entity(deviceHandlerService.getDiscoveryData()).build();
    }
}
