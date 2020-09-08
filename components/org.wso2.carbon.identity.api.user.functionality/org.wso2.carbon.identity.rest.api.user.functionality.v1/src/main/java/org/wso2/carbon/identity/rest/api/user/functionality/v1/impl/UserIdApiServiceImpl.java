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
 */

package org.wso2.carbon.identity.rest.api.user.functionality.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.functionality.v1.UserIdApiService;
import org.wso2.carbon.identity.rest.api.user.functionality.v1.core.UserFunctionalityService;
import org.wso2.carbon.identity.rest.api.user.functionality.v1.model.StatusChangeRequest;

import javax.ws.rs.core.Response;

/**
 * API service implementation of a specific user's functionality management.
 */
public class UserIdApiServiceImpl implements UserIdApiService {

    @Autowired
    private UserFunctionalityService userFunctionalityService;

    @Override
    public Response changeStatus(String functionId, String userId, StatusChangeRequest statusChangeRequest) {

        return userFunctionalityService.changeStatus(userId, functionId, statusChangeRequest);
    }

    @Override
    public Response getLockStatus(String functionId, String userId) {

        return Response.ok().entity(userFunctionalityService.getLockStatus(userId, functionId)).build();
    }
}
