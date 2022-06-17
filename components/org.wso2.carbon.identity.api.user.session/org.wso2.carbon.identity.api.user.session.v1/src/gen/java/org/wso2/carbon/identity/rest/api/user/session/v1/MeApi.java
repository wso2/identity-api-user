/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.session.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionsDTO;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/me")
@Api(value = "/me", description = "the me API")
public class MeApi {

    @Autowired
    private MeApiService delegate;

    @Valid
    @GET
    @Path("/sessions")
    @Produces({"application/json"})
    @ApiOperation(value = "Retrieve active sessions of the authenticated user",
            notes = "A user can have multiple active sessions. This API retrieves information of all the active sessions of the authenticated user. <br> <b>Permission required:</b> <br> * None <br> <b>Scope required:</b> <br> * internal_login\n",
            response = SessionsDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved session information"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Resource Forbidden"),
            @ApiResponse(code = 404, message = "Resource Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    public Response getSessionsOfLoggedInUser(@ApiParam(value = "Maximum number of records to return.\n_This parameter is not supported yet._\n") @QueryParam("limit") Integer limit,
                                              @ApiParam(value = "Number of records to skip for pagination.\n_This parameter is not supported yet._\n") @QueryParam("offset") Integer offset,
                                              @ApiParam(value = "Condition to filter the retrieval of records.\n_This parameter is not supported yet._\n") @QueryParam("filter") String filter,
                                              @ApiParam(value = "Define the order in which the retrieved records should be sorted.\n_This parameter is not supported yet._\n") @QueryParam("sort") String sort) {

        return delegate.getSessionsOfLoggedInUser(limit, offset, filter, sort);
    }

    @Valid
    @DELETE
    @Path("/sessions/{session-id}")
    @ApiOperation(value = "Terminate a given session of the authenticated user",
            notes = "A user has multiple active sessions. This API terminates a specific session of the authenticated user identified by the session-id. <br> <b>Permission required:</b> <br> * None <br> <b>Scope required:</b> <br> * internal_login\n",
            response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Invalid input request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Resource Forbidden"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    public Response terminateSessionByLoggedInUser(@ApiParam(value = "ID of the session.", required = true) @PathParam("session-id") String sessionId) {

        return delegate.terminateSessionByLoggedInUser(sessionId);
    }

    @Valid
    @DELETE
    @Path("/sessions")
    @ApiOperation(value = "Terminate all the active sessions of the authenticated user",
            notes = "A user has multiple active sessions. This API terminates all the active sessions of the authenticated user. <br> <b>Permission required:</b> <br> * None <br> <b>Scope required:</b> <br> * internal_login\n",
            response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Invalid input request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Resource Forbidden"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    public Response terminateSessionsByLoggedInUser() {

        return delegate.terminateSessionsByLoggedInUser();
    }

}
