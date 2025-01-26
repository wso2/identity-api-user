/*
 * Copyright (c) 2021-2025, WSO2 LLC. (http://www.wso2.com).
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

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.dto.ErrorDTO;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.factories.AuthorizedAppsApiServiceFactory;

import javax.validation.constraints.*;

@Path("/authorized-apps")
@Api(description = "The authorized-apps API")

public class AuthorizedAppsApi  {

    private final AuthorizedAppsApiService delegate;

    public AuthorizedAppsApi() {

        this.delegate = AuthorizedAppsApiServiceFactory.getAuthorizedAppsApi();
    }

    @Valid
    @DELETE
    @Path("/{application-id}/tokens")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Removes all the tokens granted for an application", notes = "Removes all the tokens granted for a given app ID ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "admin" })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Item Deleted", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource was not found", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDTO.class)
    })
    public Response deleteIssuedTokensByAppId(@ApiParam(value = "Application ID",required=true) @PathParam("application-id") String applicationId) {

        return delegate.deleteIssuedTokensByAppId(applicationId );
    }

}
