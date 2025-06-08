/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.association.v1.dto;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(description = "")
public class BulkAssociationOperationResponseStatusDTO {

    @Valid 
    private String errorDescription = null;

    @Valid 
    private String errorMessage = null;

    @Valid 
    private String errorCode = null;

    @Valid 
    private Integer statusCode = null;

    /**
    **/
    @ApiModelProperty(value = "")
    @JsonProperty("errorDescription")
    public String getErrorDescription() {
        return errorDescription;
    }
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    /**
    **/
    @ApiModelProperty(value = "")
    @JsonProperty("errorMessage")
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
    **/
    @ApiModelProperty(value = "")
    @JsonProperty("errorCode")
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
    **/
    @ApiModelProperty(value = "")
    @JsonProperty("statusCode")
    public Integer getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class BulkAssociationOperationResponseStatusDTO {\n");
        
        sb.append("    errorDescription: ").append(errorDescription).append("\n");
        sb.append("    errorMessage: ").append(errorMessage).append("\n");
        sb.append("    errorCode: ").append(errorCode).append("\n");
        sb.append("    statusCode: ").append(statusCode).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
