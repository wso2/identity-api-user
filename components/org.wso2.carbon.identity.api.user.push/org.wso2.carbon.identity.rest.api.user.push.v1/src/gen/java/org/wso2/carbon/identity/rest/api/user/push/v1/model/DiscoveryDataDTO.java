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

package org.wso2.carbon.identity.rest.api.user.push.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Device registration information
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Device registration information")
public class DiscoveryDataDTO  {
  
    private String did;
    private String un;
    private String td;
    private String ae;
    private String re;
    private String rde;
    private String chg;

    /**
    * Device ID
    **/
    public DiscoveryDataDTO did(String did) {

        this.did = did;
        return this;
    }
    
    @ApiModelProperty(example = "b03f90c9-6723-48f6-863b-a35f1ac77f57", value = "Device ID")
    @JsonProperty("did")
    @Valid
    public String getDid() {
        return did;
    }
    public void setDid(String did) {
        this.did = did;
    }

    /**
    * Username
    **/
    public DiscoveryDataDTO un(String un) {

        this.un = un;
        return this;
    }
    
    @ApiModelProperty(example = "admin", value = "Username")
    @JsonProperty("un")
    @Valid
    public String getUn() {
        return un;
    }
    public void setUn(String un) {
        this.un = un;
    }

    /**
    * Tenant domain or Organization
    **/
    public DiscoveryDataDTO td(String td) {

        this.td = td;
        return this;
    }
    
    @ApiModelProperty(example = "carbon.super", value = "Tenant domain or Organization")
    @JsonProperty("td")
    @Valid
    public String getTd() {
        return td;
    }
    public void setTd(String td) {
        this.td = td;
    }

    /**
    * Authentication endpoint
    **/
    public DiscoveryDataDTO ae(String ae) {

        this.ae = ae;
        return this;
    }
    
    @ApiModelProperty(example = "https://localhost:9443/push-auth/authenticate", value = "Authentication endpoint")
    @JsonProperty("ae")
    @Valid
    public String getAe() {
        return ae;
    }
    public void setAe(String ae) {
        this.ae = ae;
    }

    /**
    * Registration endpoint
    **/
    public DiscoveryDataDTO re(String re) {

        this.re = re;
        return this;
    }
    
    @ApiModelProperty(example = "https://localhost:9443/api/users/v1/me/push/devices", value = "Registration endpoint")
    @JsonProperty("re")
    @Valid
    public String getRe() {
        return re;
    }
    public void setRe(String re) {
        this.re = re;
    }

    /**
    * Remove device endpoint
    **/
    public DiscoveryDataDTO rde(String rde) {

        this.rde = rde;
        return this;
    }
    
    @ApiModelProperty(example = "https://localhost:9443/api/users/v1/me/push/devices/deviceId/remove", value = "Remove device endpoint")
    @JsonProperty("rde")
    @Valid
    public String getRde() {
        return rde;
    }
    public void setRde(String rde) {
        this.rde = rde;
    }

    /**
    * Challenge
    **/
    public DiscoveryDataDTO chg(String chg) {

        this.chg = chg;
        return this;
    }
    
    @ApiModelProperty(example = "b03f90c9-6723-48f6-863b-a35f1ac77f57", value = "Challenge")
    @JsonProperty("chg")
    @Valid
    public String getChg() {
        return chg;
    }
    public void setChg(String chg) {
        this.chg = chg;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiscoveryDataDTO discoveryDataDTO = (DiscoveryDataDTO) o;
        return Objects.equals(this.did, discoveryDataDTO.did) &&
            Objects.equals(this.un, discoveryDataDTO.un) &&
            Objects.equals(this.td, discoveryDataDTO.td) &&
            Objects.equals(this.ae, discoveryDataDTO.ae) &&
            Objects.equals(this.re, discoveryDataDTO.re) &&
            Objects.equals(this.rde, discoveryDataDTO.rde) &&
            Objects.equals(this.chg, discoveryDataDTO.chg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(did, un, td, ae, re, rde, chg);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class DiscoveryDataDTO {\n");
        
        sb.append("    did: ").append(toIndentedString(did)).append("\n");
        sb.append("    un: ").append(toIndentedString(un)).append("\n");
        sb.append("    td: ").append(toIndentedString(td)).append("\n");
        sb.append("    ae: ").append(toIndentedString(ae)).append("\n");
        sb.append("    re: ").append(toIndentedString(re)).append("\n");
        sb.append("    rde: ").append(toIndentedString(rde)).append("\n");
        sb.append("    chg: ").append(toIndentedString(chg)).append("\n");
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

