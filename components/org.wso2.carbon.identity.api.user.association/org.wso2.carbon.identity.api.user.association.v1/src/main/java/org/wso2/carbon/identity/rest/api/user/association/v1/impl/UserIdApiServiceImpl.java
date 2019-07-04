package org.wso2.carbon.identity.rest.api.user.association.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.association.v1.UserIdApiService;
import org.wso2.carbon.identity.rest.api.user.association.v1.core.UserAssociationService;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.UserDTO;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.ASSOCIATIONS_PATH;

public class UserIdApiServiceImpl extends UserIdApiService {

    @Autowired
    private UserAssociationService userAssociationService;

    @Override
    public Response userIdAssociationsAssociateUserIdDelete(String userId,String associateUserId){
        userAssociationService.validateAndDeleteUserAccountAssociation(associateUserId);
        return Response.ok().build();
    }

    @Override
    public Response userIdAssociationsDelete(String userId) {

        userAssociationService.deleteUserAccountAssociation(userId);
        return Response.ok().build();
    }

    @Override
    public Response userIdAssociationsGet(String userId) {

        List<UserDTO> userDTOList = userAssociationService.getAssociationsOfUser(userId);
        if (userDTOList.size() == 0) {
            return Response.noContent().build();
        }
        return Response.ok().entity(userDTOList).build();
    }

    @Override
    public Response userIdAssociationsPost(AssociationRequestDTO association, String userId) {

        userAssociationService.createUserAccountAssociation(association, userId);
        try {
            return Response.created(getAssociationsLocationURI(userId)).build();
        } catch (URISyntaxException e) {
            return Response.status(Response.Status.CREATED).build();
        }
    }

    private URI getAssociationsLocationURI(String userId) throws URISyntaxException {

        return new URI("/" + userId + "/" + ASSOCIATIONS_PATH);
    }
}
