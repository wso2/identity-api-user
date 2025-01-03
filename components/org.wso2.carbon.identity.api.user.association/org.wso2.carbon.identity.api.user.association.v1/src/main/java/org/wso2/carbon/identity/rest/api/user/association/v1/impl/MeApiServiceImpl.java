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

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.common.function.UniqueIdToUser;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.rest.api.user.association.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.association.v1.core.UserAssociationService;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationUserRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.factories.UserAssociationServiceFactory;
import org.wso2.carbon.identity.rest.api.user.association.v1.util.UserAssociationServiceHolder;
import org.wso2.carbon.user.core.util.UserCoreUtil;

import java.net.URI;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.ContextLoader.buildURIForHeader;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.ME_CONTEXT;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.USER_ASSOCIATIONS_PATH_COMPONENT;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.V1_API_PATH_COMPONENT;

/**
 * Association API service implementation for users/me endpoint.
 */
public class MeApiServiceImpl extends MeApiService {

    private final UserAssociationService userAssociationService;

    public MeApiServiceImpl() {

        try {
            this.userAssociationService = UserAssociationServiceFactory.getUserAssociationService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating UserAssociationService.", e);
        }
    }

    @Override
    public Response meAssociationsDelete() {

        userAssociationService.deleteUserAccountAssociation(getFullyQualifiedUsernameFromContext());
        return Response.noContent().build();
    }

    @Override
    public Response meAssociationsAssociatedUserIdDelete(String associatedUserId) {

        userAssociationService.deleteAssociatedUserAccount(getFullyQualifiedUsernameFromContext(),
                getFullyQualifiedUserName(associatedUserId));
        return Response.noContent().build();
    }

    @Override
    public Response meAssociationsGet() {

        return Response.ok().entity(userAssociationService.getAssociationsOfUser(
                getFullyQualifiedUsernameFromContext())).build();
    }

    @Override
    public Response meAssociationsPost(AssociationUserRequestDTO association) {

        userAssociationService.createUserAccountAssociation(association);
        return Response.created(getAssociationsLocationURI()).build();
    }

    @Override
    public Response meFederatedAssociationsGet() {

        return Response.ok().entity(userAssociationService.getFederatedAssociationsOfUser(
                getFullyQualifiedUsernameFromContext())).build();
    }

    @Override
    public Response meFederatedAssociationsDelete() {

        userAssociationService.deleteFederatedUserAccountAssociation(getFullyQualifiedUsernameFromContext());
        return Response.noContent().build();
    }

    @Override
    public Response meFederatedAssociationsIdDelete(String id) {

        userAssociationService.deleteFederatedUserAccountAssociation(getFullyQualifiedUsernameFromContext(), id);
        return Response.noContent().build();
    }

    private String getFullyQualifiedUsernameFromContext() {

        String username = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
        return UserCoreUtil.addTenantDomainToEntry(username, PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getTenantDomain());
    }

    private String getFullyQualifiedUserName(String userId) {

        User user = new UniqueIdToUser().apply(UserAssociationServiceHolder.getRealmService(), userId,
                IdentityTenantUtil.resolveTenantDomain());
        return user.toFullQualifiedUsername();
    }

    private URI getAssociationsLocationURI() {

        return buildURIForHeader(String.format(V1_API_PATH_COMPONENT + USER_ASSOCIATIONS_PATH_COMPONENT, ME_CONTEXT));
    }
}
