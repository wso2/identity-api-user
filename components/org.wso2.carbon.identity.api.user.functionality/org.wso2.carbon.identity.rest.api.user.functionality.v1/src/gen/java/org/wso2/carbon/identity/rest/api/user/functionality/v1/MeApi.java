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

package org.wso2.carbon.identity.rest.api.user.functionality.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.rest.api.user.functionality.v1.model.Error;
import org.wso2.carbon.identity.rest.api.user.functionality.v1.model.LockStatusResponse;
import org.wso2.carbon.identity.rest.api.user.functionality.v1.model.UserStatusChangeRequest;
import org.wso2.carbon.identity.rest.api.user.functionality.v1.MeApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/me")
@Api(description = "The me API")

public class MeApi  {

    @Autowired
    private MeApiService delegate;

    @Valid
    @PUT
    @Path("/user-functionality/{function-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "application/xml" })
    @ApiOperation(value = "Lock or Unlock a function of a user.", notes = "This API is used to lock or unlock a function for a user.", response = Void.class, authorizations = {
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
    public Response changeStatusOfLoggedInUser(@ApiParam(value = "FunctionalityIdentifier.",required=true) @PathParam("function-id") String functionId, @ApiParam(value = "" ) @Valid UserStatusChangeRequest userStatusChangeRequest) {

        return delegate.changeStatusOfLoggedInUser(functionId,  userStatusChangeRequest );
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
    public Response getLockStatusOfLoggedInUser(@ApiParam(value = "FunctionalityIdentifier.",required=true) @PathParam("function-id") String functionId) {

        return delegate.getLockStatusOfLoggedInUser(functionId );
    }

}
