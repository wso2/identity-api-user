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
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionDTO;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionsDTO;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/{user-id}")
@Api(value = "/{user-id}", description = "the {user-id} API")
public class UserIdApi {

    @Autowired
    private UserIdApiService delegate;

    @Valid
    @GET
    @Path("/sessions/{session-id}")
    @Produces({"application/json"})
    @ApiOperation(value = "Retrieve a given active session of a given user",
            notes = "Retrieves information related to the active session identified by session-id of a user identified by the user-id. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/authentication/session/view <br> <b>Scope required:</b> <br> * internal_session_view",
            response = SessionDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved session information"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Resource Forbidden"),
            @ApiResponse(code = 404, message = "Resource Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    public Response getSessionBySessionId(@ApiParam(value = "ID of the user.", required = true) @PathParam("user-id") String userId,
                                          @ApiParam(value = "ID of the session.", required = true) @PathParam("session-id") String sessionId) {

        return delegate.getSessionBySessionId(userId, sessionId);
    }

    @Valid
    @GET
    @Path("/sessions")
    @Produces({"application/json"})
    @ApiOperation(value = "Retrieve active sessions of a given user",
            notes = "Retrieves information related to the active sessions of a user identified by the user-id. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/authentication/session/view <br> <b>Scope required:</b> <br> * internal_session_view",
            response = SessionsDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved session information."),
            @ApiResponse(code = 400, message = "Invalid input request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Resource Forbidden"),
            @ApiResponse(code = 404, message = "Resource Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    public Response getSessionsByUserId(@ApiParam(value = "ID of the user.", required = true) @PathParam("user-id") String userId,
                                        @ApiParam(value = "Maximum number of records to return.\n_This parameter is not supported yet._\n") @QueryParam("limit") Integer limit,
                                        @ApiParam(value = "Number of records to skip for pagination.\n_This parameter is not supported yet._\n") @QueryParam("offset") Integer offset,
                                        @ApiParam(value = "Condition to filter the retrieval of records.\n_This parameter is not supported yet._\n") @QueryParam("filter") String filter,
                                        @ApiParam(value = "Define the order in which the retrieved records should be sorted.\n_This parameter is not supported yet._\n") @QueryParam("sort") String sort) {

        return delegate.getSessionsByUserId(userId, limit, offset, filter, sort);
    }

    @Valid
    @DELETE
    @Path("/sessions/{session-id}")
    @ApiOperation(value = "Terminate a given session of a given user",
            notes = "Terminate a specific session of a user by the session-id. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/authentication/session/delete <br> <b>Scope required:</b> <br> * internal_session_delete",
            response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Invalid input request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    public Response terminateSessionBySessionId(@ApiParam(value = "ID of the user.", required = true) @PathParam("user-id") String userId,
                                                @ApiParam(value = "ID of the session.", required = true) @PathParam("session-id") String sessionId) {

        return delegate.terminateSessionBySessionId(userId, sessionId);
    }

    @Valid
    @DELETE
    @Path("/sessions")
    @ApiOperation(value = "Terminate all sessions of a given user",
            notes = "Delete all the sessions of a user identified by the user-id. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/authentication/session/delete <br> <b>Scope required:</b> <br> * internal_session_delete",
            response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Invalid input request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    public Response terminateSessionsByUserId(@ApiParam(value = "ID of the user.", required = true) @PathParam("user-id") String userId) {

        return delegate.terminateSessionsByUserId(userId);
    }

}
