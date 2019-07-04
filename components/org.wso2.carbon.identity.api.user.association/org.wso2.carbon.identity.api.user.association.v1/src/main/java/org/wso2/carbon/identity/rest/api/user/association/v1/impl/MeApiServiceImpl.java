package org.wso2.carbon.identity.rest.api.user.association.v1.impl;

import org.wso2.carbon.identity.rest.api.user.association.v1.*;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.*;


import org.wso2.carbon.identity.rest.api.user.association.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.UserDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationUserRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationSwitchRequestDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import javax.ws.rs.core.Response;

public class MeApiServiceImpl extends MeApiService {
    @Override
    public Response meAssociationsAssociateUserIdDelete(String associateUserId){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response meAssociationsDelete(){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response meAssociationsGet(){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response meAssociationsPost(AssociationUserRequestDTO association){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response meAssociationsSwitchPut(AssociationSwitchRequestDTO switchUserReqeust){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
