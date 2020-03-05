/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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
 *
 */

package org.wso2.carbon.identity.api.user.biometric.device.handler.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
import javax.validation.Valid;
/**
 * .
 **/
public class InlineResponse201  {
  
    private String link;

    /**
    * Link of the created resource.
    **/
    public InlineResponse201 link(String link) {

        this.link = link;
        return this;
    }
    
    @ApiModelProperty(example = " https://localhost:9443/t/{tenant_context}/api/users/v1/me/biometricDevice/dev123", value = "Link of the created resource.")
    @JsonProperty("link")
    @Valid
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InlineResponse201 inlineResponse201 = (InlineResponse201) o;
        return Objects.equals(this.link, inlineResponse201.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InlineResponse201 {\n");
        
        sb.append("    link: ").append(toIndentedString(link)).append("\n");
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

