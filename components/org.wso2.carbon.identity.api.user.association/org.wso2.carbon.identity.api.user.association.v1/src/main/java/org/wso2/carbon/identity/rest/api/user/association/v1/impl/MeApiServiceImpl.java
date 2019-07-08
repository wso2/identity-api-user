package org.wso2.carbon.identity.rest.api.user.association.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.rest.api.user.association.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.association.v1.core.UserAssociationService;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationSwitchRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationUserRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.UserDTO;
import org.wso2.carbon.user.core.util.UserCoreUtil;

import java.net.URI;
import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.ContextLoader.buildURI;
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

        userAssociationService.deleteUserAccountAssociation(getUserId());
        return Response.ok().build();
    }

    @Override
    public Response meAssociationsGet() {

        List<UserDTO> userDTOList = userAssociationService.getAssociationsOfUser(getUserId());
        if (userDTOList.size() == 0) {
            return Response.noContent().build();
        }
        return Response.ok().entity(userDTOList).build();
    }

    @Override
    public Response meAssociationsPost(AssociationUserRequestDTO association) {

        userAssociationService.createUserAccountAssociation(association);
        return Response.created(getAssociationsLocationURI()).build();
    }

    @Override
    public Response meAssociationsSwitchPut(AssociationSwitchRequestDTO switchUserReqeust) {

        boolean isSwitched = userAssociationService.switchLoggedInUser(getUserId());
        if (isSwitched) {
            return Response.ok().build();
        } else {
            //TODO need to check this.
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    private String getUserId() {

        String username = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
        return UserCoreUtil.addTenantDomainToEntry(username, PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getTenantDomain());
    }

    private URI getAssociationsLocationURI() {

        return buildURI(String.format(V1_API_PATH_COMPONENT + USER_ASSOCIATIONS_PATH_COMPONENT, ME_CONTEXT));
    }
}
