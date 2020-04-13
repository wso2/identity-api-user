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
import java.util.UUID;


import java.util.Objects;
import javax.validation.Valid;

/**
 * .
 **/
public class DiscoveryDataDTO  {
  
    private String id;
    private String username;
    private String tennantDomain;
    private String userStoreDomain;
    private String registrationUrl;
    private String authenticationUrl;
    private UUID challenge;

    /**
    **/
    public DiscoveryDataDTO id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "dsfdsfdsfdsasdwadwadswad", value = "")
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
    public DiscoveryDataDTO username(String username) {

        this.username = username;
        return this;
    }
    
    @ApiModelProperty(example = "admin", value = "")
    @JsonProperty("username")
    @Valid
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    /**
    **/
    public DiscoveryDataDTO tennantDomain(String tennantDomain) {

        this.tennantDomain = tennantDomain;
        return this;
    }
    
    @ApiModelProperty(example = "mainDomain", value = "")
    @JsonProperty("tennantDomain")
    @Valid
    public String getTennantDomain() {
        return tennantDomain;
    }
    public void setTennantDomain(String tennantDomain) {
        this.tennantDomain = tennantDomain;
    }

    /**
    **/
    public DiscoveryDataDTO userStoreDomain(String userStoreDomain) {

        this.userStoreDomain = userStoreDomain;
        return this;
    }
    
    @ApiModelProperty(example = "secondary", value = "")
    @JsonProperty("userStoreDomain")
    @Valid
    public String getUserStoreDomain() {
        return userStoreDomain;
    }
    public void setUserStoreDomain(String userStoreDomain) {
        this.userStoreDomain = userStoreDomain;
    }

    /**
    **/
    public DiscoveryDataDTO registrationUrl(String registrationUrl) {

        this.registrationUrl = registrationUrl;
        return this;
    }
    
    @ApiModelProperty(example = "localhost:9443/t/{tenant_context}/api/users/v1/me/device/", value = "")
    @JsonProperty("registrationUrl")
    @Valid
    public String getRegistrationUrl() {
        return registrationUrl;
    }
    public void setRegistrationUrl(String registrationUrl) {
        this.registrationUrl = registrationUrl;
    }

    /**
    **/
    public DiscoveryDataDTO authenticationUrl(String authenticationUrl) {

        this.authenticationUrl = authenticationUrl;
        return this;
    }
    
    @ApiModelProperty(example = "localhost:9443/t/{tenant_context}/api/users/v1/me/device/biometric", value = "")
    @JsonProperty("authenticationUrl")
    @Valid
    public String getAuthenticationUrl() {
        return authenticationUrl;
    }
    public void setAuthenticationUrl(String authenticationUrl) {
        this.authenticationUrl = authenticationUrl;
    }

    /**
    **/
    public DiscoveryDataDTO challenge(UUID challenge) {

        this.challenge = challenge;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("challenge")
    @Valid
    public UUID getChallenge() {
        return challenge;
    }
    public void setChallenge(UUID challenge) {
        this.challenge = challenge;
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
        return Objects.equals(this.id, discoveryDataDTO.id) &&
            Objects.equals(this.username, discoveryDataDTO.username) &&
            Objects.equals(this.tennantDomain, discoveryDataDTO.tennantDomain) &&
            Objects.equals(this.userStoreDomain, discoveryDataDTO.userStoreDomain) &&
            Objects.equals(this.registrationUrl, discoveryDataDTO.registrationUrl) &&
            Objects.equals(this.authenticationUrl, discoveryDataDTO.authenticationUrl) &&
            Objects.equals(this.challenge, discoveryDataDTO.challenge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, tennantDomain, userStoreDomain, registrationUrl, authenticationUrl, challenge);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class DiscoveryDataDTO {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    tennantDomain: ").append(toIndentedString(tennantDomain)).append("\n");
        sb.append("    userStoreDomain: ").append(toIndentedString(userStoreDomain)).append("\n");
        sb.append("    registrationUrl: ").append(toIndentedString(registrationUrl)).append("\n");
        sb.append("    authenticationUrl: ").append(toIndentedString(authenticationUrl)).append("\n");
        sb.append("    challenge: ").append(toIndentedString(challenge)).append("\n");
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

