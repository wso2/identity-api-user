/*
 * Copyright (c) 2019-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.impl;

import org.wso2.carbon.identity.api.user.common.function.UniqueIdToUser;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.UserIdApiService;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.core.AuthorizedAppsService;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.dto.AuthorizedAppDTO;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.factories.AuthorizedAppsServiceFactory;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.utils.AuthorizedAppsServicesHolder;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.List;

import javax.ws.rs.core.Response;

/**
 * API service implementation of a specific user's OAuth authorized apps.
 */
public class UserIdApiServiceImpl extends UserIdApiService {

    private final AuthorizedAppsService authorizedAppsService;
    private final RealmService realmService;

    public UserIdApiServiceImpl() {

        try {
            this.authorizedAppsService = AuthorizedAppsServiceFactory.getAuthorizedAppsService();
            this.realmService = AuthorizedAppsServicesHolder.getRealmService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating the authorized app services.", e);
        }
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

        return new UniqueIdToUser().apply(realmService, userId, IdentityTenantUtil.resolveTenantDomain());
    }
}
