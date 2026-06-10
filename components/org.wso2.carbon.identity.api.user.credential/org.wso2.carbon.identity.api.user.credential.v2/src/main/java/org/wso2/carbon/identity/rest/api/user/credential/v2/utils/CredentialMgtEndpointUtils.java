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

package org.wso2.carbon.identity.rest.api.user.credential.v2.utils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorDTO;
import org.wso2.carbon.identity.api.user.credential.common.CredentialHandler;
import org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages;
import org.wso2.carbon.identity.api.user.credential.common.dto.CredentialDTO;
import org.wso2.carbon.identity.api.user.credential.common.dto.CredentialGroupDTO;
import org.wso2.carbon.identity.api.user.credential.common.exception.CredentialMgtClientException;
import org.wso2.carbon.identity.api.user.credential.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.rest.api.user.credential.v2.dto.CredentialEntryDTO;
import org.wso2.carbon.identity.rest.api.user.credential.v2.dto.CredentialsByTypeDTO;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

/**
 * Utility class for the User Credential Management Endpoint v2.
 */
public class CredentialMgtEndpointUtils {

    private static final Log LOG = LogFactory.getLog(CredentialMgtEndpointUtils.class);

    private CredentialMgtEndpointUtils() {

    }

    /**
     * Handles a CredentialMgtException and constructs an appropriate APIError response.
     *
     * @param e CredentialMgtException to handle.
     * @return APIError representing the error response.
     */
    public static APIError handleCredentialMgtException(CredentialMgtException e) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (e instanceof CredentialMgtClientException) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e.getMessage(), e);
            }
            if (StringUtils.equals(e.getErrorCode(),
                    ErrorMessages.ERROR_CODE_USER_NOT_FOUND.getCode())) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else {
            LOG.error(e.getMessage(), e);
        }

        String errorCode = e.getErrorCode();
        String description = StringUtils.isNotBlank(e.getDescription()) ? e.getDescription() : e.getMessage();
        return new APIError(status, getError(errorCode, e.getMessage(), description));
    }

    /**
     * Handles unsupported operations and constructs an appropriate APIError response.
     *
     * @param error Error message enum representing the unsupported operation.
     * @param data  Optional data to be included in the error description.
     * @return APIError representing the error response for unsupported operations.
     */
    public static APIError handleUnsupportedOperations(ErrorMessages error, Object... data) {

        Response.Status status = Response.Status.BAD_REQUEST;
        String errorCode = error.getCode();
        String message = error.getMessage();
        String description = error.getDescription();
        if (ArrayUtils.isNotEmpty(data)) {
            description = String.format(description, data);
        }
        return new APIError(status, getError(errorCode, message, description));
    }

    /**
     * Builds an ErrorDTO from the given fields.
     *
     * @param errorCode        Error code.
     * @param errorMessage     Error message.
     * @param errorDescription Error description.
     * @return ErrorDTO.
     */
    public static ErrorDTO getError(String errorCode, String errorMessage, String errorDescription) {

        ErrorDTO error = new ErrorDTO();
        error.setCode(errorCode);
        error.setMessage(errorMessage);
        error.setDescription(errorDescription);
        return error;
    }

    /**
     * Validates that a credential ID is not blank.
     *
     * @param credentialId Credential ID.
     * @throws CredentialMgtClientException If the credential ID is blank.
     */
    public static void validateCredentialId(String credentialId) throws CredentialMgtClientException {

        if (StringUtils.isBlank(credentialId)) {
            throw new CredentialMgtClientException(ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_ID);
        }
    }

    /**
     * Builds a CredentialsByTypeDTO by iterating over all registered credential handlers and populating
     * the response fields for each configured credential type.
     *
     * @param handlerMap Map of credential types to their handlers.
     * @param userId     User ID for whom credentials are retrieved.
     * @return Populated CredentialsByTypeDTO.
     */
    public static CredentialsByTypeDTO buildCredentialsByTypeDTO(
            Map<CredentialTypes, CredentialHandler> handlerMap, String userId) {

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
                throw handleCredentialMgtException(e);
            }
        }
        return response;
    }

    private static List<CredentialEntryDTO> toEntryDTOs(List<CredentialDTO> credentials) {

        if (credentials == null) {
            return Collections.emptyList();
        }
        return credentials.stream()
                .map(dto -> new CredentialEntryDTO()
                        .credentialId(dto.getCredentialId())
                        .displayName(dto.getDisplayName()))
                .collect(Collectors.toList());
    }
}
