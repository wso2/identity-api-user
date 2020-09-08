/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.functionality.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.functionality.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.functionality.v1.core.UserFunctionalityService;
import org.wso2.carbon.identity.rest.api.user.functionality.v1.model.UserStatusChangeRequest;

import javax.ws.rs.core.Response;

/**
 * Me API implementation of a specific user's functionality management.
 */
public class MeApiServiceImpl implements MeApiService {

    @Autowired
    private UserFunctionalityService userFunctionalityService;

    @Override
    public Response changeStatusOfLoggedInUser(String functionId, UserStatusChangeRequest userStatusChangeRequest) {

         return userFunctionalityService.changeStatusOfLoggedInUser(functionId, userStatusChangeRequest);
    }

    @Override
    public Response getLockStatusOfLoggedInUser(String functionId) {

        return Response.ok().entity(userFunctionalityService.getLockStatusOfLoggedInUser(functionId)).build();

    }
}
