/*
 * Copyright (c) 2019-2024, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.rest.api.user.authorized.apps.v1;

import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.dto.*;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.factories.MeApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.dto.AuthorizedAppDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/me")


@io.swagger.annotations.Api(value = "/me", description = "the me API")
public class MeApi  {

   private final MeApiService delegate;

   public MeApi() {

        this.delegate = MeApiServiceFactory.getMeApi();
   }

    @DELETE
    @Path("/authorized-apps/{application-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "removes authorized app by app ID for the authenticated user", notes = "Removes autherized OAuth app by an app ID for for the authenticated user\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "Item Deleted"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource was not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response deleteLoggedInUserAuthorizedAppByAppId(@ApiParam(value = "Application ID",required=true ) @PathParam("application-id")  String applicationId)
    {
    return delegate.deleteLoggedInUserAuthorizedAppByAppId(applicationId);
    }
    @DELETE
    @Path("/authorized-apps")
    
    
    @io.swagger.annotations.ApiOperation(value = "removes authorized applications for the authenticated user", notes = "Removes approved OAuth applications of the authenticated user\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "Item Deleted"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response deleteLoggedInUserAuthorizedApps()
    {
    return delegate.deleteLoggedInUserAuthorizedApps();
    }
    @GET
    @Path("/authorized-apps/{application-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "retrieve authorized app by app ID for the authenticated user", notes = "Retrived autherized OAuth app by an app ID for for the authenticated user\n", response = AuthorizedAppDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource was not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response getLoggedInUserAuthorizedAppByAppId(@ApiParam(value = "Application ID",required=true ) @PathParam("application-id")  String applicationId)
    {
    return delegate.getLoggedInUserAuthorizedAppByAppId(applicationId);
    }
    @GET
    @Path("/authorized-apps")
    
    
    @io.swagger.annotations.ApiOperation(value = "list authorized applications for the authenticated user", notes = "List approved OAuth applications of the authenticated user\n", response = AuthorizedAppDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response listLoggedInUserAuthorizedApps()
    {
    return delegate.listLoggedInUserAuthorizedApps();
    }
}

