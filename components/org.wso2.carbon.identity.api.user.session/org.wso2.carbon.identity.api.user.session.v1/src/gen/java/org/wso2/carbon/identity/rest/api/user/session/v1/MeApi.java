/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.rest.api.user.session.v1;

import org.springframework.beans.factory.annotation.Autowired;
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

   @Autowired
   private MeApiService delegate;

    @GET
    @Path("/sessions")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve active sessions of the authenticated user", notes = "A user can have multiple active sessions. This API retrieves information of all the active sessions of the authenticated user.\n<b>Permission required:</b>\n * /permission/admin/login\n", response = SessionsDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully retrieved session information"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Resource Not Found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response getSessionsOfLoggedInUser(@ApiParam(value = "Maximum number of records to return.\n_This parameter is not supported yet._\n") @QueryParam("limit")  Integer limit,
    @ApiParam(value = "Number of records to skip for pagination.\n_This parameter is not supported yet._\n") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "Condition to filter the retrival of records.\n_This parameter is not supported yet._\n") @QueryParam("filter")  String filter,
    @ApiParam(value = "Define the order in which the retrieved records should be sorted.\n_This parameter is not supported yet._\n") @QueryParam("sort")  String sort)
    {
    return delegate.getSessionsOfLoggedInUser(limit,offset,filter,sort);
    }
    @DELETE
    @Path("/sessions/{session-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "Terminate a given session of the authenticated user", notes = "A user has multiple active sessions. This API terminates a specific session of the authenticated user identified by the session-id.\n<b>Permission required:</b>\n * /permission/admin/login\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No Content"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response terminateSessionByLoggedInUser(@ApiParam(value = "ID of the session.",required=true ) @PathParam("session-id")  String sessionId)
    {
    return delegate.terminateSessionByLoggedInUser(sessionId);
    }
    @DELETE
    @Path("/sessions")
    
    
    @io.swagger.annotations.ApiOperation(value = "Terminate all the active sessions of the authenticated user", notes = "A user has multiple active sessions. This API terminates all the active sessions of the authenticated user.\n<b>Permission required:</b>\n * /permission/admin/login\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No Content"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response terminateSessionsByLoggedInUser()
    {
    return delegate.terminateSessionsByLoggedInUser();
    }
}

