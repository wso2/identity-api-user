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

package org.wso2.carbon.identity.rest.api.user.authorized.apps.v1;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.dto.AuthorizedAppDTO;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/{user-id}")


@io.swagger.annotations.Api(value = "/{user-id}", description = "the {user-id} API")
public class UserIdApi  {

   @Autowired
   private UserIdApiService delegate;

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

