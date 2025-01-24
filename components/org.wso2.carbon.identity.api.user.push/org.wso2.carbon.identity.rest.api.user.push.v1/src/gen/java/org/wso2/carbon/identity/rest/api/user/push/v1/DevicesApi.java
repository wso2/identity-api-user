/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.push.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.rest.api.user.push.v1.factories.DevicesApiServiceFactory;
import org.wso2.carbon.identity.rest.api.user.push.v1.model.DeviceDTO;
import org.wso2.carbon.identity.rest.api.user.push.v1.model.Error;
import org.wso2.carbon.identity.rest.api.user.push.v1.model.RegistrationRequestDTO;
import org.wso2.carbon.identity.rest.api.user.push.v1.model.RemoveRequestDTO;
import org.wso2.carbon.identity.rest.api.user.push.v1.DevicesApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/devices")
@Api(description = "The devices API")

public class DevicesApi  {

    private final DevicesApiService delegate;

    public DevicesApi() {

        this.delegate = DevicesApiServiceFactory.getDevicesApi();
    }

    @Valid
    @DELETE
    @Path("/{deviceId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Remove a registered device", notes = "This API is used to remove a registered device. ", response = Void.class, tags={ "me", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "The device was removed.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response deleteDeviceById(@ApiParam(value = "ID of the device to be removed",required=true) @PathParam("deviceId") String deviceId) {

        return delegate.deleteDeviceById(deviceId );
    }

    @Valid
    @GET
    @Path("/{deviceId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get mobile device details by device ID.", notes = "This API is used to get a specific registered device ", response = DeviceDTO.class, tags={ "me", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully returned the device belongs to the ID", response = DeviceDTO.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response getDeviceById(@ApiParam(value = "ID of the device to be returned",required=true) @PathParam("deviceId") String deviceId) {

        return delegate.getDeviceById(deviceId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get all devices registered for a user.", notes = "This API is used to list the devices registered for the particular user. ", response = DeviceDTO.class, responseContainer = "List", tags={ "me", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Listed devices registered for a user", response = DeviceDTO.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response getDevices() {

        return delegate.getDevices();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Register mobile devices to receive push notifications.", notes = "This API is used to register a push notification device into the Identity Server. This API will be invoked from the mobile device. ", response = Void.class, tags={ "mobile", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Registered a new device", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response registerDevice(@ApiParam(value = "Request body sent by a device for registration." ,required=true) @Valid RegistrationRequestDTO registrationRequestDTO) {

        return delegate.registerDevice(registrationRequestDTO );
    }

    @Valid
    @POST
    @Path("/{deviceId}/remove")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Remove a registered device from the mobile device.", notes = "This API is used to remove a registered device through mobile device. ", response = Void.class, tags={ "mobile" })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "The device was removed.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response removeDeviceFromMobile(@ApiParam(value = "ID of the device to be removed",required=true) @PathParam("deviceId") String deviceId, @ApiParam(value = "Remove request sent from the device." ,required=true) @Valid RemoveRequestDTO removeRequestDTO) {

        return delegate.removeDeviceFromMobile(deviceId,  removeRequestDTO );
    }

}
