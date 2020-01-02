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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.PropertyDTO;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

    /**
    * Object to reset the password of a user
    **/
@ApiModel(description = "Object to reset the password of a user")
public class ResetRequestDTO {

    @Valid 
    private String resetCode = null;

    @Valid 
    private String password = null;

    @Valid 
    private List<PropertyDTO> properties = new ArrayList<PropertyDTO>();

    /**
    * resetCode given by the confim API
    **/
    @ApiModelProperty(value = "resetCode given by the confim API")
    @JsonProperty("resetCode")
    public String getResetCode() {
        return resetCode;
    }
    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    /**
    * New password given by the user
    **/
    @ApiModelProperty(value = "New password given by the user")
    @JsonProperty("password")
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /**
    * (OPTIONAL) Additional META properties
    **/
    @ApiModelProperty(value = "(OPTIONAL) Additional META properties")
    @JsonProperty("properties")
    public List<PropertyDTO> getProperties() {
        return properties;
    }
    public void setProperties(List<PropertyDTO> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ResetRequestDTO {\n");
        
        sb.append("    resetCode: ").append(resetCode).append("\n");
        sb.append("    password: ").append(password).append("\n");
        sb.append("    properties: ").append(properties).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
