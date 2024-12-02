/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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
import org.wso2.carbon.identity.rest.api.user.push.v1.DiscoveryDataApiService;
import org.wso2.carbon.identity.rest.api.user.push.v1.core.PushDeviceManagementService;

import javax.ws.rs.core.Response;

/**
 * Implementation class of Push device Handler User APIs.
 */
public class DiscoveryDataApiServiceImpl implements DiscoveryDataApiService {

    @Autowired
    private PushDeviceManagementService pushDeviceManagementService;

    @Override
    public Response getRegistrationDiscoveryData() {

        String responseDTO = pushDeviceManagementService.getRegistrationDiscoveryData();
        return Response.ok().entity(responseDTO).build();
    }
}
