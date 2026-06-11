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

package org.wso2.carbon.identity.rest.api.user.credential.v2.impl;

import org.wso2.carbon.identity.rest.api.user.credential.v2.UsersApiService;
import org.wso2.carbon.identity.rest.api.user.credential.v2.core.CredentialManagementService;
import org.wso2.carbon.identity.rest.api.user.credential.v2.dto.CredentialCreationResponseDTO;
import org.wso2.carbon.identity.rest.api.user.credential.v2.dto.CredentialsByTypeDTO;

import javax.ws.rs.core.Response;

/**
 * Implementation of User Credential API Service v2.
 */
public class UsersCredentialApiServiceImpl implements UsersApiService {

    private final CredentialManagementService credentialManagementService;

    public UsersCredentialApiServiceImpl(CredentialManagementService credentialManagementService) {

        this.credentialManagementService = credentialManagementService;
    }

    @Override
    public Response createUserCredentialByType(String userId, String type) {

        CredentialCreationResponseDTO response = credentialManagementService.createUserCredential(userId, type);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @Override
    public Response deleteUserCredentialsByType(String userId, String type) {

        credentialManagementService.deleteUserCredentialsByType(userId, type);
        return Response.noContent().build();
    }

    @Override
    public Response deleteUserCredentialById(String userId, String type, String credentialId) {

        credentialManagementService.deleteUserCredentialById(userId, type, credentialId);
        return Response.noContent().build();
    }

    @Override
    public Response getUserCredentialsById(String userId) {

        CredentialsByTypeDTO response = credentialManagementService.getUserCredentials(userId);
        return Response.ok(response).build();
    }
}
