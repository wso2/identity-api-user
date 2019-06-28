package org.wso2.carbon.identity.rest.api.user.association.v1;

import org.wso2.carbon.identity.rest.api.user.association.v1.factories.UsersApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.rest.api.user.association.v1.dto.UserDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationUserRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationSwitchRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationRequestDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/users")
@Consumes({ "application/json" })
@Produces({ "application/json" })
@io.swagger.annotations.Api(value = "/users", description = "the users API")
public class UsersApi  {

   private final UsersApiService delegate = UsersApiServiceFactory.getUsersApi();

    @DELETE
    @Path("/me/associations/{associate-user-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete a selected user association", notes = "This API is used to delete the selected association of the auhtentiated user.", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response usersMeAssociationsAssociateUserIdDelete(@ApiParam(value = "",required=true ) @PathParam("associate-user-id")  String associateUserId)
    {
    return delegate.usersMeAssociationsAssociateUserIdDelete(associateUserId);
    }
    @DELETE
    @Path("/me/associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete all my user associations", notes = "This API is used to delete all associations of the auhtentiated user.", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response usersMeAssociationsDelete()
    {
    return delegate.usersMeAssociationsDelete();
    }
    @GET
    @Path("/me/associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get authenticated user's associations", notes = "This API is used to retrieve the associations of the authenticated user.", response = UserDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation"),
        
        @io.swagger.annotations.ApiResponse(code = 204, message = "No content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response usersMeAssociationsGet()
    {
    return delegate.usersMeAssociationsGet();
    }
    @POST
    @Path("/me/associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Associate user to the authenticated user\n", notes = "This API is used associate a user to the authenticated user.\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Successfully created"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Conflict"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response usersMeAssociationsPost(@ApiParam(value = "User will be associated to the authenticated user." ,required=true ) AssociationUserRequestDTO association)
    {
    return delegate.usersMeAssociationsPost(association);
    }
    @PUT
    @Path("/me/associations/switch")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Switch user account", notes = "This API is used to switch the user account.", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response usersMeAssociationsSwitchPut(@ApiParam(value = "" ,required=true ) AssociationSwitchRequestDTO switchUserReqeust)
    {
    return delegate.usersMeAssociationsSwitchPut(switchUserReqeust);
    }
    @DELETE
    @Path("/{user-id}/associations/{associate-user-id}}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete user's selected user association", notes = "This API is used to delete a selected association of the  user.", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized request"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response usersUserIdAssociationsAssociateUserIdDelete(@ApiParam(value = "",required=true ) @PathParam("user-id")  String userId,
    @ApiParam(value = "",required=true ) @PathParam("associate-user-id")  String associateUserId)
    {
    return delegate.usersUserIdAssociationsAssociateUserIdDelete(userId,associateUserId);
    }
    @DELETE
    @Path("/{user-id}/associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete user's all user associations", notes = "This API is used to delete all associations of the  user.", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized request"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response usersUserIdAssociationsDelete(@ApiParam(value = "",required=true ) @PathParam("user-id")  String userId)
    {
    return delegate.usersUserIdAssociationsDelete(userId);
    }
    @GET
    @Path("/{user-id}/associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get user's associations", notes = "This API is used to retrieve the associations of the user.", response = UserDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation"),
        
        @io.swagger.annotations.ApiResponse(code = 204, message = "No content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized request"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response usersUserIdAssociationsGet(@ApiParam(value = "user id",required=true ) @PathParam("user-id")  String userId)
    {
    return delegate.usersUserIdAssociationsGet(userId);
    }
    @POST
    @Path("/{user-id}/associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Associate users\n", notes = "This API is used associate two user accounts.\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Successfully created"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Conflict"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response usersUserIdAssociationsPost(@ApiParam(value = "User Id of the associating user." ,required=true ) AssociationRequestDTO association,
    @ApiParam(value = "user id",required=true ) @PathParam("user-id")  String userId)
    {
    return delegate.usersUserIdAssociationsPost(association,userId);
    }
}

