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

import java.util.Objects;
import javax.validation.Valid;

/**
 * DTO representing a single credential entry in the grouped response.
 */
public class CredentialEntryDTO {

    private String credentialId;
    private String displayName;

    @ApiModelProperty(example = "a5a81c76-27a3-42d4-82a8-55285d82a4a1",
            value = "The unique identifier for the credential.")
    @JsonProperty("credentialId")
    @Valid
    public String getCredentialId() {

        return credentialId;
    }

    public void setCredentialId(String credentialId) {

        this.credentialId = credentialId;
    }

    public CredentialEntryDTO credentialId(String credentialId) {

        this.credentialId = credentialId;
        return this;
    }

    @ApiModelProperty(example = "YubiKey 5C", value = "A user-friendly name for the credential.")
    @JsonProperty("displayName")
    @Valid
    public String getDisplayName() {

        return displayName;
    }

    public void setDisplayName(String displayName) {

        this.displayName = displayName;
    }

    public CredentialEntryDTO displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CredentialEntryDTO that = (CredentialEntryDTO) o;
        return Objects.equals(this.credentialId, that.credentialId)
                && Objects.equals(this.displayName, that.displayName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(credentialId, displayName);
    }

    @Override
    public String toString() {

        return "CredentialEntryDTO{credentialId=" + credentialId + ", displayName=" + displayName + "}";
    }
}
