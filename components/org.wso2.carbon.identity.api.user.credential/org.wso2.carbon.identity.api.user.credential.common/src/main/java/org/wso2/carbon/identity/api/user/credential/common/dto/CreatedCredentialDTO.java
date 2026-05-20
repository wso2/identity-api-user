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

import java.util.List;

/**
 * Represents the result of a credential creation operation.
 * credentialId is the backend-assigned identifier for the created credential.
 * credentials contains the generated credential values (e.g. backup codes).
 */
public class CreatedCredentialDTO {

    private final String credentialId;
    private final List<String> credentials;

    private CreatedCredentialDTO(Builder builder) {

        this.credentialId = builder.credentialId;
        this.credentials = builder.credentials;
    }

    public String getCredentialId() {

        return credentialId;
    }

    public List<String> getCredentials() {

        return credentials;
    }

    /**
     * Builder for CreatedCredentialDTO.
     */
    public static class Builder {

        private String credentialId;
        private List<String> credentials;

        public Builder credentialId(String credentialId) {

            this.credentialId = credentialId;
            return this;
        }

        public Builder credentials(List<String> credentials) {

            this.credentials = credentials;
            return this;
        }

        public CreatedCredentialDTO build() {

            return new CreatedCredentialDTO(this);
        }
    }
}
