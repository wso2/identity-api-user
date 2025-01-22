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

package org.wso2.carbon.identity.rest.api.user.push.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.push.v1.DevicesApiService;
import org.wso2.carbon.identity.rest.api.user.push.v1.core.PushDeviceManagementService;
import org.wso2.carbon.identity.rest.api.user.push.v1.model.DeviceDTO;
import org.wso2.carbon.identity.rest.api.user.push.v1.model.RegistrationRequestDTO;
import org.wso2.carbon.identity.rest.api.user.push.v1.model.RemoveRequestDTO;

import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Implementation class of Push device Handler User APIs.
 */
public class DevicesApiServiceImpl implements DevicesApiService {

    @Autowired
    private PushDeviceManagementService pushDeviceManagementService;

    @Override
    public Response deleteDeviceById(String deviceId) {

        pushDeviceManagementService.removeDevice(deviceId);
        return Response.noContent().build();
    }

    @Override
    public Response getDeviceById(String deviceId) {

        DeviceDTO deviceDTO = pushDeviceManagementService.getDevice(deviceId);
        return Response.ok().entity(deviceDTO).build();
    }

    @Override
    public Response getDevices() {

        List<DeviceDTO> deviceDTOList = pushDeviceManagementService.getDeviceByUserId();
        return Response.ok().entity(deviceDTOList).build();
    }

    @Override
    public Response registerDevice(RegistrationRequestDTO registrationRequestDTO) {

        pushDeviceManagementService.registerDevice(registrationRequestDTO);
        return Response.created(null).build();
    }

    @Override
    public Response removeDeviceFromMobile(String deviceId, RemoveRequestDTO removeRequestDTO) {

        String token = removeRequestDTO.getToken();
        pushDeviceManagementService.removeDeviceFromMobile(deviceId, token);
        return Response.noContent().build();
    }
}
