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

package org.wso2.carbon.identity.rest.api.user.recovery.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Object that holds a user claim and the corresponding value
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Object that holds a user claim and the corresponding value")
public class UserClaim  {
  
    private String uri;
    private String value;

    /**
    * Claim uri
    **/
    public UserClaim uri(String uri) {

        this.uri = uri;
        return this;
    }
    
    @ApiModelProperty(example = "http://wso2.org/claims/givenname", value = "Claim uri")
    @JsonProperty("uri")
    @Valid
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
    * Value for the claim
    **/
    public UserClaim value(String value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(example = "user1", value = "Value for the claim")
    @JsonProperty("value")
    @Valid
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserClaim userClaim = (UserClaim) o;
        return Objects.equals(this.uri, userClaim.uri) &&
            Objects.equals(this.value, userClaim.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, value);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserClaim {\n");
        
        sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

