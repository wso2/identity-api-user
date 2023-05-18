/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.idv.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.idv.v1.DefaultApiService;
import org.wso2.carbon.identity.rest.api.user.idv.v1.core.IdentityVerificationService;
import org.wso2.carbon.identity.rest.api.user.idv.v1.model.VerificationClaimRequest;
import org.wso2.carbon.identity.rest.api.user.idv.v1.model.VerificationClaimResponse;
import org.wso2.carbon.identity.rest.api.user.idv.v1.model.VerificationClaimUpdateRequest;
import org.wso2.carbon.identity.rest.api.user.idv.v1.model.VerificationPostResponse;
import org.wso2.carbon.identity.rest.api.user.idv.v1.model.VerifyRequest;

import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.ContextLoader.getUserIdFromContext;

/**
 * This class implements the DefaultApiServiceImpl interface.
 */
public class DefaultApiServiceImpl implements DefaultApiService {

    @Autowired
    IdentityVerificationService identityVerificationService;

    @Override
    public Response addIdVClaim(String userId, List<VerificationClaimRequest> verificationClaimRequest) {

        List<VerificationClaimResponse> idvAddClaimResponse =
                identityVerificationService.addIdVClaims(getUserIdFromContext(), verificationClaimRequest);
        return Response.ok().entity(idvAddClaimResponse).build();
    }

    @Override
    public Response getIdVClaim(String userId, String claimId) {

        VerificationClaimResponse verificationClaimResponse =
                identityVerificationService.getIdVClaim(userId, claimId);
        return Response.ok().entity(verificationClaimResponse).build();
    }

    @Override
    public Response getIdVClaims(String userId, String idvProviderid) {

        List<VerificationClaimResponse> verificationClaimResponse =
                identityVerificationService.getIdVClaims(userId, idvProviderid);
        return Response.ok().entity(verificationClaimResponse).build();
    }

    @Override
    public Response updateIdVClaim(String claimId, String userId,
                                   VerificationClaimUpdateRequest verificationClaimUpdateRequest) {

        VerificationClaimResponse verificationClaimResponse =
                identityVerificationService.updateIdVClaim(userId, claimId, verificationClaimUpdateRequest);
        return Response.ok().entity(verificationClaimResponse).build();
    }

    @Override
    public Response updateIdVClaims(String userId, List<VerificationClaimRequest> verificationClaimRequest) {

        List<VerificationClaimResponse> idvClaimUpdateResponse =
                identityVerificationService.updateIdVClaims(userId, verificationClaimRequest);
        return Response.ok().entity(idvClaimUpdateResponse).build();
    }

    @Override
    public Response verifyIdentity(String userId, VerifyRequest verifyRequest) {

        VerificationPostResponse verificationPostResponse =
                identityVerificationService.verifyIdentity(userId, verifyRequest);
        return Response.ok().entity(verificationPostResponse).build();
    }
}
