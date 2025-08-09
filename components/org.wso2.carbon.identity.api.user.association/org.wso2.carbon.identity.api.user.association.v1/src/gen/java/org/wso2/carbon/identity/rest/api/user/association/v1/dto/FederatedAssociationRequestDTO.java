/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com) All Rights Reserved.
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
public class FederatedAssociationRequestDTO {

    @Valid 
    private String idp = null;

    @Valid 
    private String federatedUserId = null;

    /**
    * Name of the IdP
    **/
    @ApiModelProperty(value = "Name of the IdP")
    @JsonProperty("idp")
    public String getIdp() {
        return idp;
    }
    public void setIdp(String idp) {
        this.idp = idp;
    }

    /**
    * User identifier in the federated IdP
    **/
    @ApiModelProperty(value = "User identifier in the federated IdP")
    @JsonProperty("federatedUserId")
    public String getFederatedUserId() {
        return federatedUserId;
    }
    public void setFederatedUserId(String federatedUserId) {
        this.federatedUserId = federatedUserId;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FederatedAssociationRequestDTO {\n");
        
        sb.append("    idp: ").append(idp).append("\n");
        sb.append("    federatedUserId: ").append(federatedUserId).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
