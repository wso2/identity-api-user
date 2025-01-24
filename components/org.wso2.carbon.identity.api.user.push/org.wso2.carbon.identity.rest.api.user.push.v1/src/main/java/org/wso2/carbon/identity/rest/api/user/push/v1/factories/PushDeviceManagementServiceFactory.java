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

package org.wso2.carbon.identity.rest.api.user.push.v1.factories;

import org.wso2.carbon.identity.api.user.push.common.PushDeviceManagerServiceDataHolder;
import org.wso2.carbon.identity.notification.push.device.handler.DeviceHandlerService;
import org.wso2.carbon.identity.rest.api.user.push.v1.core.PushDeviceManagementService;

/**
 * Factory class for PushDeviceManagementService.
 */
public class PushDeviceManagementServiceFactory {

    private static final PushDeviceManagementService SERVICE;

    static {

        DeviceHandlerService deviceHandlerService = PushDeviceManagerServiceDataHolder.getDeviceHandlerService();
        if (deviceHandlerService == null) {
            throw new IllegalStateException("DeviceHandlerService is not available for the OSGi context.");
        }
        SERVICE = new PushDeviceManagementService(deviceHandlerService);
    }

    /**
     * Get PushDeviceManagementService.
     *
     * @return PushDeviceManagementService
     */
    public static PushDeviceManagementService getPushDeviceManagementService() {

        return SERVICE;
    }
}
