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

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.rest.api.user.push.v1.factories.DiscoveryDataApiServiceFactory;
import org.wso2.carbon.identity.rest.api.user.push.v1.model.DiscoveryDataDTO;
import org.wso2.carbon.identity.rest.api.user.push.v1.model.Error;
import org.wso2.carbon.identity.rest.api.user.push.v1.DiscoveryDataApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/discovery-data")
@Api(description = "The discovery-data API")

public class DiscoveryDataApi  {

    private final DiscoveryDataApiService delegate;

    public DiscoveryDataApi() {

        this.delegate = DiscoveryDataApiServiceFactory.getDiscoveryDataApi();
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Generate data for device registration. ", notes = "This API is used to generate discovery data for the device registration QR code. ", response = DiscoveryDataDTO.class, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully generated registration discovery data.", response = DiscoveryDataDTO.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response getRegistrationDiscoveryData() {

        return delegate.getRegistrationDiscoveryData();
    }

}
