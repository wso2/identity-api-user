/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.rest.api.user.backupcode.v1.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Backup code error.
 **/
@ApiModel(description = "Backup code error.")
public class ErrorDTO {

    @Valid 
    @NotNull(message = "Property code cannot be null.") 
    private String code = null;

    @Valid 
    @NotNull(message = "Property message cannot be null.") 
    private String message = null;

    @Valid 
    private String description = null;

    @Valid 
    private String traceId = null;

    /**
     * Error code.
     **/
    @ApiModelProperty(required = true, value = "Error code.")
    @JsonProperty("code")
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Error message.
     **/
    @ApiModelProperty(required = true, value = "Error message.")
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Error description.
     **/
    @ApiModelProperty(value = "Error description.")
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Error trace id.
     **/
    @ApiModelProperty(value = "Error trace id.")
    @JsonProperty("traceId")
    public String getTraceId() {
        return traceId;
    }
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ErrorDTO {\n");
        
        sb.append("    code: ").append(code).append("\n");
        sb.append("    message: ").append(message).append("\n");
        sb.append("    description: ").append(description).append("\n");
        sb.append("    traceId: ").append(traceId).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
