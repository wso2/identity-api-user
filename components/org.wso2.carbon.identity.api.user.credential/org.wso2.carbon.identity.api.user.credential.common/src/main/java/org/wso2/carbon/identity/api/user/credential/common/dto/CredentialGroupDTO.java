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

package org.wso2.carbon.identity.api.user.credential.common.dto;

import org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.CredentialTypes;

import java.util.Collections;
import java.util.List;

/**
 * Represents the credential state for a single credential type.
 * isMultiValued=true: credentials list contains individual entries (e.g. passkey, push-auth).
 * isMultiValued=false: only isConfigured is meaningful (e.g. backup-code).
 */
public class CredentialGroupDTO {

    private final CredentialTypes type;
    private final boolean isConfigured;
    private final boolean isMultiValued;
    private final List<CredentialDTO> credentials;

    private CredentialGroupDTO(Builder builder) {

        this.type = builder.type;
        this.isConfigured = builder.isConfigured;
        this.isMultiValued = builder.isMultiValued;
        this.credentials = builder.credentials != null
                ? Collections.unmodifiableList(builder.credentials)
                : Collections.emptyList();
    }

    public CredentialTypes getType() {

        return type;
    }

    public boolean isConfigured() {

        return isConfigured;
    }

    public boolean isMultiValued() {

        return isMultiValued;
    }

    public List<CredentialDTO> getCredentials() {

        return credentials;
    }

    /**
     * Builder for CredentialGroupDTO.
     */
    public static class Builder {

        private CredentialTypes type;
        private boolean isConfigured;
        private boolean isMultiValued;
        private List<CredentialDTO> credentials;

        public Builder type(CredentialTypes type) {

            this.type = type;
            return this;
        }

        public Builder isConfigured(boolean isConfigured) {

            this.isConfigured = isConfigured;
            return this;
        }

        public Builder isMultiValued(boolean isMultiValued) {

            this.isMultiValued = isMultiValued;
            return this;
        }

        public Builder credentials(List<CredentialDTO> credentials) {

            this.credentials = credentials;
            return this;
        }

        public CredentialGroupDTO build() {

            return new CredentialGroupDTO(this);
        }
    }
}
