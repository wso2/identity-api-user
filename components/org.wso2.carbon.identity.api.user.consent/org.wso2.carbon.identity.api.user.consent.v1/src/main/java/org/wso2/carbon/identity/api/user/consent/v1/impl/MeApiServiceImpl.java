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

package org.wso2.carbon.identity.api.user.consent.v1.impl;

import org.wso2.carbon.identity.api.user.consent.v1.MeApiService;
import org.wso2.carbon.identity.api.user.consent.v1.impl.core.UserConsentService;
import org.wso2.carbon.identity.api.user.consent.v1.impl.factories.UserConsentServiceFactory;
import org.wso2.carbon.identity.api.user.consent.v1.model.AuthorizationRequest;
import org.wso2.carbon.identity.api.user.consent.v1.model.AuthorizationResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentCreateResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentInput;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentSummary;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentValidationResponse;

import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Implementation of the self-service consent API for the authenticated user.
 */
public class MeApiServiceImpl implements MeApiService {

    private final UserConsentService userConsentService;

    public MeApiServiceImpl() {

        userConsentService = UserConsentServiceFactory.getUserConsentService();
    }

    @Override
    public Response addConsentOfLoggedInUser(ConsentInput consent) {

        ConsentCreateResponse response = userConsentService.addConsent(consent);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @Override
    public Response authorizeConsentOfLoggedInUser(String consentId, AuthorizationRequest authorization) {

        AuthorizationResponse response = userConsentService.authorizeConsent(consentId, authorization);
        return Response.ok(response).build();
    }

    @Override
    public Response getConsentOfLoggedInUser(String consentId) {

        ConsentResponse response = userConsentService.getConsent(consentId);
        return Response.ok(response).build();
    }

    @Override
    public Response listConsentsOfLoggedInUser(String serviceId, String state, String purposeId,
                                               String purposeVersionId, String filter,
                                               Integer limit, String after, String before) {

        List<ConsentSummary> summaries = userConsentService.listConsents(
                serviceId, state, purposeId, purposeVersionId, filter, limit, after, before);
        return Response.ok(summaries).build();
    }

    @Override
    public Response revokeConsentOfLoggedInUser(String consentId) {

        userConsentService.revokeConsent(consentId);
        return Response.ok().build();
    }

    @Override
    public Response validateConsentOfLoggedInUser(String consentId) {

        ConsentValidationResponse response = userConsentService.validateConsent(consentId);
        return Response.ok(response).build();
    }
}
