/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.core.AuthorizedAppsService;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.dto.AuthorizedAppDTO;

import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.ContextLoader.getUserFromContext;

/**
 * API service implementation for authenticated user's OAuth authorized apps.
 */
public class MeApiServiceImpl extends MeApiService {

    @Autowired
    private AuthorizedAppsService authorizedAppsService;

    @Override
    public Response deleteLoggedInUserAuthorizedAppByAppId(String applicationId) {

        authorizedAppsService.deleteUserAuthorizedApps(getUserFromContext(), applicationId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteLoggedInUserAuthorizedApps() {

        authorizedAppsService.deleteUserAuthorizedApps(getUserFromContext());
        return Response.noContent().build();
    }

    @Override
    public Response getLoggedInUserAuthorizedAppByAppId(String applicationId) {

        authorizedAppsService.deleteUserAuthorizedApps(getUserFromContext(), applicationId);
        return Response.ok().build();
    }

    @Override
    public Response listLoggedInUserAuthorizedApps() {

        List<AuthorizedAppDTO> authorizedAppDTOs = authorizedAppsService.listUserAuthorizedApps(getUserFromContext());
        return Response.ok().entity(authorizedAppDTOs).build();
    }

}
