/*
 * Copyright (c) 2020-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.impl;

import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.MeApiService;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.core.AuthorizedAppsService;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.dto.AuthorizedAppDTO;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.factories.AuthorizedAppsServiceFactory;

import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.ContextLoader.getUserFromContext;

/**
* Implementation of MeApi Service.
*/
public class MeApiServiceImpl implements MeApiService {

    private final AuthorizedAppsService authorizedAppsService;

    public MeApiServiceImpl() {

        try {
            this.authorizedAppsService = AuthorizedAppsServiceFactory.getAuthorizedAppsService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating AuthorizedAppsService.", e);
        }
    }

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

        AuthorizedAppDTO authorizedAppDTO = authorizedAppsService.listUserAuthorizedAppsByAppId(getUserFromContext(),
                applicationId);
        return Response.ok().entity(authorizedAppDTO).build();
    }

    @Override
    public Response listLoggedInUserAuthorizedApps() {

        List<AuthorizedAppDTO> authorizedAppDTOs = authorizedAppsService.listUserAuthorizedApps(getUserFromContext());
        return Response.ok().entity(authorizedAppDTOs).build();
    }

}
