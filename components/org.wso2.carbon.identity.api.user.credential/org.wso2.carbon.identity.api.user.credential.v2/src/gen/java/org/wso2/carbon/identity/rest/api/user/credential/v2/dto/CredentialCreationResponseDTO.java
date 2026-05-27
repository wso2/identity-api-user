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
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;

/**
 * Response DTO for credential creation.
 */
public class CredentialCreationResponseDTO {

    private String type;
    private List<String> credentials = new ArrayList<>();

    @ApiModelProperty(example = "backup-code", value = "The type of the created credential.")
    @JsonProperty("type")
    @Valid
    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public CredentialCreationResponseDTO type(String type) {

        this.type = type;
        return this;
    }

    @ApiModelProperty(example = "[abc123, def456, ghi789]", value = "The generated credential values.")
    @JsonProperty("credentials")
    @Valid
    public List<String> getCredentials() {

        return credentials;
    }

    public void setCredentials(List<String> credentials) {

        this.credentials = credentials;
    }

    public CredentialCreationResponseDTO credentials(List<String> credentials) {

        this.credentials = credentials;
        return this;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CredentialCreationResponseDTO that = (CredentialCreationResponseDTO) o;
        return Objects.equals(type, that.type)
                && Objects.equals(credentials, that.credentials);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, credentials);
    }

    @Override
    public String toString() {

        return "CredentialCreationResponseDTO{type=" + type + ", credentials=[redacted]}";
    }
}
