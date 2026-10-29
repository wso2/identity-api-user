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

package org.wso2.carbon.identity.rest.api.user.device.v1.impl;

import org.wso2.carbon.identity.rest.api.user.device.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.device.v1.core.DeviceManagementApiService;
import org.wso2.carbon.identity.rest.api.user.device.v1.factories.DeviceManagementApiServiceFactory;
import org.wso2.carbon.identity.rest.api.user.device.v1.model.DevicePatchRequest;

import javax.ws.rs.core.Response;

/**
 * Implementation of the user device management API.
 */
public class MeApiServiceImpl implements MeApiService {

    private final DeviceManagementApiService deviceManagementApiService;

    public MeApiServiceImpl() {

        try {
            this.deviceManagementApiService = DeviceManagementApiServiceFactory.getDeviceManagementApiService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating DeviceManagementApiService.", e);
        }
    }

    @Override
    public Response deleteMyDevice(String deviceId) {

        deviceManagementApiService.deleteMyDevice(deviceId);
        return Response.noContent().build();
    }

    @Override
    public Response getMyDevice(String deviceId) {

        return Response.ok().entity(deviceManagementApiService.getMyDevice(deviceId)).build();
    }

    @Override
    public Response listMyDevices(Integer limit, Integer offset) {

        return Response.ok().entity(deviceManagementApiService.listMyDevices(limit, offset)).build();
    }

    @Override
    public Response updateMyDevice(String deviceId, DevicePatchRequest devicePatchRequest) {

        return Response.ok().entity(deviceManagementApiService.updateMyDevice(deviceId, devicePatchRequest)).build();
    }
}
