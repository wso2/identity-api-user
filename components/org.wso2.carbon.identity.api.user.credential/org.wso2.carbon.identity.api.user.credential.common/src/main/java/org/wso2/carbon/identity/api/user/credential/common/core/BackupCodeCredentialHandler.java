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
import org.wso2.carbon.identity.api.user.credential.common.dto.CreatedCredentialDTO;
import org.wso2.carbon.identity.api.user.credential.common.dto.CredentialGroupDTO;
import org.wso2.carbon.identity.api.user.credential.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.api.user.credential.common.utils.CredentialManagementUtils;
import org.wso2.carbon.identity.application.authenticator.backupcode.BackupCodeAPIHandler;
import org.wso2.carbon.identity.application.authenticator.backupcode.exception.BackupCodeClientException;
import org.wso2.carbon.identity.application.authenticator.backupcode.exception.BackupCodeException;

import java.util.List;

import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.CredentialTypes.BACKUP_CODE;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_BACKUP_CODE_MGT_FAILURE;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_GET_BACKUP_CODES_FAILURE;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_GET_CREDENTIALS_CLIENT_FAILURE;
import static org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_UPDATE_BACKUP_CODES_FAILURE;

/**
 * Handler for managing backup code credentials.
 */
public class BackupCodeCredentialHandler implements CredentialHandler {

    private static final Log LOG = LogFactory.getLog(BackupCodeCredentialHandler.class);

    @Override
    public CredentialGroupDTO getCredentials(String userId) throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving backup code credentials for user id: " + userId);
        }
        try {
            int remainingCount = BackupCodeAPIHandler.getRemainingBackupCodesCountByUserId(userId);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved backup code count for user id: " + userId);
            }
            return new CredentialGroupDTO.Builder()
                    .type(BACKUP_CODE)
                    .isConfigured(remainingCount > 0)
                    .build();
        } catch (BackupCodeClientException e) {
            throw CredentialManagementUtils.handleClientException(ERROR_CODE_GET_CREDENTIALS_CLIENT_FAILURE, e,
                    BACKUP_CODE.apiValue(), userId);
        } catch (BackupCodeException e) {
            throw CredentialManagementUtils.handleServerException(ERROR_CODE_GET_BACKUP_CODES_FAILURE, e, userId);
        }
    }

    @Override
    public void deleteCredentials(String userId) throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting backup code credentials for user ID: " + userId);
        }
        try {
            BackupCodeAPIHandler.deleteBackupCodesByUserId(userId);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully deleted backup code credentials for user ID: " + userId);
            }
        } catch (BackupCodeClientException e) {
            throw CredentialManagementUtils.handleClientException(ERROR_CODE_BACKUP_CODE_MGT_FAILURE, e, userId);
        } catch (BackupCodeException e) {
            throw CredentialManagementUtils.handleServerException(ERROR_CODE_UPDATE_BACKUP_CODES_FAILURE, e, userId);
        }
    }

    @Override
    public CreatedCredentialDTO createCredential(String userId) throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Generating backup code credentials for user ID: " + userId);
        }
        try {
            List<String> codes = BackupCodeAPIHandler.generateBackupCodesByUserId(userId);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully generated backup code credentials for user ID: " + userId);
            }
            return new CreatedCredentialDTO.Builder()
                    .credentials(codes)
                    .build();
        } catch (BackupCodeClientException e) {
            throw CredentialManagementUtils.handleClientException(ERROR_CODE_BACKUP_CODE_MGT_FAILURE, e, userId);
        } catch (BackupCodeException e) {
            throw CredentialManagementUtils.handleServerException(ERROR_CODE_UPDATE_BACKUP_CODES_FAILURE, e, userId);
        }
    }
}
