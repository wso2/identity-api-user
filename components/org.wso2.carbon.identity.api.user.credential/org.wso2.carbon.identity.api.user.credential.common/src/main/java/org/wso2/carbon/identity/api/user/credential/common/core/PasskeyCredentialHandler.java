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

package org.wso2.carbon.identity.api.user.credential.common.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.credential.common.CredentialHandler;
import org.wso2.carbon.identity.api.user.credential.common.CredentialManagementServiceDataHolder;
import org.wso2.carbon.identity.api.user.credential.common.dto.CredentialDTO;
import org.wso2.carbon.identity.api.user.credential.common.dto.CredentialGroupDTO;
import org.wso2.carbon.identity.api.user.credential.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.api.user.credential.common.utils.CredentialManagementUtils;
import org.wso2.carbon.identity.application.authenticator.fido2.core.WebAuthnService;
import org.wso2.carbon.identity.application.authenticator.fido2.dto.FIDO2CredentialRegistration;
import org.wso2.carbon.identity.application.authenticator.fido2.exception.FIDO2AuthenticatorClientException;
import org.wso2.carbon.identity.application.authenticator.fido2.exception.FIDO2AuthenticatorServerException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.CredentialTypes.PASSKEY;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_DELETE_PASSKEYS_CLIENT_FAILURE;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_DELETE_PASSKEYS_FAILURE;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_GET_PASSKEYS_FAILURE;

/**
 * Handler for managing passkey (FIDO2/WebAuthn) credentials.
 */
public class PasskeyCredentialHandler implements CredentialHandler {

    private static final Log LOG = LogFactory.getLog(PasskeyCredentialHandler.class);

    private final WebAuthnService webAuthnService;

    public PasskeyCredentialHandler() {

        this.webAuthnService = CredentialManagementServiceDataHolder.getWebAuthnService();
    }

    @Override
    public CredentialGroupDTO getCredentials(String userId) throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving passkey credentials for user ID: " + userId);
        }
        try {
            String username = CredentialManagementUtils.resolveUsernameFromUserId(userId).toFullQualifiedUsername();
            Collection<FIDO2CredentialRegistration> passkeyCredentials = webAuthnService
                    .getFIDO2DeviceMetaData(username);

            List<CredentialDTO> credentialDTOs = new ArrayList<>();
            for (FIDO2CredentialRegistration credential : passkeyCredentials) {
                credentialDTOs.add(mapPasskeyToCredentialDTO(credential));
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved " + credentialDTOs.size() + " passkey credentials for user ID: "
                        + userId);
            }
            return new CredentialGroupDTO.Builder()
                    .type(PASSKEY)
                    .isConfigured(!credentialDTOs.isEmpty())
                    .credentials(credentialDTOs)
                    .build();
        } catch (FIDO2AuthenticatorServerException e) {
            throw CredentialManagementUtils.handleServerException(ERROR_CODE_GET_PASSKEYS_FAILURE, e, userId);
        }
    }

    @Override
    public void deleteCredentialById(String userId, String credentialId)
            throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting passkey credential for user ID: " + userId);
        }
        try {
            String username = CredentialManagementUtils.resolveUsernameFromUserId(userId).toFullQualifiedUsername();
            webAuthnService.deregisterFIDO2Credential(credentialId, username);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully deleted passkey credential for user ID: " + userId);
            }
        } catch (FIDO2AuthenticatorClientException e) {
            throw CredentialManagementUtils.handleClientException(
                    ERROR_CODE_DELETE_PASSKEYS_CLIENT_FAILURE, e, credentialId, userId);
        } catch (FIDO2AuthenticatorServerException e) {
            throw CredentialManagementUtils.handleServerException(
                    ERROR_CODE_DELETE_PASSKEYS_FAILURE, e, credentialId, userId);
        }
    }

    private CredentialDTO mapPasskeyToCredentialDTO(FIDO2CredentialRegistration credential) {

        return new CredentialDTO.Builder()
                .credentialId(credential.getCredential().getCredentialId().getBase64Url())
                .displayName(credential.getDisplayName())
                .build();
    }
}
