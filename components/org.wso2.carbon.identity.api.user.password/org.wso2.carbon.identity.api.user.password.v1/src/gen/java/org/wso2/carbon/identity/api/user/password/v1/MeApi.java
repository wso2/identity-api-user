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


import org.wso2.carbon.identity.api.user.password.v1.model.ErrorDTO;
import org.wso2.carbon.identity.api.user.password.v1.model.PasswordUpdateRequest;
import org.wso2.carbon.identity.api.user.password.v1.model.PasswordUpdateResponse;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;
import org.wso2.carbon.identity.api.user.password.v1.factories.MeApiServiceFactory;

@Path("/me")
@Api(description = "The me API")

public class MeApi  {

    private final MeApiService delegate;

    public MeApi() {
        this.delegate = MeApiServiceFactory.getMeApi();
    }

    @Valid
    @PUT
    @Path("/password")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update user password", notes = "This API is used to update the password of the authenticated user.  <b>Permission required:</b>     * none <b>Scope required:</b>     * internal_login ", response = PasswordUpdateResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Password successfully updated", response = PasswordUpdateResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDTO.class)
    })
    public Response updatePassword(@ApiParam(value = "Password update request" ,required=true) @Valid PasswordUpdateRequest passwordUpdateRequest) {

        return this.delegate.updatePassword(passwordUpdateRequest );
    }

}
