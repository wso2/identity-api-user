/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.recovery.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.APICall;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ResetCodeResponse  {
  
    private String resetCode;
    private List<APICall> links = null;


    /**
    * Password reset code to reset the password
    **/
    public ResetCodeResponse resetCode(String resetCode) {

        this.resetCode = resetCode;
        return this;
    }
    
    @ApiModelProperty(example = "1234-55678-5668-2345", value = "Password reset code to reset the password")
    @JsonProperty("resetCode")
    @Valid
    public String getResetCode() {
        return resetCode;
    }
    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    /**
    * Contains available api calls
    **/
    public ResetCodeResponse links(List<APICall> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(value = "Contains available api calls")
    @JsonProperty("links")
    @Valid
    public List<APICall> getLinks() {
        return links;
    }
    public void setLinks(List<APICall> links) {
        this.links = links;
    }

    public ResetCodeResponse addLinksItem(APICall linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
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
        ResetCodeResponse resetCodeResponse = (ResetCodeResponse) o;
        return Objects.equals(this.resetCode, resetCodeResponse.resetCode) &&
            Objects.equals(this.links, resetCodeResponse.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resetCode, links);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ResetCodeResponse {\n");
        
        sb.append("    resetCode: ").append(toIndentedString(resetCode)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

