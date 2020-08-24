/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.rest.api.user.functionality.v1;

import org.springframework.beans.factory.annotation.Autowired;

import org.wso2.carbon.identity.rest.api.user.functionality.v1.model.Error;
import org.wso2.carbon.identity.rest.api.user.functionality.v1.model.LockStatusResponse;
import org.wso2.carbon.identity.rest.api.user.functionality.v1.model.StatusChangeRequest;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

@Path("/{user-id}")
@Api(description = "The {user-id} API")

public class UserIdApi {

    @Autowired
    private UserIdApiService delegate;

    @Valid
    @PUT
    @Path("/user-functionality/{function-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "application/xml" })
    @ApiOperation(value = "Lock a function.", notes = "This API is used to lock a function for a user. Hence the user cannot access that feature.", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "user functionality management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "The feature is successfully locked.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response changeStatus(@ApiParam(value = "FunctionalityIdentifier.",required=true) @PathParam("function-id") String functionId, @ApiParam(value = "UserID.",required=true) @PathParam("user-id") String userId, @ApiParam(value = "" ) @Valid StatusChangeRequest statusChangeRequest) {

        return delegate.changeStatus(functionId,  userId,  statusChangeRequest );
    }

    @Valid
    @GET
    @Path("/user-functionality/{function-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get the lock status of a functionality of a user ", notes = "Get the status of a functionality of a user", response = LockStatusResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "user functionality management" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = LockStatusResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getLockStatus(@ApiParam(value = "FunctionalityIdentifier.",required=true) @PathParam("function-id") String functionId, @ApiParam(value = "UserID.",required=true) @PathParam("user-id") String userId) {

        return delegate.getLockStatus(functionId,  userId );
    }
}
