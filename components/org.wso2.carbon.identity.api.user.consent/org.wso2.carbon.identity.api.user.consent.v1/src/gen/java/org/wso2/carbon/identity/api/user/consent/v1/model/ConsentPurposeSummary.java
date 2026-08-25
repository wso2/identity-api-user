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

package org.wso2.carbon.identity.api.user.consent.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Minimal consented purpose information for list responses.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Minimal consented purpose information for list responses.")
public class ConsentPurposeSummary  {
  
    private String id;
    private String name;
    private String type;
    private String versionId;
    private String version;

    /**
    * UUID of the purpose.
    **/
    public ConsentPurposeSummary id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "a43f8d09-2e1f-4b3a-9f6e-3a1b2c4d5e6f", value = "UUID of the purpose.")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Name of the purpose.
    **/
    public ConsentPurposeSummary name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Marketing communications", value = "Name of the purpose.")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Purpose type classification.
    **/
    public ConsentPurposeSummary type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "Policy", value = "Purpose type classification.")
    @JsonProperty("type")
    @Valid
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    * UUID of the purpose version consented to.
    **/
    public ConsentPurposeSummary versionId(String versionId) {

        this.versionId = versionId;
        return this;
    }
    
    @ApiModelProperty(example = "b53g9e10-3f2g-5c4b-0g7f-4b2c3d5e6f7g", value = "UUID of the purpose version consented to.")
    @JsonProperty("versionId")
    @Valid
    public String getVersionId() {
        return versionId;
    }
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    /**
    * Human-readable version label.
    **/
    public ConsentPurposeSummary version(String version) {

        this.version = version;
        return this;
    }
    
    @ApiModelProperty(example = "2", value = "Human-readable version label.")
    @JsonProperty("version")
    @Valid
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConsentPurposeSummary consentPurposeSummary = (ConsentPurposeSummary) o;
        return Objects.equals(this.id, consentPurposeSummary.id) &&
            Objects.equals(this.name, consentPurposeSummary.name) &&
            Objects.equals(this.type, consentPurposeSummary.type) &&
            Objects.equals(this.versionId, consentPurposeSummary.versionId) &&
            Objects.equals(this.version, consentPurposeSummary.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, versionId, version);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConsentPurposeSummary {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    versionId: ").append(toIndentedString(versionId)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
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

