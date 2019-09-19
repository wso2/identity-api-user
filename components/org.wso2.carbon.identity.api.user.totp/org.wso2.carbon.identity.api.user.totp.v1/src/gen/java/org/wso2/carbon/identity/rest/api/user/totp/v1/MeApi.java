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

package org.wso2.carbon.identity.rest.api.user.totp.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.totp.v1.dto.*;
import org.wso2.carbon.identity.rest.api.user.totp.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.totp.v1.factories.MeApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.rest.api.user.totp.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.user.totp.v1.dto.TOTPResponseDTO;
import org.wso2.carbon.identity.rest.api.user.totp.v1.dto.UserRequestDTO;
import org.wso2.carbon.identity.rest.api.user.totp.v1.dto.TOTPSecretResponseDTO;

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

   @Autowired
   private MeApiService delegate;

    @Valid
    @DELETE
    @Path("/totp")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Resets TOTP credentials of the authenticated user.", notes = "This API is used to delete TOTP credentials of the authenticated user.\n\n  <b>Permission required:</b>\n\n  * /permission/admin/login\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Credentials deleted successfully"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response meTotpDelete()
    {
    return delegate.meTotpDelete();
    }
    @Valid
    @GET
    @Path("/totp")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve the QR Code URL of the authenticated user.", notes = "This API is used to retrieve the QR code URL of the authenticated user.\n\n  <b>Permission required:</b>\n\n  * /permission/admin/login\n", response = TOTPResponseDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response meTotpGet()
    {
    return delegate.meTotpGet();
    }
    @Valid
    @POST
    @Path("/totp")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Refresh, validate the QR code URL\n", notes = "This API is used to perform following actions.\n* <b>INIT</b>- Generate TOTP QR code URL for the authenticated user\n* <b>REFRESH</b> - Refresh TOTP secret key of the authenticated user\n* <b>VALIDATE</b> - Validate the user entered verification code\n\n <b>Permission required:</b>\n * /permission/admin/login\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "TOTP secret successfully refreshed"),
        
        @io.swagger.annotations.ApiResponse(code = 201, message = "Created"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Conflict"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response meTotpPost(@ApiParam(value = "Actions supported by the API. Actions can be INIT, VALIDATE or REFRESH" ,required=true ) @Valid UserRequestDTO request)
    {
    return delegate.meTotpPost(request);
    }
    @Valid
    @GET
    @Path("/totp/secret")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve the TOTP secret of the authenticated user.", notes = "This API is used to retrieve the TOTP secret of the authenticated user.\n\n  <b>Permission required:</b>\n\n  * /permission/admin/login\n", response = TOTPSecretResponseDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response meTotpSecretGet()
    {
    return delegate.meTotpSecretGet();
    }
}

