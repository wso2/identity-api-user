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
  
    private String deviceId;
    private String username;
    private String host;
    private String tenantDomain;
    private String tenantPath;
    private String organizationId;
    private String organizationName;
    private String organizationPath;
    private String challenge;

    /**
    * Device ID
    **/
    public DiscoveryDataDTO deviceId(String deviceId) {

        this.deviceId = deviceId;
        return this;
    }
    
    @ApiModelProperty(example = "b03f90c9-6723-48f6-863b-a35f1ac77f57", value = "Device ID")
    @JsonProperty("deviceId")
    @Valid
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
    * Username
    **/
    public DiscoveryDataDTO username(String username) {

        this.username = username;
        return this;
    }
    
    @ApiModelProperty(example = "admin", value = "Username")
    @JsonProperty("username")
    @Valid
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    /**
    * Host
    **/
    public DiscoveryDataDTO host(String host) {

        this.host = host;
        return this;
    }
    
    @ApiModelProperty(example = "https://localhost:9443", value = "Host")
    @JsonProperty("host")
    @Valid
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }

    /**
    * Tenant domain
    **/
    public DiscoveryDataDTO tenantDomain(String tenantDomain) {

        this.tenantDomain = tenantDomain;
        return this;
    }
    
    @ApiModelProperty(example = "carbon.super", value = "Tenant domain")
    @JsonProperty("tenantDomain")
    @Valid
    public String getTenantDomain() {
        return tenantDomain;
    }
    public void setTenantDomain(String tenantDomain) {
        this.tenantDomain = tenantDomain;
    }

    /**
    * Tenanted path
    **/
    public DiscoveryDataDTO tenantPath(String tenantPath) {

        this.tenantPath = tenantPath;
        return this;
    }
    
    @ApiModelProperty(example = "/t/carbon.super", value = "Tenanted path")
    @JsonProperty("tenantPath")
    @Valid
    public String getTenantPath() {
        return tenantPath;
    }
    public void setTenantPath(String tenantPath) {
        this.tenantPath = tenantPath;
    }

    /**
    * Organization ID
    **/
    public DiscoveryDataDTO organizationId(String organizationId) {

        this.organizationId = organizationId;
        return this;
    }
    
    @ApiModelProperty(example = "b03f90c9-6723-48f6-863b-a35f1ac77f57", value = "Organization ID")
    @JsonProperty("organizationId")
    @Valid
    public String getOrganizationId() {
        return organizationId;
    }
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
    * Organization name
    **/
    public DiscoveryDataDTO organizationName(String organizationName) {

        this.organizationName = organizationName;
        return this;
    }
    
    @ApiModelProperty(example = "sampleOrg", value = "Organization name")
    @JsonProperty("organizationName")
    @Valid
    public String getOrganizationName() {
        return organizationName;
    }
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
    * Organization path
    **/
    public DiscoveryDataDTO organizationPath(String organizationPath) {

        this.organizationPath = organizationPath;
        return this;
    }
    
    @ApiModelProperty(example = "/o/sampleOrg", value = "Organization path")
    @JsonProperty("organizationPath")
    @Valid
    public String getOrganizationPath() {
        return organizationPath;
    }
    public void setOrganizationPath(String organizationPath) {
        this.organizationPath = organizationPath;
    }

    /**
    * Challenge
    **/
    public DiscoveryDataDTO challenge(String challenge) {

        this.challenge = challenge;
        return this;
    }
    
    @ApiModelProperty(example = "b03f90c9-6723-48f6-863b-a35f1ac77f57", value = "Challenge")
    @JsonProperty("challenge")
    @Valid
    public String getChallenge() {
        return challenge;
    }
    public void setChallenge(String challenge) {
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
        return Objects.equals(this.deviceId, discoveryDataDTO.deviceId) &&
            Objects.equals(this.username, discoveryDataDTO.username) &&
            Objects.equals(this.host, discoveryDataDTO.host) &&
            Objects.equals(this.tenantDomain, discoveryDataDTO.tenantDomain) &&
            Objects.equals(this.tenantPath, discoveryDataDTO.tenantPath) &&
            Objects.equals(this.organizationId, discoveryDataDTO.organizationId) &&
            Objects.equals(this.organizationName, discoveryDataDTO.organizationName) &&
            Objects.equals(this.organizationPath, discoveryDataDTO.organizationPath) &&
            Objects.equals(this.challenge, discoveryDataDTO.challenge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, username, host, tenantDomain, tenantPath, organizationId, organizationName, organizationPath, challenge);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class DiscoveryDataDTO {\n");
        
        sb.append("    deviceId: ").append(toIndentedString(deviceId)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    host: ").append(toIndentedString(host)).append("\n");
        sb.append("    tenantDomain: ").append(toIndentedString(tenantDomain)).append("\n");
        sb.append("    tenantPath: ").append(toIndentedString(tenantPath)).append("\n");
        sb.append("    organizationId: ").append(toIndentedString(organizationId)).append("\n");
        sb.append("    organizationName: ").append(toIndentedString(organizationName)).append("\n");
        sb.append("    organizationPath: ").append(toIndentedString(organizationPath)).append("\n");
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

