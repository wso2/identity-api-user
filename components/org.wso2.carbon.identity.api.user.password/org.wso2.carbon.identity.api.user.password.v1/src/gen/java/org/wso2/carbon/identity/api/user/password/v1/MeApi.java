/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.user.password.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.user.password.v1.model.Error;
import org.wso2.carbon.identity.api.user.password.v1.model.PasswordChangeRequest;
import org.wso2.carbon.identity.api.user.password.v1.MeApiService;
import org.wso2.carbon.identity.api.user.password.v1.factories.MeApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/me")
@Api(description = "The me API")

public class MeApi  {

    private final MeApiService delegate;

    public MeApi() {

        this.delegate = MeApiServiceFactory.getMeApi();
    }

    @Valid
    @POST
    @Path("/change-password")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Change user password", notes = "This API is used to change the password of the authenticated user.  <b>Permission required:</b>     * none <b>Scope required:</b>     * internal_login ", response = Void.class, authorizations = {
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Password successfully changed", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response changePassword(@ApiParam(value = "Password change request." ,required=true) @Valid PasswordChangeRequest passwordChangeRequest) {

        return delegate.changePassword(passwordChangeRequest );
    }

}
