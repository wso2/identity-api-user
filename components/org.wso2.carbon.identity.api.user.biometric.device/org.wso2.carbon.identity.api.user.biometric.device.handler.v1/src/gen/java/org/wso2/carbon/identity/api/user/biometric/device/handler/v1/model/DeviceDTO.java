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
public class DeviceDTO  {
  
    private String id;
    private String name;
    private String model;
    private String pushId = null;
    private String publicKey = null;
    private Object registrationTime;
    private Object lastUsedTime;

    /**
    **/
    public DeviceDTO id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "768tyu78", value = "")
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
    public DeviceDTO name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "My Iphone", value = "")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public DeviceDTO model(String model) {

        this.model = model;
        return this;
    }
    
    @ApiModelProperty(example = "Iphone 8", value = "")
    @JsonProperty("model")
    @Valid
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    /**
    **/
    public DeviceDTO pushId(String pushId) {

        this.pushId = pushId;
        return this;
    }
    
    @ApiModelProperty(example = "816768tyu78", value = "")
    @JsonProperty("pushId")
    @Valid
    public String getPushId() {
        return pushId;
    }
    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    /**
    **/
    public DeviceDTO publicKey(String publicKey) {

        this.publicKey = publicKey;
        return this;
    }
    
    @ApiModelProperty(example = "81fsgfdfdsfds6768tyu78", value = "")
    @JsonProperty("publicKey")
    @Valid
    public String getPublicKey() {
        return publicKey;
    }
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
    **/
    public DeviceDTO registrationTime(Object registrationTime) {

        this.registrationTime = registrationTime;
        return this;
    }
    
    @ApiModelProperty(example = "2019-11-26T05:16:19.932Z", value = "")
    @JsonProperty("registrationTime")
    @Valid
    public Object getRegistrationTime() {
        return registrationTime;
    }
    public void setRegistrationTime(Object registrationTime) {
        this.registrationTime = registrationTime;
    }

    /**
    **/
    public DeviceDTO lastUsedTime(Object lastUsedTime) {

        this.lastUsedTime = lastUsedTime;
        return this;
    }
    
    @ApiModelProperty(example = "2019-11-26T05:16:19.932Z", value = "")
    @JsonProperty("lastUsedTime")
    @Valid
    public Object getLastUsedTime() {
        return lastUsedTime;
    }
    public void setLastUsedTime(Object lastUsedTime) {
        this.lastUsedTime = lastUsedTime;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeviceDTO deviceDTO = (DeviceDTO) o;
        return Objects.equals(this.id, deviceDTO.id) &&
            Objects.equals(this.name, deviceDTO.name) &&
            Objects.equals(this.model, deviceDTO.model) &&
            Objects.equals(this.pushId, deviceDTO.pushId) &&
            Objects.equals(this.publicKey, deviceDTO.publicKey) &&
            Objects.equals(this.registrationTime, deviceDTO.registrationTime) &&
            Objects.equals(this.lastUsedTime, deviceDTO.lastUsedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, model, pushId, publicKey, registrationTime, lastUsedTime);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class DeviceDTO {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    model: ").append(toIndentedString(model)).append("\n");
        sb.append("    pushId: ").append(toIndentedString(pushId)).append("\n");
        sb.append("    publicKey: ").append(toIndentedString(publicKey)).append("\n");
        sb.append("    registrationTime: ").append(toIndentedString(registrationTime)).append("\n");
        sb.append("    lastUsedTime: ").append(toIndentedString(lastUsedTime)).append("\n");
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

