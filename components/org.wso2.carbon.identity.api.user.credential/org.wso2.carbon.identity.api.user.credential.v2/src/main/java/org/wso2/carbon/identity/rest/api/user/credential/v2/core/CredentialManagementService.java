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

package org.wso2.carbon.identity.rest.api.user.credential.v2.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.credential.common.CredentialHandler;
import org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.user.credential.common.dto.CreatedCredentialDTO;
import org.wso2.carbon.identity.api.user.credential.common.dto.CredentialDTO;
import org.wso2.carbon.identity.api.user.credential.common.dto.CredentialGroupDTO;
import org.wso2.carbon.identity.api.user.credential.common.exception.CredentialMgtClientException;
import org.wso2.carbon.identity.api.user.credential.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.api.user.credential.common.exception.CredentialMgtServerException;
import org.wso2.carbon.identity.api.user.credential.common.utils.CredentialManagementUtils;
import org.wso2.carbon.identity.rest.api.user.credential.v2.dto.CredentialCreationResponseDTO;
import org.wso2.carbon.identity.rest.api.user.credential.v2.dto.CredentialEntryDTO;
import org.wso2.carbon.identity.rest.api.user.credential.v2.dto.CredentialsByTypeDTO;
import org.wso2.carbon.identity.rest.api.user.credential.v2.utils.CredentialMgtEndpointUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_CREDENTIAL_CREATION_NOT_SUPPORTED;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_CREDENTIAL_DELETION_BY_ID_NOT_SUPPORTED;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_CREDENTIAL_DELETION_BY_TYPE_NOT_SUPPORTED;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_HANDLER_NOT_REGISTERED;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_ID;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_TYPE;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_OPERATION;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_USER_NOT_FOUND;

/**
 * Core service for user credential management operations.
 * Validates user IDs, resolves credential handlers, and translates handler results to API response DTOs.
 */
public class CredentialManagementService {

    private static final Log LOG = LogFactory.getLog(CredentialManagementService.class);

    private final Map<CredentialTypes, CredentialHandler> handlerMap;

    public CredentialManagementService(Map<CredentialTypes, CredentialHandler> handlerMap) {

        this.handlerMap = handlerMap;
    }

    /**
     * Creates a credential of the given type for the specified user.
     *
     * @param userId User ID.
     * @param type   Credential type string.
     * @return Populated CredentialCreationResponseDTO.
     */
    public CredentialCreationResponseDTO createUserCredential(String userId, String type) {

        try {
            CredentialHandler handler = resolveCredentialHandler(type);
            validateUserId(userId);
            CreatedCredentialDTO dto = handler.createCredential(userId);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully created credential type: " + type + " for user ID: " + userId);
            }
            return new CredentialCreationResponseDTO()
                    .type(type)
                    .credentials(dto.getCredentials());
        } catch (CredentialMgtException e) {
            if (ERROR_CODE_UNSUPPORTED_OPERATION.getCode().equals(e.getErrorCode())) {
                throw CredentialMgtEndpointUtils.handleUnsupportedOperations(
                        ERROR_CODE_CREDENTIAL_CREATION_NOT_SUPPORTED, type);
            }
            throw CredentialMgtEndpointUtils.handleCredentialMgtException(e);
        }
    }

    /**
     * Deletes all credentials of the given type for the specified user.
     *
     * @param userId User ID.
     * @param type   Credential type string.
     */
    public void deleteUserCredentialsByType(String userId, String type) {

        try {
            CredentialHandler handler = resolveCredentialHandler(type);
            validateUserId(userId);
            handler.deleteCredentials(userId);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully deleted all credentials of type: " + type + " for user ID: " + userId);
            }
        } catch (CredentialMgtException e) {
            if (ERROR_CODE_UNSUPPORTED_OPERATION.getCode().equals(e.getErrorCode())) {
                throw CredentialMgtEndpointUtils.handleUnsupportedOperations(
                        ERROR_CODE_CREDENTIAL_DELETION_BY_TYPE_NOT_SUPPORTED, type);
            }
            throw CredentialMgtEndpointUtils.handleCredentialMgtException(e);
        }
    }

    /**
     * Deletes a specific credential by ID for the specified user.
     *
     * @param userId       User ID.
     * @param type         Credential type string.
     * @param credentialId Credential ID to delete.
     */
    public void deleteUserCredentialById(String userId, String type, String credentialId) {

        try {
            CredentialHandler handler = resolveCredentialHandler(type);
            validateUserId(userId);
            validateCredentialId(credentialId);
            handler.deleteCredentialById(userId, credentialId);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully deleted credential type: " + type + " with ID: " + credentialId
                        + " for user ID: " + userId);
            }
        } catch (CredentialMgtException e) {
            if (ERROR_CODE_UNSUPPORTED_OPERATION.getCode().equals(e.getErrorCode())) {
                throw CredentialMgtEndpointUtils.handleUnsupportedOperations(
                        ERROR_CODE_CREDENTIAL_DELETION_BY_ID_NOT_SUPPORTED, type);
            }
            throw CredentialMgtEndpointUtils.handleCredentialMgtException(e);
        }
    }

    /**
     * Retrieves all credentials for the specified user, grouped by type.
     *
     * @param userId User ID.
     * @return Populated CredentialsByTypeDTO.
     */
    public CredentialsByTypeDTO getUserCredentials(String userId) {

        validateUserId(userId);
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
                        LOG.warn("No response mapping for credential type: " + entry.getKey() + ". Skipping.");
                        break;
                }
            } catch (CredentialMgtException e) {
                throw CredentialMgtEndpointUtils.handleCredentialMgtException(e);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully retrieved credentials for user ID: " + userId);
        }
        return response;
    }

    private CredentialHandler resolveCredentialHandler(String type) throws CredentialMgtException {

        CredentialTypes credentialType = CredentialTypes.fromString(type);
        if (credentialType == null) {
            throw new CredentialMgtClientException(ERROR_CODE_INVALID_CREDENTIAL_TYPE);
        }
        if (!handlerMap.containsKey(credentialType)) {
            throw new CredentialMgtServerException(ERROR_CODE_HANDLER_NOT_REGISTERED);
        }
        return handlerMap.get(credentialType);
    }

    private List<CredentialEntryDTO> toEntryDTOs(List<CredentialDTO> credentials) {

        if (credentials == null) {
            return Collections.emptyList();
        }
        return credentials.stream()
                .map(dto -> new CredentialEntryDTO()
                        .credentialId(dto.getCredentialId())
                        .displayName(dto.getDisplayName()))
                .collect(Collectors.toList());
    }

    private void validateUserId(String userId) {

        try {
            if (!CredentialManagementUtils.validateUserId(userId)) {
                throw CredentialMgtEndpointUtils.handleCredentialMgtException(
                        CredentialManagementUtils.handleClientException(ERROR_CODE_USER_NOT_FOUND, null, userId));
            }
        } catch (CredentialMgtServerException e) {
            throw CredentialMgtEndpointUtils.handleCredentialMgtException(e);
        }
    }

    private void validateCredentialId(String credentialId) throws CredentialMgtClientException {

        if (StringUtils.isBlank(credentialId)) {
            throw new CredentialMgtClientException(ERROR_CODE_INVALID_CREDENTIAL_ID);
        }
    }
}
