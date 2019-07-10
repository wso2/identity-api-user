package org.wso2.carbon.identity.rest.api.user.association.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.function.UserIdToUser;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.rest.api.user.association.v1.UserIdApiService;
import org.wso2.carbon.identity.rest.api.user.association.v1.core.UserAssociationService;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationRequestDTO;

import java.net.URI;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.ContextLoader.buildURI;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.USER_ASSOCIATIONS_PATH_COMPONENT;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.V1_API_PATH_COMPONENT;

/**
 * Association API service implementation for users/{userId} endpoint.
 */
public class UserIdApiServiceImpl extends UserIdApiService {

    @Autowired
    private UserAssociationService userAssociationService;

    @Override
    public Response userIdAssociationsDelete(String userId) {

        userAssociationService.deleteUserAccountAssociation(getUser(userId));
        return Response.noContent().build();
    }

    @Override
    public Response userIdAssociationsGet(String userId) {

        return Response.ok().entity(userAssociationService.getAssociationsOfUser(getUser(userId))).build();
    }

    @Override
    public Response userIdAssociationsPost(AssociationRequestDTO association, String userId) {

        association.setAssociateUserId(getUser(association.getAssociateUserId()));
        userAssociationService.createUserAccountAssociation(association, getUser(userId));
        return Response.created(getAssociationsLocationURI(userId)).build();
    }

    private URI getAssociationsLocationURI(String userId) {

        return buildURI(String.format(V1_API_PATH_COMPONENT + USER_ASSOCIATIONS_PATH_COMPONENT, userId));
    }

    private String getUser(String userId) {

        User user = new UserIdToUser().apply(userId, ContextLoader.getTenantDomainFromContext());
        return user.toFullQualifiedUsername();
    }
}
