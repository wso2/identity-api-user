/*
 * Copyright (c) 2020-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.authorized.apps.v2;

import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.dto.AuthorizedAppDTO;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.dto.ErrorDTO;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.factories.UserIdApiServiceFactory;

@Path("/{user-id}")
@Api(description = "The {user-id} API")

public class UserIdApi {

    private final UserIdApiService delegate;

    public UserIdApi() {

        this.delegate = UserIdApiServiceFactory.getUserIdApi();
    }

    @Valid
    @DELETE
    @Path("/authorized-apps")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Remove all authorized applications of a user", notes = "Removes authorization from all OAuth apps of a given user ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "admin", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Item Deleted", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDTO.class)
    })
    public Response deleteUserAuthorizedApps(@ApiParam(value = "username of the user",required=true) @PathParam("user-id") String userId) {

        return delegate.deleteUserAuthorizedApps(userId );
    }

    @Valid
    @DELETE
    @Path("/authorized-apps/{application-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Removes challenge question answers", notes = "Removes autherized OAuth apps by an app ID for a given user ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "admin", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Item Deleted", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource was not found", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDTO.class)
    })
    public Response deleteUserAuthorizedAppsByAppId(@ApiParam(value = "username of the user",required=true) @PathParam("user-id") String userId, @ApiParam(value = "Application ID",required=true) @PathParam("application-id") String applicationId) {

        return delegate.deleteUserAuthorizedAppsByAppId(userId,  applicationId );
    }

    @Valid
    @GET
    @Path("/authorized-apps")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List authorized applications of a user", notes = "List approved OAuth applications for a given user ", response = AuthorizedAppDTO.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "admin", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "search results matching criteria", response = AuthorizedAppDTO.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDTO.class)
    })
    public Response listUserAuthorizedApps(@ApiParam(value = "username of the user",required=true) @PathParam("user-id") String userId) {

        return delegate.listUserAuthorizedApps(userId );
    }

    @Valid
    @GET
    @Path("/authorized-apps/{application-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve authorized app by app ID for a user", notes = "Retrived autherized OAuth apps by an app ID for a given user ", response = AuthorizedAppDTO.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "admin" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "search results matching criteria", response = AuthorizedAppDTO.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource was not found", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDTO.class)
    })
    public Response listUserAuthorizedAppsByAppId(@ApiParam(value = "username of the user",required=true) @PathParam("user-id") String userId, @ApiParam(value = "Application ID",required=true) @PathParam("application-id") String applicationId) {

        return delegate.listUserAuthorizedAppsByAppId(userId,  applicationId );
    }

}
