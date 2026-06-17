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


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PIICategorySummary  {
  
    private String piiCategoryId;
    private String piiCategoryName;

    /**
    * UUID of the PII category.
    **/
    public PIICategorySummary piiCategoryId(String piiCategoryId) {

        this.piiCategoryId = piiCategoryId;
        return this;
    }
    
    @ApiModelProperty(example = "c64h0f21-4g3h-6d5c-1h8g-5c3d4e6f7g8h", value = "UUID of the PII category.")
    @JsonProperty("piiCategoryId")
    @Valid
    public String getPiiCategoryId() {
        return piiCategoryId;
    }
    public void setPiiCategoryId(String piiCategoryId) {
        this.piiCategoryId = piiCategoryId;
    }

    /**
    * Name of the PII category.
    **/
    public PIICategorySummary piiCategoryName(String piiCategoryName) {

        this.piiCategoryName = piiCategoryName;
        return this;
    }
    
    @ApiModelProperty(example = "Email Address", value = "Name of the PII category.")
    @JsonProperty("piiCategoryName")
    @Valid
    public String getPiiCategoryName() {
        return piiCategoryName;
    }
    public void setPiiCategoryName(String piiCategoryName) {
        this.piiCategoryName = piiCategoryName;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PIICategorySummary piICategorySummary = (PIICategorySummary) o;
        return Objects.equals(this.piiCategoryId, piICategorySummary.piiCategoryId) &&
            Objects.equals(this.piiCategoryName, piICategorySummary.piiCategoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piiCategoryId, piiCategoryName);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PIICategorySummary {\n");
        
        sb.append("    piiCategoryId: ").append(toIndentedString(piiCategoryId)).append("\n");
        sb.append("    piiCategoryName: ").append(toIndentedString(piiCategoryName)).append("\n");
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

