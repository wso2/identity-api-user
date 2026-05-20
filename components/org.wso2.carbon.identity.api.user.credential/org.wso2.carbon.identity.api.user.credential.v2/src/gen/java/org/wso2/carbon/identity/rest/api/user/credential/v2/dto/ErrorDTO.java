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
 * DTO representing an API error response.
 */
public class ErrorDTO {

    private String code;
    private String message;
    private String description;

    @ApiModelProperty(example = "CM-60004",
            value = "A unique, machine-readable error code following the WSO2 standard.")
    @JsonProperty("code")
    @Valid
    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public ErrorDTO code(String code) {

        this.code = code;
        return this;
    }

    @ApiModelProperty(example = "Entity not found.", value = "A human-readable summary of the error.")
    @JsonProperty("message")
    @Valid
    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public ErrorDTO message(String message) {

        this.message = message;
        return this;
    }

    @ApiModelProperty(example = "The user specified by the user-id does not exist in the system.",
            value = "A more detailed explanation of the error.")
    @JsonProperty("description")
    @Valid
    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public ErrorDTO description(String description) {

        this.description = description;
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
        ErrorDTO errorDTO = (ErrorDTO) o;
        return Objects.equals(this.code, errorDTO.code)
                && Objects.equals(this.message, errorDTO.message)
                && Objects.equals(this.description, errorDTO.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(code, message, description);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ErrorDTO {\n");
        sb.append("  code: ").append(code).append("\n");
        sb.append("  message: ").append(message).append("\n");
        sb.append("  description: ").append(description).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
