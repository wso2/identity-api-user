/*
 * Copyright (c) 2020-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.association.v1;

import org.wso2.carbon.identity.rest.api.user.association.v1.factories.MeApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.rest.api.user.association.v1.dto.UserDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationUserRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.FederatedAssociationDTO;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/me")
@Consumes({ "application/json" })
@Produces({ "application/json" })
@io.swagger.annotations.Api(value = "/me", description = "the me API")
public class MeApi  {

    private final MeApiService delegate;

    public MeApi() {

        this.delegate = MeApiServiceFactory.getMeApi();
    }

    @Valid
    @DELETE
    @Path("/associations/{associated-user-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete given user account from my user associations",
            notes = "This API is used to delete the given user account from the associations of the auhtentiated user.\n\n  <b>Permission required:</b>\n\n  * /permission/admin/login\n",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response meAssociationsAssociatedUserIdDelete(@ApiParam(value = "",required=true ) @PathParam("associated-user-id")  String associatedUserId) {

        return delegate.meAssociationsAssociatedUserIdDelete(associatedUserId);
    }

    @Valid
    @DELETE
    @Path("/associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete all my user associations",
            notes = "This API is used to delete all associations of the auhtentiated user.\n\n  <b>Permission required:</b>\n\n  * /permission/admin/login\n",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response meAssociationsDelete() {

        return delegate.meAssociationsDelete();
    }

    @Valid
    @GET
    @Path("/associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrive the associations of the authenticated user.",
            notes = "This API is used to retrieve the associations of the authenticated user.\n\n  <b>Permission required:</b>\n\n  * /permission/admin/login\n",
            response = UserDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation"),
        
        @io.swagger.annotations.ApiResponse(code = 204, message = "No content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response meAssociationsGet() {

        return delegate.meAssociationsGet();
    }

    @Valid
    @POST
    @Path("/associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Associate a user to the authenticated user\n",
            notes = "This API is used to associate a user to the authenticated user. For example if it is required associate  my user account to the user account of 'john', this endpoint can be used. userId and the password are required to associate the accounts.\n\n <b>Permission required:</b>\n * /permission/admin/login\n",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Successfully created"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Conflict"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response meAssociationsPost(@ApiParam(value = "User details to be associated. userId should be the fully qalified username of the user." ,required=true ) @Valid AssociationUserRequestDTO association) {

        return delegate.meAssociationsPost(association);
    }

    @Valid
    @DELETE
    @Path("/federated-associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete all my federated user associations",
            notes = "This API is used to delete all federated associations of the auhtentiated user.\n\n  <b>Permission required:</b>\n\n  * /permission/admin/login\n",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response meFederatedAssociationsDelete() {

        return delegate.meFederatedAssociationsDelete();
    }

    @Valid
    @GET
    @Path("/federated-associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrive the federated associations of the authenticated user.",
            notes = "This API is used to retrieve the federated associations of the authenticated user.\n\n  <b>Permission required:</b>\n\n  * /permission/admin/login\n",
            response = FederatedAssociationDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation"),
        
        @io.swagger.annotations.ApiResponse(code = 204, message = "No content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response meFederatedAssociationsGet() {

        return delegate.meFederatedAssociationsGet();
    }

    @Valid
    @DELETE
    @Path("/federated-associations/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete my federated association",
            notes = "This API is used to delete a federated association of the auhtentiated user.\n\n<b>Permission required:</b>\n  * /permission/admin/manage/identity/user/association/delete\n",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized request"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response meFederatedAssociationsIdDelete(@ApiParam(value = "",required=true ) @PathParam("id")  String id) {

        return delegate.meFederatedAssociationsIdDelete(id);
    }

}
