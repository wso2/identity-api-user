package org.wso2.carbon.identity.rest.api.user.association.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.user.common.function.UniqueIdToUser;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.rest.api.user.association.v1.UserIdApiService;
import org.wso2.carbon.identity.rest.api.user.association.v1.core.UserAssociationService;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.FederatedAssociationRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.util.UserAssociationServiceHolder;

import javax.ws.rs.core.Response;

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
    public Response userIdFederatedAssociationsPost(String userId,
                                                    FederatedAssociationRequestDTO federatedAssociation) {

        userAssociationService.addFederatedUserAccountAssociation(getUser(userId), federatedAssociation);
        return Response.noContent().build();
    }

    @Override
    public Response userIdFederatedAssociationsIdDelete(String userId, String id) {

        userAssociationService.deleteFederatedUserAccountAssociation(getUser(userId), id);
        return Response.noContent().build();
    }
}
