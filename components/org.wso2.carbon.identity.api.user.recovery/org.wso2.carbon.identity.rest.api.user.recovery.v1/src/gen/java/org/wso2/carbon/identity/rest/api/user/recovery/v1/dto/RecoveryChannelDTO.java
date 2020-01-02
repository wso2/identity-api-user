/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.recovery.v1.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

    /**
    * Object with notification channel attributes
    **/
@ApiModel(description = "Object with notification channel attributes")
public class RecoveryChannelDTO {

    @Valid 
    private String id = null;

    @Valid 
    private String type = null;

    @Valid 
    private String value = null;

    @Valid 
    private Boolean preferred = null;

    /**
    * Id given to the channel
    **/
    @ApiModelProperty(value = "Id given to the channel")
    @JsonProperty("id")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Type of the chanel
    **/
    @ApiModelProperty(value = "Type of the chanel")
    @JsonProperty("type")
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    * Masked channel value
    **/
    @ApiModelProperty(value = "Masked channel value")
    @JsonProperty("value")
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    /**
    * Whether the channel is a user preferred channel
    **/
    @ApiModelProperty(value = "Whether the channel is a user preferred channel")
    @JsonProperty("preferred")
    public Boolean getPreferred() {
        return preferred;
    }
    public void setPreferred(Boolean preferred) {
        this.preferred = preferred;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RecoveryChannelDTO {\n");
        
        sb.append("    id: ").append(id).append("\n");
        sb.append("    type: ").append(type).append("\n");
        sb.append("    value: ").append(value).append("\n");
        sb.append("    preferred: ").append(preferred).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
