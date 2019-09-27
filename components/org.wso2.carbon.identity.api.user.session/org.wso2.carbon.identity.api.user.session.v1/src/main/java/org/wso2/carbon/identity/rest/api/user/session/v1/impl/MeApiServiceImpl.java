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
import org.wso2.carbon.identity.rest.api.user.session.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.session.v1.core.SessionManagementService;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionsDTO;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.ContextLoader.getUserFromContext;

/**
 * API service implementation of the authenticated user's session related operations.
 */
public class MeApiServiceImpl extends MeApiService {

    @Autowired
    private SessionManagementService sessionManagementService;

    @Override
    public Response getSessionsOfLoggedInUser(Integer limit, Integer offset, String filter, String sort) {

        SessionsDTO sessionsOfUser = sessionManagementService.getSessionsBySessionId(getUserFromContext(), limit,
                offset, filter, sort);

        if (sessionsOfUser == null || sessionsOfUser.getSessions().isEmpty()) {
            return Response.ok().entity("{}").type(MediaType.APPLICATION_JSON).build();
        } else {
            return Response.ok().entity(sessionsOfUser).build();
        }
    }

    @Override
    public Response terminateSessionByLoggedInUser(String sessionId) {

        sessionManagementService.terminateSessionBySessionId(getUserFromContext(), sessionId);

        return Response.noContent().build();
    }

    @Override
    public Response terminateSessionsByLoggedInUser() {

        sessionManagementService.terminateSessionsByUserId(getUserFromContext());

        return Response.noContent().build();
    }
}
