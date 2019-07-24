package org.wso2.carbon.identity.rest.api.user.session.v1;

import org.wso2.carbon.identity.rest.api.user.session.v1.dto.*;
import org.wso2.carbon.identity.rest.api.user.session.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.session.v1.factories.MeApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionsDTO;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.ErrorDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/me")


@io.swagger.annotations.Api(value = "/me", description = "the me API")
public class MeApi  {

   private final MeApiService delegate = MeApiServiceFactory.getMeApi();

    @GET
    @Path("/sessions")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "get active sessions", notes = "Retrieve information related to the active sessions of the logged in user.", response = SessionsDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully retrieved session information"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response getSessionsOfLoggedInUser(@ApiParam(value = "maximum number of records to return") @QueryParam("limit")  Integer limit,
    @ApiParam(value = "number of records to skip for pagination") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "Condition to filter the retrival of records.") @QueryParam("filter")  String filter,
    @ApiParam(value = "Define the order how the retrieved records should be sorted.") @QueryParam("sort")  String sort)
    {
    return delegate.getSessionsOfLoggedInUser(limit,offset,filter,sort);
    }
    @DELETE
    @Path("/sessions/{session-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "Terminate a session", notes = "Terminate a given session of the logged in user", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No Content."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response terminateSessionByLoggedInUser(@ApiParam(value = "id of the session",required=true ) @PathParam("session-id")  String sessionId)
    {
    return delegate.terminateSessionByLoggedInUser(sessionId);
    }
    @DELETE
    @Path("/sessions")
    
    
    @io.swagger.annotations.ApiOperation(value = "Terminate all sessions", notes = "Delete all the sessions of the logged in user", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No Content."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response terminateSessionsByLoggedInUser()
    {
    return delegate.terminateSessionsByLoggedInUser();
    }
}

