/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.user.organization.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.user.organization.v1.model.BasicOrganizationObject;
import org.wso2.carbon.identity.api.user.organization.v1.model.Error;
import org.wso2.carbon.identity.api.user.organization.v1.model.RootOrganizationResponse;
import org.wso2.carbon.identity.api.user.organization.v1.OrganizationsApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/organizations")
@Api(description = "The organizations API")

public class OrganizationsApi  {

    @Autowired
    private OrganizationsApiService delegate;

    @Valid
    @GET
    @Path("/root/descendants")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get the descendant organizations of the authenticated user's resident organization ", notes = "This API provides the capability to retrieve the descendant organizations of the authenticated user's resident organizations. The response includes  the organization's id and name from the resident organization to the accessed child organization.  <b>Permission required:</b> <br/>   - none  <b>Scope required:</b> <br/>   - internal_login ", response = BasicOrganizationObject.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "me", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = BasicOrganizationObject.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response organizationsRootDescendantsGet() {

        return delegate.organizationsRootDescendantsGet();
    }

    @Valid
    @GET
    @Path("/root")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get the root organization of the authenticated user ", notes = "This API provides the capability to retrieve the root organization of the authenticated user.  <b>Permission required:</b> <br/>   - none  <b>Scope required:</b> <br/>   - internal_login ", response = RootOrganizationResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = RootOrganizationResponse.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response organizationsRootGet() {

        return delegate.organizationsRootGet();
    }

}
