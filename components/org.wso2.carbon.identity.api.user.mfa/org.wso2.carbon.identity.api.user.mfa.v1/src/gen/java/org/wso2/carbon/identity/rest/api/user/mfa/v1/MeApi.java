/*
 * Copyright (c) 2022-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.mfa.v1;

import org.wso2.carbon.identity.rest.api.user.mfa.v1.dto.*;
import org.wso2.carbon.identity.rest.api.user.mfa.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.mfa.v1.factories.MeApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.rest.api.user.mfa.v1.dto.EnabledAuthenticatorsDTO;
import org.wso2.carbon.identity.rest.api.user.mfa.v1.dto.ErrorDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/me")
@Consumes({ "application/json" })
@Produces({ "application/json" })
@io.swagger.annotations.Api(value = "/me", description = "the me API")
public class MeApi  {

    private final MeApiService delegate;

    public MeApi() {

        this.delegate = MeApiServiceFactory.getMeApi();
    }

    @Valid
    @GET
    @Path("/mfa/authenticators")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve enabled authenticators of the authenticated user.",
            notes = "This API is used to retrieve enabled authenticators of the authenticated user.\n\n\n<b>Permission required:</b>\n    * none\n<b>Scope required:</b>\n    * internal_login\n",
            response = EnabledAuthenticatorsDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response meMfaAuthenticatorsGet() {

        return delegate.meMfaAuthenticatorsGet();
    }

    @Valid
    @POST
    @Path("/mfa/authenticators")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update Enabled Authenticators of the user.\n",
            notes = "This API is used to perform following actions.\n* Update Enabled Authenticators of the user.\n\n<b>Permission required:</b>\n    * none\n<b>Scope required:</b>\n    * internal_login\n",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Enabled authenticators successfully updated"),
        
        @io.swagger.annotations.ApiResponse(code = 201, message = "Created"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response meMfaAuthenticatorsPost(@ApiParam(value = "Enable authenticators" ,required=true ) @Valid EnabledAuthenticatorsDTO request) {

        return delegate.meMfaAuthenticatorsPost(request);
    }

}
