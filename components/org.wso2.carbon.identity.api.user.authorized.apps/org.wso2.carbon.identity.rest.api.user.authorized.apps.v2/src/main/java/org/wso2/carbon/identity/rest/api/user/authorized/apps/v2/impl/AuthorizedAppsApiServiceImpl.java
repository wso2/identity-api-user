/*
 * Copyright (c) 2021-2025, WSO2 LLC. (http://www.wso2.com).
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

import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.AuthorizedAppsApiService;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.core.AuthorizedAppsService;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.factories.AuthorizedAppsServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Implementation of {@link AuthorizedAppsApiService}.
 */
public class AuthorizedAppsApiServiceImpl implements AuthorizedAppsApiService {

    private final AuthorizedAppsService authorizedAppsService;

    public AuthorizedAppsApiServiceImpl() {

        this.authorizedAppsService = AuthorizedAppsServiceFactory.getAuthorizedAppsService();
    }

    @Override
    public Response deleteIssuedTokensByAppId(String applicationId) {

        authorizedAppsService.deleteIssuedTokensByAppId(applicationId);
        return Response.noContent().build();
    }
}
