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

package org.wso2.carbon.identity.rest.api.user.device.v1.core.function;

import org.wso2.carbon.identity.device.mgt.api.model.Device;
import org.wso2.carbon.identity.rest.api.user.device.v1.model.DeviceResponse;

/**
 * Builds the API model of a device from the device management domain model. Only the fields that are safe to expose
 * to the device owner are mapped; the public key and the owner's user id are never part of the response.
 */
public class DeviceResponseBuilder {

    private DeviceResponseBuilder() {

    }

    /**
     * Builds a {@link DeviceResponse} from the given device.
     *
     * @param device Device domain model.
     * @return Device response.
     */
    public static DeviceResponse buildDeviceResponse(Device device) {

        DeviceResponse response = new DeviceResponse();
        response.setId(device.getId());
        response.setDeviceName(device.getDeviceName());
        response.setDeviceModel(device.getDeviceModel());
        if (device.getStatus() != null) {
            response.setStatus(device.getStatus().name());
        }
        if (device.getRegisteredAt() != null) {
            response.setRegisteredAt(device.getRegisteredAt().toInstant().toString());
        }
        return response;
    }
}
