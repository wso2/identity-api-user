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

package org.wso2.carbon.identity.rest.api.user.application.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.net.URI;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ApplicationResponse  {
  
    private String id;
    private String name;
    private String description;
    private URI image;
    private URI accessUrl;

    /**
    **/
    public ApplicationResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "85e3f4b8-0d22-4181-b1e3-1651f71b88bd", value = "")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    **/
    public ApplicationResponse name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Salesforce", value = "")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public ApplicationResponse description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Customer relationship management application", value = "")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    **/
    public ApplicationResponse image(URI image) {

        this.image = image;
        return this;
    }
    
    @ApiModelProperty(example = "https://example.com/logo/mysalesforce-logo.png", value = "")
    @JsonProperty("image")
    @Valid
    public URI getImage() {
        return image;
    }
    public void setImage(URI image) {
        this.image = image;
    }

    /**
    **/
    public ApplicationResponse accessUrl(URI accessUrl) {

        this.accessUrl = accessUrl;
        return this;
    }
    
    @ApiModelProperty(example = "https://example.my.salesforce.com/", value = "")
    @JsonProperty("accessUrl")
    @Valid
    public URI getAccessUrl() {
        return accessUrl;
    }
    public void setAccessUrl(URI accessUrl) {
        this.accessUrl = accessUrl;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplicationResponse applicationResponse = (ApplicationResponse) o;
        return Objects.equals(this.id, applicationResponse.id) &&
            Objects.equals(this.name, applicationResponse.name) &&
            Objects.equals(this.description, applicationResponse.description) &&
            Objects.equals(this.image, applicationResponse.image) &&
            Objects.equals(this.accessUrl, applicationResponse.accessUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, image, accessUrl);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ApplicationResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    accessUrl: ").append(toIndentedString(accessUrl)).append("\n");
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

