/*
 * Copyright (c) 2023-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.organization.v1.impl;

import org.wso2.carbon.identity.rest.api.user.organization.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.organization.v1.core.UserOrganizationService;
import org.wso2.carbon.identity.rest.api.user.organization.v1.factories.UserOrganizationServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Service implementation of user organization management API.
 */
public class MeApiServiceImpl implements MeApiService {

    private final UserOrganizationService userOrganizationService;

    public MeApiServiceImpl() {
        try {
            this.userOrganizationService = UserOrganizationServiceFactory.getUserOrganizationService();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while initiating UserOrganizationService.", e);
        }
    }

    @Override
    public Response rootGet(String filter, Integer limit, String after, String before, Boolean recursive,
                            String authorizedAppName) {

        return Response.ok(userOrganizationService.getAuthorizedOrganizations(filter, limit, after, before,
                recursive, authorizedAppName)).build();
    }
}
