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

package org.wso2.carbon.identity.rest.api.user.idv.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.idv.v1.model.ProviderProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class VerifyRequest  {
  
    private String idVProviderId;
    private List<String> claims = null;

    private List<ProviderProperty> properties = new ArrayList<>();


    /**
    **/
    public VerifyRequest idVProviderId(String idVProviderId) {

        this.idVProviderId = idVProviderId;
        return this;
    }
    
    @ApiModelProperty(example = "d75b8685-383a-4320-a555-80a3eecb5af2", value = "")
    @JsonProperty("idVProviderId")
    @Valid
    public String getIdVProviderId() {
        return idVProviderId;
    }
    public void setIdVProviderId(String idVProviderId) {
        this.idVProviderId = idVProviderId;
    }

    /**
    **/
    public VerifyRequest claims(List<String> claims) {

        this.claims = claims;
        return this;
    }
    
    @ApiModelProperty(example = "[\"http://wso2.org/claims/dob\",\"http://wso2.org/claims/country\"]", value = "")
    @JsonProperty("claims")
    @Valid
    public List<String> getClaims() {
        return claims;
    }
    public void setClaims(List<String> claims) {
        this.claims = claims;
    }

    public VerifyRequest addClaimsItem(String claimsItem) {
        if (this.claims == null) {
            this.claims = new ArrayList<>();
        }
        this.claims.add(claimsItem);
        return this;
    }

        /**
    **/
    public VerifyRequest properties(List<ProviderProperty> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("properties")
    @Valid
    @NotNull(message = "Property properties cannot be null.")

    public List<ProviderProperty> getProperties() {
        return properties;
    }
    public void setProperties(List<ProviderProperty> properties) {
        this.properties = properties;
    }

    public VerifyRequest addPropertiesItem(ProviderProperty propertiesItem) {
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
        VerifyRequest verifyRequest = (VerifyRequest) o;
        return Objects.equals(this.idVProviderId, verifyRequest.idVProviderId) &&
            Objects.equals(this.claims, verifyRequest.claims) &&
            Objects.equals(this.properties, verifyRequest.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVProviderId, claims, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class VerifyRequest {\n");
        
        sb.append("    idVProviderId: ").append(toIndentedString(idVProviderId)).append("\n");
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

