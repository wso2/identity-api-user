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

package org.wso2.carbon.identity.rest.api.user.association.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.*;
import org.wso2.carbon.identity.rest.api.user.association.v1.UserIdApiService;
import org.wso2.carbon.identity.rest.api.user.association.v1.factories.UserIdApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.rest.api.user.association.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.UserDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.FederatedAssociationDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/{user-id}")
@Consumes({ "application/json" })
@Produces({ "application/json" })
@io.swagger.annotations.Api(value = "/{user-id}", description = "the {user-id} API")
public class UserIdApi  {

    @Autowired
    private UserIdApiService delegate;

    @Valid
    @DELETE
    @Path("/associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete user's all user associations",
            notes = "This API is used to delete all associations of the  user.\n\n<b>Permission required:</b>\n  * /permission/admin/manage/identity/user/association/delete\n",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized request"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response userIdAssociationsDelete(@ApiParam(value = "",required=true ) @PathParam("user-id")  String userId) {

        return delegate.userIdAssociationsDelete(userId);
    }

    @Valid
    @GET
    @Path("/associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get user's associations",
            notes = "This API is used to retrieve the associations of the user.\n\n  <b>Permission required:</b>\n* /permission/admin/manage/identity/user/association/view\n",
            response = UserDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation"),
        
        @io.swagger.annotations.ApiResponse(code = 204, message = "No content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized request"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response userIdAssociationsGet(@ApiParam(value = "user id",required=true ) @PathParam("user-id")  String userId) {

        return delegate.userIdAssociationsGet(userId);
    }

    @Valid
    @DELETE
    @Path("/federated-associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete user's all user federated associations",
            notes = "This API is used to delete all federated associations of the  user.\n\n<b>Permission required:</b>\n  * /permission/admin/manage/identity/user/association/delete\n",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized request"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response userIdFederatedAssociationsDelete(@ApiParam(value = "",required=true ) @PathParam("user-id")  String userId) {

        return delegate.userIdFederatedAssociationsDelete(userId);
    }

    @Valid
    @GET
    @Path("/federated-associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get user's federated associations",
            notes = "This API is used to retrieve the federated associations of the user.\n\n  <b>Permission required:</b>\n* /permission/admin/manage/identity/user/association/view\n",
            response = FederatedAssociationDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation"),
        
        @io.swagger.annotations.ApiResponse(code = 204, message = "No content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized request"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response userIdFederatedAssociationsGet(@ApiParam(value = "user id",required=true ) @PathParam("user-id")  String userId) {

        return delegate.userIdFederatedAssociationsGet(userId);
    }

    @Valid
    @POST
    @Path("/federated-associations")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create federated user association\n",
            notes = "This API is used to create federated user associations. <br>\n <b>Permission required:</b>\n * /permission/admin/manage/identity/user/association/create\n <b>Scope required:</b>\n * internal_user_association_create",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 201, message = "Successfully created"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),

            @io.swagger.annotations.ApiResponse(code = 409, message = "Conflict"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response userIdFederatedAssociationsPost(@ApiParam(value = "",required=true ) @PathParam("user-id")  String userId,
                                       @ApiParam(value = "User details to be associated." ,required=true ) @Valid FederatedAssociationRequestDTO association) {

        return delegate.userIdFederatedAssociationsPost(userId, association);
    }

    @Valid
    @DELETE
    @Path("/federated-associations/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete user's federated association",
            notes = "This API is used to delete a federated association of the user.\n\n<b>Permission required:</b>\n  * /permission/admin/manage/identity/user/association/delete\n",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized request"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response userIdFederatedAssociationsIdDelete(@ApiParam(value = "",required=true ) @PathParam("user-id")  String userId,
    @ApiParam(value = "",required=true ) @PathParam("id")  String id) {

        return delegate.userIdFederatedAssociationsIdDelete(userId,id);
    }

}
