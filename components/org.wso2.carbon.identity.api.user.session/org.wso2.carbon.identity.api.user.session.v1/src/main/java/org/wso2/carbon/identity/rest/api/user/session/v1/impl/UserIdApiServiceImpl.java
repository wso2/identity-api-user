/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.session.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.function.UserIdToUser;
import org.wso2.carbon.identity.rest.api.user.session.v1.UserIdApiService;
import org.wso2.carbon.identity.rest.api.user.session.v1.core.SessionManagementService;

import javax.ws.rs.core.Response;

/**
 * API service implementation of a specific user's session related operations.
 */
public class UserIdApiServiceImpl extends UserIdApiService {

    @Autowired
    private SessionManagementService sessionManagementService;

    @Override
    public Response getSessionsByUserId(String userId, Integer limit, Integer offset, String filter, String sort) {

        return Response.ok().entity(sessionManagementService.getSessionsBySessionId(
                new UserIdToUser().apply(userId, ContextLoader.getTenantDomainFromContext()), limit, offset, filter,
                sort)).build();
    }

    @Override
    public Response terminateSessionBySessionId(String userId, String sessionId) {

        sessionManagementService.terminateSessionBySessionId(new UserIdToUser().apply(userId, ContextLoader
                .getTenantDomainFromContext()), sessionId);

        return Response.noContent().build();
    }

    @Override
    public Response terminateSessionsByUserId(String userId) {

        sessionManagementService.terminateSessionsByUserId(new UserIdToUser().apply(userId, ContextLoader
                .getTenantDomainFromContext()));

        return Response.noContent().build();
    }
}
