/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.recovery.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Object with notification channel attributes
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Object with notification channel attributes")
public class RecoveryChannel  {
  
    private String id;
    private String type;
    private String value;
    private Boolean preferred;

    /**
    * Id given to the channel
    **/
    public RecoveryChannel id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "1", value = "Id given to the channel")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Type of the chanel
    **/
    public RecoveryChannel type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "EMAIL", value = "Type of the chanel")
    @JsonProperty("type")
    @Valid
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    * Masked channel value
    **/
    public RecoveryChannel value(String value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(example = "wso2***********.com", value = "Masked channel value")
    @JsonProperty("value")
    @Valid
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    /**
    * Whether the channel is a user preferred channel
    **/
    public RecoveryChannel preferred(Boolean preferred) {

        this.preferred = preferred;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Whether the channel is a user preferred channel")
    @JsonProperty("preferred")
    @Valid
    public Boolean getPreferred() {
        return preferred;
    }
    public void setPreferred(Boolean preferred) {
        this.preferred = preferred;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecoveryChannel recoveryChannel = (RecoveryChannel) o;
        return Objects.equals(this.id, recoveryChannel.id) &&
            Objects.equals(this.type, recoveryChannel.type) &&
            Objects.equals(this.value, recoveryChannel.value) &&
            Objects.equals(this.preferred, recoveryChannel.preferred);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, value, preferred);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RecoveryChannel {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("    preferred: ").append(toIndentedString(preferred)).append("\n");
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

