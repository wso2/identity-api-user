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

package org.wso2.carbon.identity.api.user.biometric.device.handler.v1;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;



import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

import org.wso2.carbon.identity.api.user.biometric.device.handler.v1.model.DeviceDTO;
import org.wso2.carbon.identity.api.user.biometric.device.handler.v1.model.ErrorDTO;


/**
 **/
@Path("/")
@Api(description = "The  API")

public class DefaultApi  {

    @Autowired
    private DefaultApiService delegate;

    @Valid
    @DELETE
    @Path("/{user-id}/biometricDevice/{deviceId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Remove devices by deviceId. ", notes = "This API is used by admins to remove a specific device <b>Permission required:</b>  * /permission/admin/manage/identity/user/biodevice/delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags = { "admin", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "No content", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unautharized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response userIdBiometricDeviceDeviceIdDelete(@ApiParam(value = "ID of user", required = true)@PathParam("user-id") String userId, @ApiParam(value = "Unique Id of device",required = true) @PathParam("deviceId") String deviceId) {

        return delegate.userIdBiometricDeviceDeviceIdDelete(userId,  deviceId );
    }

    @Valid
    @GET
    @Path("/{user-id}/biometricDevice/{deviceId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Returns Specific Device ", notes = "This API is by admin setto retrive aspecifc device <b>Permission required:</b>  * /permission/admin/manage/identity/user/biodevice/view ", response = DeviceDTO.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "admin", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Specific device selected by the user.", response = DeviceDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unautharized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response userIdBiometricDeviceDeviceIdGet(@ApiParam(value = "ID of user",required=true) @PathParam("user-id") String userId, @ApiParam(value = "ID of device to return",required=true) @PathParam("deviceId") String deviceId) {

        return delegate.userIdBiometricDeviceDeviceIdGet(userId,  deviceId );
    }

    @Valid
    @GET
    @Path("/{user-id}/biometricDevice/devices")
    
    @Produces({ "array", "application/json" })
    @ApiOperation(value = "Returns Devices of a user ", notes = "This API is used by admins to retrieve all devices registered under a user\" <b>Permission required:</b>  * /permission/admin/manage/identity/user/biodevice/list ", response = Object.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "admin" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "All registered devices of the user.", response = Object.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unautharized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not found", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response userIdBiometricDeviceDevicesGet(@ApiParam(value = "ID of user",required=true) @PathParam("user-id") String userId) {

        return delegate.userIdBiometricDeviceDevicesGet(userId );
    }

}
