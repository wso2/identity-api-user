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

package org.wso2.carbon.identity.api.user.credential.common;

import org.wso2.carbon.identity.api.user.credential.common.dto.CreatedCredentialDTO;
import org.wso2.carbon.identity.api.user.credential.common.dto.CredentialGroupDTO;
import org.wso2.carbon.identity.api.user.credential.common.exception.CredentialMgtClientException;
import org.wso2.carbon.identity.api.user.credential.common.exception.CredentialMgtException;

/**
 * Credential Management Handler interface.
 */
public interface CredentialHandler {

    /**
     * Retrieves credentials for a given entity grouped as a single typed result.
     */
    CredentialGroupDTO getCredentials(String entityId) throws CredentialMgtException;

    /**
     * Deletes a specific credential by ID for an entity.
     * Handlers that do not support per-credential deletion should leave this as the default.
     */
    default void deleteCredentialById(String entityId, String credentialId) throws CredentialMgtException {

        throw new CredentialMgtClientException(
                CredentialManagementConstants.ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_TYPE);
    }

    /**
     * Deletes all credentials for an entity.
     * Handlers that do not support bulk deletion should leave this as the default.
     */
    default void deleteCredentials(String entityId) throws CredentialMgtException {

        throw new CredentialMgtClientException(
                CredentialManagementConstants.ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_TYPE);
    }

    /**
     * Creates credentials for an entity.
     * Handlers that do not support creation should leave this as the default.
     */
    default CreatedCredentialDTO createCredential(String entityId) throws CredentialMgtException {

        throw new CredentialMgtClientException(
                CredentialManagementConstants.ErrorMessages.ERROR_CODE_CREDENTIAL_CREATION_NOT_SUPPORTED);
    }
}
