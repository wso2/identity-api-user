package org.wso2.carbon.identity.rest.api.user.association.v1;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.UserDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/{user-id}")
@Consumes({ "application/json" })
@Produces({ "application/json" })
@io.swagger.annotations.Api(value = "/{user-id}", description = "the {user-id} API")
public class UserIdApi  {

 @Autowired
 private UserIdApiService delegate;

    @DELETE
    @Path("/associations/{associate-user-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete user's selected user association", notes = "This API is used to delete a selected association of the  user.", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized request"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response userIdAssociationsAssociateUserIdDelete(@ApiParam(value = "",required=true ) @PathParam("user-id")  String userId,
    @ApiParam(value = "",required=true ) @PathParam("associate-user-id")  String associateUserId)
    {
    return delegate.userIdAssociationsAssociateUserIdDelete(userId,associateUserId);
    }
    @DELETE
    @Path("/associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete user's all user associations", notes = "This API is used to delete all associations of the  user.", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized request"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response userIdAssociationsDelete(@ApiParam(value = "",required=true ) @PathParam("user-id")  String userId)
    {
    return delegate.userIdAssociationsDelete(userId);
    }
    @GET
    @Path("/associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get user's associations", notes = "This API is used to retrieve the associations of the user.", response = UserDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation"),
        
        @io.swagger.annotations.ApiResponse(code = 204, message = "No content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized request"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response userIdAssociationsGet(@ApiParam(value = "user id",required=true ) @PathParam("user-id")  String userId)
    {
    return delegate.userIdAssociationsGet(userId);
    }
    @POST
    @Path("/associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Associate users\n", notes = "This API is used associate two user accounts.\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Successfully created"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Conflict"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response userIdAssociationsPost(@ApiParam(value = "User Id of the associating user." ,required=true ) AssociationRequestDTO association,
    @ApiParam(value = "user id",required=true ) @PathParam("user-id")  String userId)
    {
    return delegate.userIdAssociationsPost(association,userId);
    }
}

