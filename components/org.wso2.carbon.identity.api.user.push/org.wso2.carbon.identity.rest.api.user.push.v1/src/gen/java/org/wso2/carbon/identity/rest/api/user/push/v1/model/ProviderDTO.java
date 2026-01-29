/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.push.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.*;

/**
 * Provider information with metadata
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Provider information with metadata")
public class ProviderDTO  {
  
    private String name;
    private Map<String, String> metadata = null;


    /**
    * Provider name
    **/
    public ProviderDTO name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "AmazonSNS", value = "Provider name")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Provider metadata as key-value pairs
    **/
    public ProviderDTO metadata(Map<String, String> metadata) {

        this.metadata = metadata;
        return this;
    }
    
    @ApiModelProperty(value = "Provider metadata as key-value pairs")
    @JsonProperty("metadata")
    @Valid
    public Map<String, String> getMetadata() {
        return metadata;
    }
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }


    public ProviderDTO putMetadataItem(String key, String metadataItem) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, metadataItem);
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
        ProviderDTO providerDTO = (ProviderDTO) o;
        return Objects.equals(this.name, providerDTO.name) &&
            Objects.equals(this.metadata, providerDTO.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, metadata);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ProviderDTO {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

