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
import org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants;
import org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.user.credential.common.dto.CreatedCredentialDTO;
import org.wso2.carbon.identity.api.user.credential.common.dto.CredentialGroupDTO;
import org.wso2.carbon.identity.api.user.credential.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.api.user.credential.common.utils.CredentialManagementUtils;
import org.wso2.carbon.identity.application.authenticator.backupcode.BackupCodeAPIHandler;
import org.wso2.carbon.identity.application.authenticator.backupcode.exception.BackupCodeException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * Handler for managing backup code credentials.
 */
public class BackupCodeCredentialHandler implements CredentialHandler {

    private static final Log LOG = LogFactory.getLog(BackupCodeCredentialHandler.class);
    private static final String BACKUP_CODE_CREDENTIAL_ID = Base64.getEncoder().encodeToString(
            CredentialTypes.BACKUP_CODE.getApiValue().getBytes(StandardCharsets.UTF_8));

    @Override
    public CredentialGroupDTO getCredentials(String entityId) throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving backup code credentials for entity ID: " + entityId);
        }
        try {
            String username = CredentialManagementUtils.resolveUsernameFromUserId(entityId);
            int remainingCount = BackupCodeAPIHandler.getRemainingBackupCodesCount(username);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved backup code credential for entity ID: " + entityId);
            }
            return new CredentialGroupDTO.Builder()
                    .type(CredentialTypes.BACKUP_CODE.getApiValue())
                    .isConfigured(remainingCount > 0)
                    .isMultiValued(false)
                    .build();
        } catch (BackupCodeException e) {
            throw CredentialManagementUtils.handleServerException(
                    CredentialManagementConstants.ErrorMessages.ERROR_CODE_GET_BACKUP_CODES, e, entityId);
        }
    }

    @Override
    public void deleteCredentials(String entityId) throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting backup code credentials for entity ID: " + entityId);
        }
        try {
            String username = CredentialManagementUtils.resolveUsernameFromUserId(entityId);
            BackupCodeAPIHandler.deleteBackupCodes(username);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully deleted backup code credentials for entity ID: " + entityId);
            }
        } catch (BackupCodeException e) {
            throw CredentialManagementUtils.handleServerException(
                    CredentialManagementConstants.ErrorMessages.ERROR_CODE_DELETE_BACKUP_CODES, e, entityId);
        }
    }

    @Override
    public CreatedCredentialDTO createCredential(String entityId) throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Generating backup code credentials for entity ID: " + entityId);
        }
        try {
            String username = CredentialManagementUtils.resolveUsernameFromUserId(entityId);
            List<String> codes = BackupCodeAPIHandler.generateBackupCodes(username);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully generated backup code credentials for entity ID: " + entityId);
            }
            return new CreatedCredentialDTO.Builder()
                    .credentialId(BACKUP_CODE_CREDENTIAL_ID)
                    .credentials(codes)
                    .build();
        } catch (BackupCodeException e) {
            throw CredentialManagementUtils.handleServerException(
                    CredentialManagementConstants.ErrorMessages.ERROR_CODE_CREATE_BACKUP_CODES, e, entityId);
        }
    }
}
