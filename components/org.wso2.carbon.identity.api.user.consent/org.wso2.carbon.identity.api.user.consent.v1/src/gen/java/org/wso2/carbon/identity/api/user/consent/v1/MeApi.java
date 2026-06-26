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

package org.wso2.carbon.identity.api.user.consent.v1;

import org.wso2.carbon.identity.api.user.consent.v1.factories.MeApiServiceFactory;
import org.wso2.carbon.identity.api.user.consent.v1.model.AuthorizationRequest;
import org.wso2.carbon.identity.api.user.consent.v1.model.AuthorizationResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentCreateResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentInput;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentSummary;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentValidationResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.Error;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/me")
@Api(description = "The me API")

public class MeApi  {

    private final MeApiService delegate;

    public MeApi() {

        this.delegate = MeApiServiceFactory.getMeApi();
    }

    @Valid
    @POST
    @Path("/consents")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a consent record", notes = "Record consent for specified purposes and elements. The authenticated user creates and gives consent simultaneously. The consent is immediately recorded as `ACTIVE` or `REJECTED` based on the provided state. <br> <b>Permission required:</b> <br> * None <br> <b>Scope required:</b> <br> * internal_login ", response = ConsentCreateResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "me", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Consent created successfully.", response = ConsentCreateResponse.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response addConsentOfLoggedInUser(@ApiParam(value = "Consent to be created." ,required=true) @Valid ConsentInput consent) {

        return delegate.addConsentOfLoggedInUser(consent );
    }

    @Valid
    @POST
    @Path("/consents/{consent-id}/authorize")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Authorize a consent", notes = "Authorizes a PENDING consent. The authenticated user must be in the consent's authorization list, returns 403 otherwise. When all required users have approved, the consent transitions to ACTIVE state. If any user rejects, the consent transitions to REJECTED state. <br> <b>Permission required:</b> <br> * None <br> <b>Scope required:</b> <br> * internal_login ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "me", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Authorization recorded successfully.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Resource Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response authorizeConsentOfLoggedInUser(@ApiParam(value = "UUID of the consent receipt.",required=true) @PathParam("consent-id") String consentId, @ApiParam(value = "Authorization decision." ,required=true) @Valid AuthorizationRequest authorization) {

        return delegate.authorizeConsentOfLoggedInUser(consentId,  authorization );
    }

    @Valid
    @GET
    @Path("/consents/{consent-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a consent record", notes = "Retrieve a specific consent record by consent ID. Only the consent subject may retrieve the record. Returns 403 if the authenticated user is not the consent subject. <br> <b>Permission required:</b> <br> * None <br> <b>Scope required:</b> <br> * internal_login ", response = ConsentResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "me", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Consent retrieved successfully.", response = ConsentResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Resource Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getConsentOfLoggedInUser(@ApiParam(value = "UUID of the consent receipt.",required=true) @PathParam("consent-id") String consentId) {

        return delegate.getConsentOfLoggedInUser(consentId );
    }

    @Valid
    @GET
    @Path("/consents")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List consent records", notes = "Retrieve consent records with optional filtering. Returns only the authenticated user's own consents. <br> <b>Permission required:</b> <br> * None <br> <b>Scope required:</b> <br> * internal_login ", response = ConsentSummary.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "me", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Consents retrieved successfully.", response = ConsentSummary.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response listConsentsOfLoggedInUser(    @Valid@ApiParam(value = "Filter consents by service identifier.")  @QueryParam("serviceId") String serviceId,     @Valid@ApiParam(value = "Filter consents by state.", allowableValues="PENDING, ACTIVE, REJECTED, REVOKED, EXPIRED")  @QueryParam("state") String state,     @Valid@ApiParam(value = "Filter consents by purpose UUID.")  @QueryParam("purposeId") String purposeId,     @Valid@ApiParam(value = "Filter consents by specific purpose version UUID.")  @QueryParam("purposeVersionId") String purposeVersionId,     @Valid@ApiParam(value = "Filter consents by custom properties using dot notation `properties.<key>`. Supports 'sw' (starts with), 'co' (contains), 'ew' (ends with), and 'eq' (equals) operations. Combine multiple conditions with 'and', 'or' logical operators. Examples: filter=properties.region eq EU ")  @QueryParam("filter") String filter,     @Valid @Min(1)@ApiParam(value = "Maximum number of records to return. Default is 10.", defaultValue="10") @DefaultValue("10")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Cursor for forward pagination. Pass the base64-encoded UUID of the last item received from the previous page to retrieve the next page of results. ")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Cursor for backward pagination. Pass the base64-encoded UUID of the first item received from the current page to retrieve the previous page of results. ")  @QueryParam("before") String before) {

        return delegate.listConsentsOfLoggedInUser(serviceId,  state,  purposeId,  purposeVersionId,  filter,  limit,  after,  before );
    }

    @Valid
    @POST
    @Path("/consents/{consent-id}/revoke")
    
    @Produces({ "*/*" })
    @ApiOperation(value = "Revoke a consent", notes = "Revokes the consent. The authenticated user must be the consent subject, returns 403 otherwise. Idempotent — if the consent is already revoked, returns 200 with no error. <br> <b>Permission required:</b> <br> * None <br> <b>Scope required:</b> <br> * internal_login ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "me", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Resource Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response revokeConsentOfLoggedInUser(@ApiParam(value = "UUID of the consent receipt.",required=true) @PathParam("consent-id") String consentId) {

        return delegate.revokeConsentOfLoggedInUser(consentId );
    }

    @Valid
    @GET
    @Path("/consents/{consent-id}/validate")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get the current state of a consent object", notes = "Returns the current state of a consent record, performing a lazy expiry check. The authenticated user must be the consent subject, returns 403 otherwise. <br> <b>Permission required:</b> <br> * None <br> <b>Scope required:</b> <br> * internal_login ", response = ConsentValidationResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Consent status retrieved successfully.", response = ConsentValidationResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Resource Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response validateConsentOfLoggedInUser(@ApiParam(value = "UUID of the consent receipt.",required=true) @PathParam("consent-id") String consentId) {

        return delegate.validateConsentOfLoggedInUser(consentId );
    }

}
