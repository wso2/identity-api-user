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
    * Object that holds a user claim and the corresponding value
    **/
@ApiModel(description = "Object that holds a user claim and the corresponding value")
public class UserClaimDTO {

    @Valid 
    private String uri = null;

    @Valid 
    private String value = null;

    /**
    * Claim uri
    **/
    @ApiModelProperty(value = "Claim uri")
    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
    * Value for the claim
    **/
    @ApiModelProperty(value = "Value for the claim")
    @JsonProperty("value")
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserClaimDTO {\n");
        
        sb.append("    uri: ").append(uri).append("\n");
        sb.append("    value: ").append(value).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
