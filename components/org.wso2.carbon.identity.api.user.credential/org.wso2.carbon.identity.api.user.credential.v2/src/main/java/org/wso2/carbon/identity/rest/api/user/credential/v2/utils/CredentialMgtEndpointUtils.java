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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorDTO;
import org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants;
import org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.user.credential.common.exception.CredentialMgtClientException;
import org.wso2.carbon.identity.api.user.credential.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.api.user.credential.common.utils.CredentialManagementUtils;
import org.wso2.carbon.identity.rest.api.user.credential.v2.constants.CredentialMgtEndpointConstants;

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
                    CredentialManagementConstants.ErrorMessages.ERROR_CODE_ENTITY_NOT_FOUND.getCode())) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else {
            LOG.error(e.getMessage(), e);
        }

        String errorCode = e.getErrorCode();

        String description = StringUtils.isNotBlank(e.getDescription()) ? e.getDescription() : e.getMessage();
        return handleException(status, errorCode, e.getMessage(), description);
    }

    /**
     * Constructs an APIError from the given HTTP status and error details.
     *
     * @param status      HTTP response status.
     * @param errorCode   Machine-readable error code.
     * @param message     Human-readable error message.
     * @param description Detailed error description.
     * @return APIError.
     */
    public static APIError handleException(Response.Status status, String errorCode,
                                           String message, String description) {

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
     * Validates that the given credential type string is a known type and returns the resolved value.
     *
     * @param value Credential type string.
     * @return Resolved credential type.
     * @throws CredentialMgtClientException If the type is unrecognised.
     */
    public static CredentialTypes validateType(String value) throws CredentialMgtClientException {

        return resolveType(value);
    }

    /**
     * Validates that the given credential type supports admin-initiated creation.
     *
     * @param value Credential type string.
     * @return Resolved credential type.
     * @throws CredentialMgtClientException If the type is unrecognized or does not support creation.
     */
    public static CredentialTypes validateCreatable(String value) throws CredentialMgtClientException {

        CredentialTypes type = resolveType(value);
        if (!CredentialMgtEndpointConstants.CREATABLE_TYPES.contains(type)) {
            throw CredentialManagementUtils.handleClientException(
                    CredentialManagementConstants.ErrorMessages.ERROR_CODE_CREDENTIAL_CREATION_NOT_SUPPORTED,
                    null, value);
        }
        return type;
    }

    /**
     * Validates that the given credential type supports bulk deletion by type.
     *
     * @param value Credential type string.
     * @return Resolved credential type.
     * @throws CredentialMgtClientException If the type is unrecognised or does not support deletion by type.
     */
    public static CredentialTypes validateDeletableByType(String value) throws CredentialMgtClientException {

        CredentialTypes type = resolveType(value);
        if (!CredentialMgtEndpointConstants.DELETABLE_BY_TYPE_TYPES.contains(type)) {
            throw CredentialManagementUtils.handleClientException(
                    CredentialManagementConstants.ErrorMessages.ERROR_CODE_CREDENTIAL_DELETION_BY_TYPE_NOT_SUPPORTED,
                    null, value);
        }
        return type;
    }

    private static CredentialTypes resolveType(String value) throws CredentialMgtClientException {

        CredentialTypes type = CredentialTypes.fromString(value);
        if (type == null) {
            throw new CredentialMgtClientException(CredentialManagementConstants
                    .ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_TYPE);
        }
        return type;
    }

    /**
     * Validates that a credential ID is not blank.
     *
     * @param credentialId Credential ID.
     * @throws CredentialMgtClientException If the credential ID is blank.
     */
    public static void validateCredentialId(String credentialId) throws CredentialMgtClientException {

        if (StringUtils.isBlank(credentialId)) {
            throw new CredentialMgtClientException(CredentialManagementConstants
                    .ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_ID);
        }
    }
}
