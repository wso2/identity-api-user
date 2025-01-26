/*
 * Copyright (c) 2022-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.mfa.v1.impl;

import org.wso2.carbon.identity.rest.api.user.mfa.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.mfa.v1.core.MFAService;

import org.wso2.carbon.identity.rest.api.user.mfa.v1.dto.EnabledAuthenticatorsDTO;
import org.wso2.carbon.identity.rest.api.user.mfa.v1.factories.MFAServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Implementation of MeApi Service.
 */
public class MeApiServiceImpl extends MeApiService {

    private final MFAService mfaService;

    public MeApiServiceImpl() {

        try {
            this.mfaService = MFAServiceFactory.getMFAService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating the MFAService.", e);
        }
    }

    @Override
    public Response meMfaAuthenticatorsGet() {

        return Response.ok().entity(mfaService.getEnabledAuthenticators()).build();
    }

    @Override
    public Response meMfaAuthenticatorsPost(EnabledAuthenticatorsDTO request) {

        return Response.ok().entity(mfaService.updateEnabledAuthenticators(request.getEnabledAuthenticators())).build();
    }
}
