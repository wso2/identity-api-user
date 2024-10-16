/*
 * Copyright (c) 2019-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.application.v1;

import org.wso2.carbon.identity.rest.api.user.application.v1.factories.MeApiServiceFactory;
import org.wso2.carbon.identity.rest.api.user.application.v1.model.ApplicationListResponse;
import org.wso2.carbon.identity.rest.api.user.application.v1.model.ApplicationResponse;
import org.wso2.carbon.identity.rest.api.user.application.v1.model.Error;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/me")
@Api(description = "The me API")

public class MeApi  {

    private final MeApiService delegate;

    public MeApi(){

        this.delegate = MeApiServiceFactory.getMeApi();
    }

    @Valid
    @GET
    @Path("/applications/{applicationId}")
    
    @Produces({ "application/json", "application/json'" })
    @ApiOperation(value = "Get application by Id.", notes = "This API provides the capability to retrieve an application authorized to the user. ", response = ApplicationResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Listing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ApplicationResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Error.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = Error.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getApplication(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getApplication(applicationId );
    }

    @Valid
    @GET
    @Path("/applications")
    
    @Produces({ "application/json", "application/json'" })
    @ApiOperation(value = "List applications.", notes = "This API provides the capability to retrive the list of applications authorized to the user. ", response = ApplicationListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Listing" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ApplicationListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Error.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = Error.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response getApplications(    @Valid@ApiParam(value = "Define only the required attributes to be sent in the response object as a comma separated string.")  @QueryParam("attributes") String attributes,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to return.", defaultValue="30") @DefaultValue("30")  @QueryParam("limit") Integer limit,     @Valid @Min(0)@ApiParam(value = "Number of records to skip for pagination.", defaultValue="0") @DefaultValue("0")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Condition to filter the retrival of records.")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Define the order how the retrieved records should be sorted. _This parameter is not supported yet._ ", allowableValues="asc, desc")  @QueryParam("sortOrder") String sortOrder,     @Valid@ApiParam(value = "Attribute by which the retrieved records should be sorted. _This parameter is not supported yet._ ")  @QueryParam("sortBy") String sortBy) {

        return delegate.getApplications(attributes,  limit,  offset,  filter,  sortOrder,  sortBy );
    }

}
