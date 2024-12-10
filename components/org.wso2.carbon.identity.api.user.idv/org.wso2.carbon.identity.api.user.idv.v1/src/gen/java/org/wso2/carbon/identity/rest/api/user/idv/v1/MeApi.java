/*
 * Copyright (c) 2023-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.idv.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.rest.api.user.idv.v1.factories.MeApiServiceFactory;
import org.wso2.carbon.identity.rest.api.user.idv.v1.model.Error;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.idv.v1.model.VerificationClaimRequest;
import org.wso2.carbon.identity.rest.api.user.idv.v1.model.VerificationClaimResponse;
import org.wso2.carbon.identity.rest.api.user.idv.v1.model.VerificationClaimUpdateRequest;
import org.wso2.carbon.identity.rest.api.user.idv.v1.model.VerificationPostResponse;
import org.wso2.carbon.identity.rest.api.user.idv.v1.model.VerifyRequest;

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
    @Path("/idv/claims")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "application/xml",  })
    @ApiOperation(value = "Add identity verification claim.", notes = "This API provides the capability to add verification claim data", response = List.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Me - Identity Verification", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response", response = VerificationClaimResponse.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response meAddIdVClaim(@ApiParam(value = "This represents the identity provider to be created." ,required=true) @Valid List<VerificationClaimRequest> verificationClaimRequest) {

        return delegate.meAddIdVClaim(verificationClaimRequest );
    }

    @Valid
    @GET
    @Path("/idv/claims/{claim-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get identity verification claim", notes = "This API provides the capability to retrieve a identity verification claim of a user.", response = VerificationClaimResponse.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Me - Identity Verification", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = VerificationClaimResponse.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid status value", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response meGetIdVClaim(@ApiParam(value = "Claim that needs to retrieve verification metadata",required=true) @PathParam("claim-id") String claimId) {

        return delegate.meGetIdVClaim(claimId );
    }

    @Valid
    @GET
    @Path("/idv/claims")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get the identity verification claims of a user", notes = "This API provides the capability to retrieve the verification details of a user", response = VerificationClaimResponse.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Me - Identity Verification", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = VerificationClaimResponse.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid status value", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response meGetIdVClaims(    @Valid@ApiParam(value = "Id of the identity verification provider. ")  @QueryParam("idVProviderId") String idVProviderId) {

        return delegate.meGetIdVClaims(idVProviderId );
    }

    @Valid
    @PUT
    @Path("/idv/claims/{claim-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update identity verification claim", notes = "This API provides the capability to update a identity verification claim of a user.", response = VerificationClaimResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Me - Identity Verification", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = VerificationClaimResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response meUpdateIdVClaim(@ApiParam(value = "Claim that needs to retrieve verification metadata",required=true) @PathParam("claim-id") String claimId, @ApiParam(value = "" ,required=true) @Valid VerificationClaimUpdateRequest verificationClaimUpdateRequest) {

        return delegate.meUpdateIdVClaim(claimId,  verificationClaimUpdateRequest );
    }

    @Valid
    @PUT
    @Path("/idv/claims")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "application/xml",  })
    @ApiOperation(value = "Update identity verification claims.", notes = "This API provides the capability to update verification claims of a user", response = List.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Me - Identity Verification", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response", response = VerificationClaimResponse.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response meUpdateIdVClaims(@ApiParam(value = "This represents the identity provider to be created." ,required=true) @Valid List<VerificationClaimRequest> verificationClaimRequest) {

        return delegate.meUpdateIdVClaims(verificationClaimRequest );
    }

    @Valid
    @POST
    @Path("/idv/verify")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Verify an Identity", notes = "This API provides the capability to verify a user with the configured verification required attributes", response = VerificationPostResponse.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Me - Identity Verification" })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Accepted", response = VerificationPostResponse.class, responseContainer = "List"),
        @ApiResponse(code = 200, message = "Successful operation", response = VerificationPostResponse.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response meVerifyIdentity(@ApiParam(value = "Verify an identity" ,required=true) @Valid VerifyRequest verifyRequest) {

        return delegate.meVerifyIdentity(verifyRequest );
    }

}
