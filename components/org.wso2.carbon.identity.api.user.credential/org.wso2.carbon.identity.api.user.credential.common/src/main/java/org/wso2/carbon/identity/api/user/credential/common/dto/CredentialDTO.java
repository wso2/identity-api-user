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

/**
 * Represents a single enrolled credential entry.
 */
public class CredentialDTO {

    private final String credentialId;
    private final String displayName;

    private CredentialDTO(Builder builder) {

        this.credentialId = builder.credentialId;
        this.displayName = builder.displayName;
    }

    public String getCredentialId() {

        return credentialId;
    }

    public String getDisplayName() {

        return displayName;
    }

    /**
     * Builder for CredentialDTO.
     */
    public static class Builder {

        private String credentialId;
        private String displayName;

        public Builder credentialId(String credentialId) {

            this.credentialId = credentialId;
            return this;
        }

        public Builder displayName(String displayName) {

            this.displayName = displayName;
            return this;
        }

        public CredentialDTO build() {

            return new CredentialDTO(this);
        }
    }
}
