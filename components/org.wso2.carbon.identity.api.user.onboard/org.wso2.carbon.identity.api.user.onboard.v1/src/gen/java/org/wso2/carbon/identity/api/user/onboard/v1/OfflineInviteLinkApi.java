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

package org.wso2.carbon.identity.api.user.onboard.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

import org.wso2.carbon.identity.api.user.onboard.v1.factories.OfflineInviteLinkApiServiceFactory;
import org.wso2.carbon.identity.api.user.onboard.v1.model.Error;
import org.wso2.carbon.identity.api.user.onboard.v1.model.InvitationRequest;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * This API is used to generate a random link that can be used to set a new password.
 */
@Path("/offline-invite-link")
@Api(description = "The offline-invite-link API")

public class OfflineInviteLinkApi  {

    private final OfflineInviteLinkApiService delegate;

    public OfflineInviteLinkApi() {

        this.delegate = OfflineInviteLinkApiServiceFactory.getOfflineInviteLinkApi();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "text/plain", "application/json" })
    @ApiOperation(value = "Generates a random link that can be used to set a new password", notes = "Generates a " +
            "random and secured one time link that can be used to set a new password for specified user",
            response = String.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags = { "Temporary Link" })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created", response = String.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response generateLink(@ApiParam(value = "This represents the invitation request." , required = true)
                                     @Valid InvitationRequest invitationRequest) {

        return delegate.generateLink (invitationRequest);
    }

}
