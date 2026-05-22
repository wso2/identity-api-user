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

package org.wso2.carbon.identity.rest.api.user.credential.v2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Response model for GET /{user-id}/credentials.
 */
public class CredentialsByTypeDTO {

    private List<CredentialEntryDTO> passkey = new ArrayList<>();

    @JsonProperty("push-auth")
    private List<CredentialEntryDTO> pushAuth = new ArrayList<>();

    @JsonProperty("backup-code")
    private boolean backupCode = false;

    public List<CredentialEntryDTO> getPasskey() {

        return passkey;
    }

    public CredentialsByTypeDTO passkey(List<CredentialEntryDTO> passkey) {

        this.passkey = passkey;
        return this;
    }

    public List<CredentialEntryDTO> getPushAuth() {

        return pushAuth;
    }

    public CredentialsByTypeDTO pushAuth(List<CredentialEntryDTO> pushAuth) {

        this.pushAuth = pushAuth;
        return this;
    }

    public boolean isBackupCode() {

        return backupCode;
    }

    public CredentialsByTypeDTO backupCode(boolean backupCode) {

        this.backupCode = backupCode;
        return this;
    }
}
