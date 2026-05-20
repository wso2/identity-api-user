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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.credential.common.CredentialHandler;
import org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.user.credential.common.dto.CreatedCredentialDTO;
import org.wso2.carbon.identity.api.user.credential.common.dto.CredentialDTO;
import org.wso2.carbon.identity.api.user.credential.common.dto.CredentialGroupDTO;
import org.wso2.carbon.identity.api.user.credential.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.rest.api.user.credential.v2.UsersApiService;
import org.wso2.carbon.identity.rest.api.user.credential.v2.dto.CredentialCreationResponseDTO;
import org.wso2.carbon.identity.rest.api.user.credential.v2.dto.CredentialEntryDTO;
import org.wso2.carbon.identity.rest.api.user.credential.v2.dto.CredentialsByTypeDTO;
import org.wso2.carbon.identity.rest.api.user.credential.v2.utils.CredentialMgtEndpointUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

/**
 * Implementation of User Credential API Service v2.
 */
public class UsersCredentialApiServiceImpl implements UsersApiService {

    private static final Log LOG = LogFactory.getLog(UsersCredentialApiServiceImpl.class);

    private final Map<CredentialTypes, CredentialHandler> handlerMap;

    public UsersCredentialApiServiceImpl(Map<CredentialTypes, CredentialHandler> handlerMap) {

        this.handlerMap = handlerMap;
    }

    @Override
    public Response createUserCredentialByType(String userId, String type) {

        try {
            CredentialMgtEndpointUtils.validateCreatableCredentialType(type);
            CredentialTypes credentialType = CredentialTypes.fromString(type);
            CreatedCredentialDTO dto = handlerMap.get(credentialType).createCredential(userId);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully created credential type: " + type + " for user ID: " + userId);
            }
            CredentialCreationResponseDTO response = new CredentialCreationResponseDTO()
                    .credentialId(dto.getCredentialId())
                    .type(type)
                    .credentials(dto.getCredentials());
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (CredentialMgtException e) {
            throw CredentialMgtEndpointUtils.handleCredentialMgtException(e);
        }
    }

    @Override
    public Response deleteUserCredentialsByType(String userId, String type) {

        try {
            CredentialMgtEndpointUtils.validateCredentialType(type);
            CredentialTypes credentialType = CredentialTypes.fromString(type);
            handlerMap.get(credentialType).deleteCredentials(userId);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully deleted all credentials of type: " + type + " for user ID: " + userId);
            }
            return Response.noContent().build();
        } catch (CredentialMgtException e) {
            throw CredentialMgtEndpointUtils.handleCredentialMgtException(e);
        }
    }

    @Override
    public Response deleteUserCredentialById(String userId, String type, String credentialId) {

        try {
            CredentialMgtEndpointUtils.validateCredentialId(credentialId);
            CredentialMgtEndpointUtils.validateCredentialType(type);
            CredentialTypes credentialType = CredentialTypes.fromString(type);
            handlerMap.get(credentialType).deleteCredentialById(userId, credentialId);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully deleted credential type: " + type + " with ID: " + credentialId
                        + " for user ID: " + userId);
            }
            return Response.noContent().build();
        } catch (CredentialMgtException e) {
            throw CredentialMgtEndpointUtils.handleCredentialMgtException(e);
        }
    }

    @Override
    public Response getUserCredentialsById(String userId) {

        CredentialsByTypeDTO response = new CredentialsByTypeDTO();
        for (Map.Entry<CredentialTypes, CredentialHandler> entry : handlerMap.entrySet()) {
            try {
                CredentialGroupDTO group = entry.getValue().getCredentials(userId);
                if (!group.isConfigured()) {
                    continue;
                }
                switch (entry.getKey()) {
                    case PASSKEY:
                        response.passkey(toEntryDTOs(group.getCredentials()));
                        break;
                    case PUSH_AUTH:
                        response.pushAuth(toEntryDTOs(group.getCredentials()));
                        break;
                    case BACKUP_CODE:
                        response.backupCode(true);
                        break;
                    default:
                        break;
                }
            } catch (CredentialMgtException e) {
                throw CredentialMgtEndpointUtils.handleCredentialMgtException(e);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully retrieved credentials for user ID: " + userId);
        }
        return Response.ok(response).build();
    }

    private List<CredentialEntryDTO> toEntryDTOs(List<CredentialDTO> credentials) {

        return credentials.stream()
                .map(dto -> new CredentialEntryDTO()
                        .credentialId(dto.getCredentialId())
                        .displayName(dto.getDisplayName()))
                .collect(Collectors.toList());
    }
}
