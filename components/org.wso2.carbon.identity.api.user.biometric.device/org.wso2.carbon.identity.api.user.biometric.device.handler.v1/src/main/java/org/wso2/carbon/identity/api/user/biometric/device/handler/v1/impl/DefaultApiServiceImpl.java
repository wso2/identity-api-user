
/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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
 *
 */

package org.wso2.carbon.identity.api.user.biometric.device.handler.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.user.biometric.device.handler.v1.DefaultApiService;
import org.wso2.carbon.identity.api.user.biometric.device.handler.v1.core.BiometricdeviceHandlerService;

import javax.ws.rs.core.Response;

/**
 *Implementation class of Biometric device Handler Admin APIs .
 */
public class DefaultApiServiceImpl implements DefaultApiService {
    private static final Log log = LogFactory.getLog(DefaultApiServiceImpl.class);

    @Autowired
    private BiometricdeviceHandlerService deviceHandlerService;

    @Override
    public Response userIdBiometricDeviceDeviceIdDelete(String userId, String deviceId) {

        if (log.isDebugEnabled()) {
            log.debug("Removing device : {0} " + deviceId + " of User : {0} ");
        }
        deviceHandlerService = new BiometricdeviceHandlerService();
        deviceHandlerService.unregisterDevice(deviceId);
        return Response.noContent().build();
    }


    @Override
    public Response userIdBiometricDeviceDeviceIdGet(String userId, String deviceId) {
        if (log.isDebugEnabled()) {
            log.debug("Fetching data of device : {0}" + deviceId + " of user :{0}" + userId);
        }
        deviceHandlerService = new BiometricdeviceHandlerService();
        deviceHandlerService.getDevice(deviceId);
        return Response.noContent().build();
    }

    @Override
    public Response userIdBiometricDeviceDevicesGet(String userId) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving all devices of user ");
        }
        deviceHandlerService = new BiometricdeviceHandlerService();
        return Response.ok().entity(deviceHandlerService.listDevices()).build();
    }
}
