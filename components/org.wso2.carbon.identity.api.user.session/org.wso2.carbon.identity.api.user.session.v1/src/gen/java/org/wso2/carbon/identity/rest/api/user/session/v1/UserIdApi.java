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

package org.wso2.carbon.identity.rest.api.user.session.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.*;
import org.wso2.carbon.identity.rest.api.user.session.v1.UserIdApiService;
import org.wso2.carbon.identity.rest.api.user.session.v1.factories.UserIdApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionsDTO;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.ErrorDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/{user-id}")
@io.swagger.annotations.Api(value = "/{user-id}", description = "the {user-id} API")
public class UserIdApi  {

    @Autowired
    private UserIdApiService delegate;

    @Valid
    @GET
    @Path("/sessions")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get active sessions",
            notes = "Retrieves information related to the active sessions of a user identified by the user-id. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/authentication/session/view <br> <b>Scope required:</b> <br> * internal_session_view",
            response = SessionsDTO.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully retrieved session information."),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),

            @io.swagger.annotations.ApiResponse(code = 404, message = "Resource Not Found"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response getSessionsByUserId(@ApiParam(value = "ID of the user.",required=true ) @PathParam("user-id")  String userId,
                                        @ApiParam(value = "Maximum number of records to return.\n_This parameter is not supported yet._\n") @QueryParam("limit")  Integer limit,
                                        @ApiParam(value = "Number of records to skip for pagination.\n_This parameter is not supported yet._\n") @QueryParam("offset")  Integer offset,
                                        @ApiParam(value = "Condition to filter the retrival of records.\n_This parameter is not supported yet._\n") @QueryParam("filter")  String filter,
                                        @ApiParam(value = "Define the order in which the retrieved records should be sorted.\n_This parameter is not supported yet._\n") @QueryParam("sort")  String sort) {

        return delegate.getSessionsByUserId(userId,limit,offset,filter,sort);
    }

    @Valid
    @DELETE
    @Path("/sessions/{session-id}")
    @io.swagger.annotations.ApiOperation(value = "Terminate a session",
            notes = "Terminate a specific session of a user by the session-id. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/authentication/session/delete <br> <b>Scope required:</b> <br> * internal_session_delete",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 204, message = "No Content"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response terminateSessionBySessionId(@ApiParam(value = "ID of the user.",required=true ) @PathParam("user-id")  String userId,
                                                @ApiParam(value = "ID of the session.",required=true ) @PathParam("session-id")  String sessionId) {

        return delegate.terminateSessionBySessionId(userId,sessionId);
    }

    @Valid
    @DELETE
    @Path("/sessions")
    @io.swagger.annotations.ApiOperation(value = "Terminate all sessions",
            notes = "Delete all the sessions of a user identified by the user-id. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/authentication/session/delete <br> <b>Scope required:</b> <br> * internal_session_delete",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 204, message = "No Content"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response terminateSessionsByUserId(@ApiParam(value = "ID of the user.",required=true ) @PathParam("user-id")  String userId) {

        return delegate.terminateSessionsByUserId(userId);
    }

}
