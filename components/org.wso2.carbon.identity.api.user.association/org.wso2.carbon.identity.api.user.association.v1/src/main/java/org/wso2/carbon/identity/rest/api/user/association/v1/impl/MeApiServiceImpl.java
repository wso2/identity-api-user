package org.wso2.carbon.identity.rest.api.user.association.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.function.UniqueIdToUser;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.rest.api.user.association.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.association.v1.core.UserAssociationService;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationUserRequestDTO;
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

    @Autowired
    private UserAssociationService userAssociationService;

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
                ContextLoader.getTenantDomainFromContext());
        return user.toFullQualifiedUsername();
    }

    private URI getAssociationsLocationURI() {

        return buildURIForHeader(String.format(V1_API_PATH_COMPONENT + USER_ASSOCIATIONS_PATH_COMPONENT, ME_CONTEXT));
    }
}
