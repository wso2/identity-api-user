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

package org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.function.UniqueIdToUser;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.UserIdApiService;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.core.AuthorizedAppsService;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.dto.AuthorizedAppDTO;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Implementation of UserIdApi Service.
 */
public class UserIdApiServiceImpl implements UserIdApiService {

    @Autowired
    private AuthorizedAppsService authorizedAppsService;
    private static RealmService realmService = null;

    static {
        realmService = (RealmService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(RealmService.class, null);
    }

    @Override
    public Response deleteUserAuthorizedApps(String userId) {

        authorizedAppsService.deleteUserAuthorizedApps(getUser(userId));
        return Response.noContent().build();
    }

    @Override
    public Response deleteUserAuthorizedAppsByAppId(String userId, String applicationId) {

        authorizedAppsService.deleteUserAuthorizedApps(getUser(userId), applicationId);
        return Response.noContent().build();
    }

    @Override
    public Response listUserAuthorizedApps(String userId) {

        List<AuthorizedAppDTO> authorizedAppDTOs = authorizedAppsService.listUserAuthorizedApps(getUser(userId));
        return Response.ok().entity(authorizedAppDTOs).build();
    }

    @Override
    public Response listUserAuthorizedAppsByAppId(String userId, String applicationId) {

        AuthorizedAppDTO authorizedAppDTO = authorizedAppsService
                .listUserAuthorizedAppsByAppId(getUser(userId), applicationId);
        return Response.ok().entity(authorizedAppDTO).build();
    }

    private User getUser(String userId) {

        return new UniqueIdToUser().apply(realmService, userId, ContextLoader.getTenantDomainFromContext());
    }
}
