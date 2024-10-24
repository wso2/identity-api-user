/*
 * Copyright (c) 2019-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.association.v1.impl;

import org.wso2.carbon.identity.api.user.common.function.UniqueIdToUser;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.rest.api.user.association.v1.UserIdApiService;
import org.wso2.carbon.identity.rest.api.user.association.v1.core.UserAssociationService;
import org.wso2.carbon.identity.rest.api.user.association.v1.factories.UserAssociationServiceFactory;
import org.wso2.carbon.identity.rest.api.user.association.v1.util.UserAssociationServiceHolder;

import javax.ws.rs.core.Response;

/**
 * Association API service implementation for users/{userId} endpoint.
 */
public class UserIdApiServiceImpl extends UserIdApiService {

    private final UserAssociationService userAssociationService;

    public UserIdApiServiceImpl() {

        try {
            this.userAssociationService = UserAssociationServiceFactory.getUserAssociationService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating UserAssociationService.", e);
        }
    }

    @Override
    public Response userIdAssociationsDelete(String userId) {

        userAssociationService.deleteUserAccountAssociation(getUser(userId));
        return Response.noContent().build();
    }

    @Override
    public Response userIdAssociationsGet(String userId) {

        return Response.ok().entity(userAssociationService.getAssociationsOfUser(getUser(userId))).build();
    }

    private String getUser(String userId) {

        User user = new UniqueIdToUser().apply(UserAssociationServiceHolder.getRealmService(), userId,
                IdentityTenantUtil.resolveTenantDomain());
        return user.toFullQualifiedUsername();
    }

    @Override
    public Response userIdFederatedAssociationsGet(String userId) {

        return Response.ok().entity(userAssociationService.getFederatedAssociationsOfUser(getUser(userId))).build();
    }

    @Override
    public Response userIdFederatedAssociationsDelete(String userId) {

        userAssociationService.deleteFederatedUserAccountAssociation(getUser(userId));
        return Response.noContent().build();
    }

    @Override
    public Response userIdFederatedAssociationsIdDelete(String userId, String id) {

        userAssociationService.deleteFederatedUserAccountAssociation(getUser(userId), id);
        return Response.noContent().build();
    }
}
