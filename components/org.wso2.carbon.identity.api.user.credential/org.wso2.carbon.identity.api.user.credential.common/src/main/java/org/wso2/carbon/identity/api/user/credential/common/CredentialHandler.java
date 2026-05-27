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
     * Retrieves credentials for a given user grouped as a single typed result.
     */
    CredentialGroupDTO getCredentials(String userId) throws CredentialMgtException;

    /**
     * Deletes a specific credential by ID for a user.
     * Handlers that do not support per-credential deletion should leave this as the default.
     */
    default void deleteCredentialById(String userId, String credentialId) throws CredentialMgtException {

        throw new CredentialMgtClientException(
                CredentialManagementConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_OPERATION);
    }

    /**
     * Deletes all credentials for a user.
     * Handlers that do not support bulk deletion should leave this as the default.
     */
    default void deleteCredentials(String userId) throws CredentialMgtException {

        throw new CredentialMgtClientException(
                CredentialManagementConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_OPERATION);
    }

    /**
     * Creates credentials for a user.
     * Handlers that do not support creation should leave this as the default.
     */
    default CreatedCredentialDTO createCredential(String userId) throws CredentialMgtException {

        throw new CredentialMgtClientException(
                CredentialManagementConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_OPERATION);
    }
}
