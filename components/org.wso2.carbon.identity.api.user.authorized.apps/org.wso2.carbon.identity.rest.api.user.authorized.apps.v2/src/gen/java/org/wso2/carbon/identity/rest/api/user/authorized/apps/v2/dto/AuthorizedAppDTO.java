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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AuthorizedAppDTO  {
  
    private String id;
    private String name;
    private String clientId;
    private List<String> approvedScopes = new ArrayList<>();


    /**
    * Unique Id of the application.
    **/
    public AuthorizedAppDTO id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "298c5fd8-01ac-4ada-bc10-1ce37f32140", required = true, value = "Unique Id of the application.")
    @JsonProperty("id")
    @Valid
    @NotNull(message = "Property id cannot be null.")

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Name of the application
    **/
    public AuthorizedAppDTO name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "FooApp", value = "Name of the application")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Client Id of the application.
    **/
    public AuthorizedAppDTO clientId(String clientId) {

        this.clientId = clientId;
        return this;
    }
    
    @ApiModelProperty(example = "7y7zPj4wDX3nRtfPKrmt8Auke44a", required = true, value = "Client Id of the application.")
    @JsonProperty("clientId")
    @Valid
    @NotNull(message = "Property clientId cannot be null.")

    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
    * Approved scopes
    **/
    public AuthorizedAppDTO approvedScopes(List<String> approvedScopes) {

        this.approvedScopes = approvedScopes;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Approved scopes")
    @JsonProperty("approvedScopes")
    @Valid
    @NotNull(message = "Property approvedScopes cannot be null.")

    public List<String> getApprovedScopes() {
        return approvedScopes;
    }
    public void setApprovedScopes(List<String> approvedScopes) {
        this.approvedScopes = approvedScopes;
    }

    public AuthorizedAppDTO addApprovedScopesItem(String approvedScopesItem) {
        this.approvedScopes.add(approvedScopesItem);
        return this;
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthorizedAppDTO authorizedAppDTO = (AuthorizedAppDTO) o;
        return Objects.equals(this.id, authorizedAppDTO.id) &&
            Objects.equals(this.name, authorizedAppDTO.name) &&
            Objects.equals(this.clientId, authorizedAppDTO.clientId) &&
            Objects.equals(this.approvedScopes, authorizedAppDTO.approvedScopes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, clientId, approvedScopes);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthorizedAppDTO {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
        sb.append("    approvedScopes: ").append(toIndentedString(approvedScopes)).append("\n");
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

