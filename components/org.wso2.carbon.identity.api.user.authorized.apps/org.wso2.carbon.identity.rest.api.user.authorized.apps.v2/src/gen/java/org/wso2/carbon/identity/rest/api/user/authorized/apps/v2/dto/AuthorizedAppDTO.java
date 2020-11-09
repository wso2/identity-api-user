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

package org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.dto;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(description = "")
public class AuthorizedAppDTO {

    @Valid 
    @NotNull(message = "Property id cannot be null.") 
    private String id = null;

    @Valid 
    private String name = null;

    @Valid 
    @NotNull(message = "Property clientId cannot be null.") 
    private String clientId = null;

    /**
    * Unique Id of the application.
    **/
    @ApiModelProperty(required = true, value = "Unique Id of the application.")
    @JsonProperty("id")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Name of the application
    **/
    @ApiModelProperty(value = "Name of the application")
    @JsonProperty("name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Client Id of the application.
    **/
    @ApiModelProperty(required = true, value = "Client Id of the application.")
    @JsonProperty("clientId")
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthorizedAppDTO {\n");
        
        sb.append("    id: ").append(id).append("\n");
        sb.append("    name: ").append(name).append("\n");
        sb.append("    clientId: ").append(clientId).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
