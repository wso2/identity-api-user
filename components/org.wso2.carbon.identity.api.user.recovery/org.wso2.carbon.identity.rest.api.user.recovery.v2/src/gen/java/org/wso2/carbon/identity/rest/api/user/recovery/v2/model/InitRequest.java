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
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.Property;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.UserClaim;
import javax.validation.constraints.*;

/**
 * Request to initate an account recovery
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Request to initate an account recovery")
public class InitRequest  {
  
    private List<UserClaim> claims = new ArrayList<>();

    private List<Property> properties = null;


    /**
    * User claims to identify the user as UserClaim objects
    **/
    public InitRequest claims(List<UserClaim> claims) {

        this.claims = claims;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "User claims to identify the user as UserClaim objects")
    @JsonProperty("claims")
    @Valid
    @NotNull(message = "Property claims cannot be null.")

    public List<UserClaim> getClaims() {
        return claims;
    }
    public void setClaims(List<UserClaim> claims) {
        this.claims = claims;
    }

    public InitRequest addClaimsItem(UserClaim claimsItem) {
        this.claims.add(claimsItem);
        return this;
    }

        /**
    * (OPTIONAL) Additional META properties
    **/
    public InitRequest properties(List<Property> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "(OPTIONAL) Additional META properties")
    @JsonProperty("properties")
    @Valid
    public List<Property> getProperties() {
        return properties;
    }
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public InitRequest addPropertiesItem(Property propertiesItem) {
        if (this.properties == null) {
            this.properties = new ArrayList<>();
        }
        this.properties.add(propertiesItem);
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
        InitRequest initRequest = (InitRequest) o;
        return Objects.equals(this.claims, initRequest.claims) &&
            Objects.equals(this.properties, initRequest.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(claims, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InitRequest {\n");
        
        sb.append("    claims: ").append(toIndentedString(claims)).append("\n");
        sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
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

