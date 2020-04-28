/*
* Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.user.biometric.device.handler.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * A JSONPatch as defined by RFC 6902.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "A JSONPatch as defined by RFC 6902.")
public class PatchDTO  {
  
    private String operation;
    private String value;

    /**
    * The operation to be performed
    **/
    public PatchDTO operation(String operation) {

        this.operation = operation;
        return this;
    }
    
    @ApiModelProperty(example = "REPLACE", required = true, value = "The operation to be performed")
    @JsonProperty("operation")
    @Valid
    @NotNull(message = "Property operation cannot be null.")

    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
    * The value to be used within the operations
    **/
    public PatchDTO value(String value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(example = "My new Device", required = true, value = "The value to be used within the operations")
    @JsonProperty("value")
    @Valid
    @NotNull(message = "Property value cannot be null.")

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PatchDTO patchDTO = (PatchDTO) o;
        return Objects.equals(this.operation, patchDTO.operation) &&
            Objects.equals(this.value, patchDTO.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operation, value);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PatchDTO {\n");
        
        sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}

