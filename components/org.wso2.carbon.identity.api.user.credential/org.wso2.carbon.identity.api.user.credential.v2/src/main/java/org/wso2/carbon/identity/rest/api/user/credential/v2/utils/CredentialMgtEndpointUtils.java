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
     * Maps a CredentialMgtException to an APIError with the appropriate HTTP status.
     *
     * @param e CredentialMgtException.
     * @return APIError.
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
        if (StringUtils.isBlank(errorCode)) {
            errorCode = CredentialMgtEndpointConstants.ErrorMessages.ERROR_CODE_GET_CREDENTIALS.getCode();
        } else if (!errorCode.contains(CredentialMgtEndpointConstants.ERROR_CODE_DELIMITER)) {
            errorCode = CredentialMgtEndpointConstants.CREDENTIAL_MGT_PREFIX + errorCode;
        }

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
     * Validates that the given credential type string is a known type.
     *
     * @param value Credential type string.
     * @throws CredentialMgtClientException If the type is unrecognised.
     */
    public static void validateCredentialType(String value) throws CredentialMgtClientException {

        if (CredentialTypes.fromString(value) == null) {
            throw new CredentialMgtClientException(CredentialManagementConstants
                    .ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_TYPE);
        }
    }

    /**
     * Validates that the given credential type supports admin-initiated creation.
     *
     * @param value Credential type string.
     * @throws CredentialMgtClientException If the type is invalid or does not support creation.
     */
    public static void validateCreatableCredentialType(String value) throws CredentialMgtClientException {

        CredentialTypes type = CredentialTypes.fromString(value);
        if (type == null) {
            throw new CredentialMgtClientException(CredentialManagementConstants
                    .ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_TYPE);
        }
        if (!type.isCreatable()) {
            throw CredentialManagementUtils.handleClientException(
                    CredentialManagementConstants.ErrorMessages.ERROR_CODE_CREDENTIAL_CREATION_NOT_SUPPORTED,
                    null, value);
        }
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
