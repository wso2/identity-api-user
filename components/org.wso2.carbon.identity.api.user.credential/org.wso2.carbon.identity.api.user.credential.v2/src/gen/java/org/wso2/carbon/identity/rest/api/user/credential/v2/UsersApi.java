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

package org.wso2.carbon.identity.rest.api.user.credential.v2;

import org.wso2.carbon.identity.rest.api.user.credential.v2.factories.UsersApiServiceFactory;

import io.swagger.annotations.ApiParam;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/{user-id}")
@Consumes({ "application/json" })
@Produces({ "application/json" })
@io.swagger.annotations.Api(value = "/{user-id}", description = "the users credential API v2")
public class UsersApi {

    private final UsersApiService delegate;

    public UsersApi() {

        this.delegate = UsersApiServiceFactory.getUsersApi();
    }

    @javax.validation.Valid
    @POST
    @Path("/credentials/{type}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Generate credentials of a specified type for a user.",
            notes = "Generates credentials of the specified type for a user and returns the generated values. "
                    + "Calling this endpoint replaces any existing credentials of the same type. Requires "
                    + "administrative privileges with scope internal_user_credential_mgt_create.",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 201, message = "Credentials Created."),
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request."),
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden."),
        @io.swagger.annotations.ApiResponse(code = 404, message = "User Not Found."),
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.")
    })
    public Response createUserCredentialByType(
            @ApiParam(value = "The unique identifier of the user.", required = true)
            @PathParam("user-id") String userId,
            @ApiParam(value = "The type of the credential to generate.", required = true,
                    allowableValues = "backup-code")
            @PathParam("type") String type) {

        return delegate.createUserCredentialByType(userId, type);
    }

    @javax.validation.Valid
    @DELETE
    @Path("/credentials/{type}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete all credentials of a specified type for a user.",
            notes = "Deletes all enrolled credentials of the specified type for a user. Only supported for "
                    + "backup-code. Requires administrative privileges with scope internal_user_credential_mgt_delete.",
            response = Void.class)
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 204, message = "User Credentials Deleted."),
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request."),
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden."),
        @io.swagger.annotations.ApiResponse(code = 404, message = "User Not Found."),
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.")
    })
    public Response deleteUserCredentialsByType(
            @ApiParam(value = "The unique identifier of the user.", required = true)
            @PathParam("user-id") String userId,
            @ApiParam(value = "The type of the credentials to delete.", required = true,
                    allowableValues = "backup-code")
            @PathParam("type") String type) {

        return delegate.deleteUserCredentialsByType(userId, type);
    }

    @javax.validation.Valid
    @DELETE
    @Path("/credentials/{type}/{credential-id}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete a user-enrolled credential.",
            notes = "Deletes a specific enrolled credential for a user. Requires administrative privileges with "
                    + "scope internal_user_credential_mgt_delete.",
            response = Void.class)
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 204, message = "User Credential Deleted."),
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request."),
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden."),
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.")
    })
    public Response deleteUserCredentialById(
            @ApiParam(value = "The unique identifier of the user.", required = true)
            @PathParam("user-id") String userId,
            @ApiParam(value = "The type of the credential.", required = true,
                    allowableValues = "passkey, push-auth")
            @PathParam("type") String type,
            @ApiParam(value = "The unique identifier of the credential to delete.", required = true)
            @PathParam("credential-id") String credentialId) {

        return delegate.deleteUserCredentialById(userId, type, credentialId);
    }

    @javax.validation.Valid
    @GET
    @Path("/credentials")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get all credentials for a user grouped by type",
            notes = "Retrieves all user-enrolled credentials grouped by credential type. Requires administrative "
                    + "privileges with scope internal_user_credential_mgt_view. Supported credential types are "
                    + "passkey, push authentication, and backup codes.",
            response = java.util.Map.class)
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request."),
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden."),
        @io.swagger.annotations.ApiResponse(code = 404, message = "User Not Found."),
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.")
    })
    public Response getUserCredentialsById(
            @ApiParam(value = "The unique identifier of the user.", required = true)
            @PathParam("user-id") String userId) {

        return delegate.getUserCredentialsById(userId);
    }
}
