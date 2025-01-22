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

package org.wso2.carbon.identity.api.user.push.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.notification.push.device.handler.DeviceHandlerService;

/**
 * Push device manager OSGI service factory.
 */
public class PushDeviceManagerOSGIServiceFactory extends AbstractFactoryBean<DeviceHandlerService> {

    private DeviceHandlerService deviceHandlerService;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected DeviceHandlerService createInstance() throws Exception {

        if (this.deviceHandlerService == null) {
            DeviceHandlerService deviceHandlerService = (DeviceHandlerService) PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiService(DeviceHandlerService.class, null);
            if (deviceHandlerService != null) {
                this.deviceHandlerService = deviceHandlerService;
            } else {
                throw new Exception("Unable to retrieve DeviceHandlerService.");
            }
        }
        return this.deviceHandlerService;
    }
}
