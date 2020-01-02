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
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.UserClaimDTO;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

    /**
    * Request to initate an account recovery
    **/
@ApiModel(description = "Request to initate an account recovery")
public class InitRequestDTO {

    @Valid 
    @NotNull(message = "Property claims cannot be null.") 
    private List<UserClaimDTO> claims = new ArrayList<UserClaimDTO>();

    @Valid 
    private List<PropertyDTO> properties = new ArrayList<PropertyDTO>();

    /**
    * User claims to identify the user as UserClaim objects
    **/
    @ApiModelProperty(required = true, value = "User claims to identify the user as UserClaim objects")
    @JsonProperty("claims")
    public List<UserClaimDTO> getClaims() {
        return claims;
    }
    public void setClaims(List<UserClaimDTO> claims) {
        this.claims = claims;
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
        sb.append("class InitRequestDTO {\n");
        
        sb.append("    claims: ").append(claims).append("\n");
        sb.append("    properties: ").append(properties).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
