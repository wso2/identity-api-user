/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.authorized.apps.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.dto.*;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.MeApiService;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.factories.MeApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.dto.AuthorizedAppDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/me")
@io.swagger.annotations.Api(value = "/me", description = "the me API")
public class MeApi  {

    @Autowired
    private MeApiService delegate;

    @Valid
    @DELETE
    @Path("/authorized-apps/{application-id}")
    @io.swagger.annotations.ApiOperation(value = "Removes authorized app by app ID for the authenticated user",
            notes = "Removes authorized OAuth app by an app ID for the authenticated user<br> <b>Permission required:</b> <br> * None <br> <b>Scope required:</b> <br> * internal_login",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "Item Deleted"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource was not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response deleteLoggedInUserAuthorizedAppByAppId(@ApiParam(value = "Application ID",required=true ) @PathParam("application-id")  String applicationId) {

        return delegate.deleteLoggedInUserAuthorizedAppByAppId(applicationId);
    }

    @Valid
    @DELETE
    @Path("/authorized-apps")
    @io.swagger.annotations.ApiOperation(value = "Removes authorized applications for the authenticated user",
            notes = "Removes approved OAuth applications of the authenticated user<br> <b>Permission required:</b> <br> * None <br> <b>Scope required:</b> <br> * internal_login",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "Item Deleted"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response deleteLoggedInUserAuthorizedApps() {

        return delegate.deleteLoggedInUserAuthorizedApps();
    }

    @Valid
    @GET
    @Path("/authorized-apps/{application-id}")
    @io.swagger.annotations.ApiOperation(value = "Retrieve authorized app by app ID for the authenticated user",
            notes = "Retrieves authorized OAuth app by an app ID for the authenticated user<br> <b>Permission required:</b> <br> * None <br> <b>Scope required:</b> <br> * internal_login",
            response = AuthorizedAppDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource was not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response getLoggedInUserAuthorizedAppByAppId(@ApiParam(value = "Application ID",required=true ) @PathParam("application-id")  String applicationId) {

        return delegate.getLoggedInUserAuthorizedAppByAppId(applicationId);
    }

    @Valid
    @GET
    @Path("/authorized-apps")
    @io.swagger.annotations.ApiOperation(value = "List authorized applications for the authenticated user",
            notes = "Lists approved OAuth applications of the authenticated user<br> <b>Permission required:</b> <br> * None <br> <b>Scope required:</b> <br> * internal_login",
            response = AuthorizedAppDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response listLoggedInUserAuthorizedApps() {

        return delegate.listLoggedInUserAuthorizedApps();
    }

}
