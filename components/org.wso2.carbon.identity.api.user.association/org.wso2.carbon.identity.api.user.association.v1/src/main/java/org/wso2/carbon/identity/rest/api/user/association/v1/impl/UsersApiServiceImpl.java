package org.wso2.carbon.identity.rest.api.user.association.v1.impl;

import org.wso2.carbon.identity.rest.api.user.association.v1.*;


import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationUserRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationSwitchRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationRequestDTO;

import javax.ws.rs.core.Response;

public class UsersApiServiceImpl extends UsersApiService {
    @Override
    public Response usersMeAssociationsAssociateUserIdDelete(String associateUserId){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response usersMeAssociationsDelete(){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response usersMeAssociationsGet(){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response usersMeAssociationsPost(AssociationUserRequestDTO association){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response usersMeAssociationsSwitchPut(AssociationSwitchRequestDTO switchUserReqeust){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response usersUserIdAssociationsAssociateUserIdDelete(String userId,String associateUserId){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response usersUserIdAssociationsDelete(String userId){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response usersUserIdAssociationsGet(String userId){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response usersUserIdAssociationsPost(AssociationRequestDTO association,String userId){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
