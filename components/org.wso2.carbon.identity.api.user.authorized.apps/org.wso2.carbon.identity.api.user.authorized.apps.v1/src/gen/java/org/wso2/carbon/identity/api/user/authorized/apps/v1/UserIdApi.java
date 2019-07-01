package org.wso2.carbon.identity.api.user.authorized.apps.v1;

import org.wso2.carbon.identity.api.user.authorized.apps.v1.dto.*;
import org.wso2.carbon.identity.api.user.authorized.apps.v1.UserIdApiService;
import org.wso2.carbon.identity.api.user.authorized.apps.v1.factories.UserIdApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.api.user.authorized.apps.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.api.user.authorized.apps.v1.dto.AuthorizedAppDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/{user-id}")


@io.swagger.annotations.Api(value = "/{user-id}", description = "the {user-id} API")
public class UserIdApi  {

   private final UserIdApiService delegate = UserIdApiServiceFactory.getUserIdApi();

    @DELETE
    @Path("/authorized-apps")
    
    
    @io.swagger.annotations.ApiOperation(value = "remove all authorized applications of a user", notes = "Removes authorization from all OAuth apps of a given user\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Item Deleted"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response deleteUserAuthorizedApps(@ApiParam(value = "username of the user",required=true ) @PathParam("user-id")  String userId)
    {
    return delegate.deleteUserAuthorizedApps(userId);
    }
    @DELETE
    @Path("/authorized-apps/{application-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "removes challenge question answers", notes = "Removes autherized OAuth apps by an app ID for a given user\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Item Deleted"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource was not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response deleteUserAuthorizedAppsByAppId(@ApiParam(value = "username of the user",required=true ) @PathParam("user-id")  String userId,
    @ApiParam(value = "Application ID",required=true ) @PathParam("application-id")  String applicationId)
    {
    return delegate.deleteUserAuthorizedAppsByAppId(userId,applicationId);
    }
    @GET
    @Path("/authorized-apps")
    
    
    @io.swagger.annotations.ApiOperation(value = "list authorized applications of a user", notes = "List approved OAuth applications for a given user\n", response = AuthorizedAppDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response listUserAuthorizedApps(@ApiParam(value = "username of the user",required=true ) @PathParam("user-id")  String userId)
    {
    return delegate.listUserAuthorizedApps(userId);
    }
    @GET
    @Path("/authorized-apps/{application-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "retrieve autherized app by app ID for a user", notes = "Retrived autherized OAuth apps by an app ID for a given user\n", response = AuthorizedAppDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource was not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response listUserAuthorizedAppsByAppId(@ApiParam(value = "username of the user",required=true ) @PathParam("user-id")  String userId,
    @ApiParam(value = "Application ID",required=true ) @PathParam("application-id")  String applicationId)
    {
    return delegate.listUserAuthorizedAppsByAppId(userId,applicationId);
    }
}

