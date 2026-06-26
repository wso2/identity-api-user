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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentPurposeInput;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ConsentInput  {
  
    private String serviceId;
    private String language;
    private List<ConsentPurposeInput> purposes = new ArrayList<>();


@XmlType(name="StateEnum")
@XmlEnum(String.class)
public enum StateEnum {

    @XmlEnumValue("ACTIVE") ACTIVE(String.valueOf("ACTIVE")), @XmlEnumValue("REJECTED") REJECTED(String.valueOf("REJECTED"));


    private String value;

    StateEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static StateEnum fromValue(String value) {
        for (StateEnum b : StateEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private StateEnum state = StateEnum.ACTIVE;
    private Long expiryTime;
    private Map<String, String> properties = null;


    /**
    * Identifier of the service the consent is given for.
    **/
    public ConsentInput serviceId(String serviceId) {

        this.serviceId = serviceId;
        return this;
    }
    
    @ApiModelProperty(example = "sampleApp", required = true, value = "Identifier of the service the consent is given for.")
    @JsonProperty("serviceId")
    @Valid
    @NotNull(message = "Property serviceId cannot be null.")
 @Size(min=1,max=255)
    public String getServiceId() {
        return serviceId;
    }
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
    * Language code for the consent.
    **/
    public ConsentInput language(String language) {

        this.language = language;
        return this;
    }
    
    @ApiModelProperty(example = "en", value = "Language code for the consent.")
    @JsonProperty("language")
    @Valid @Size(min=1,max=20)
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
    * List of purposes the user is consenting to.
    **/
    public ConsentInput purposes(List<ConsentPurposeInput> purposes) {

        this.purposes = purposes;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "List of purposes the user is consenting to.")
    @JsonProperty("purposes")
    @Valid
    @NotNull(message = "Property purposes cannot be null.")
 @Size(min=1)
    public List<ConsentPurposeInput> getPurposes() {
        return purposes;
    }
    public void setPurposes(List<ConsentPurposeInput> purposes) {
        this.purposes = purposes;
    }

    public ConsentInput addPurposesItem(ConsentPurposeInput purposesItem) {
        this.purposes.add(purposesItem);
        return this;
    }

        /**
    * Initial state of the consent. Use ACTIVE when the user accepts (default). Use REJECTED when the user explicitly declines an optional policy — this records that the user saw and skipped this version so it is not shown again until a new version is published. 
    **/
    public ConsentInput state(StateEnum state) {

        this.state = state;
        return this;
    }
    
    @ApiModelProperty(example = "ACTIVE", value = "Initial state of the consent. Use ACTIVE when the user accepts (default). Use REJECTED when the user explicitly declines an optional policy — this records that the user saw and skipped this version so it is not shown again until a new version is published. ")
    @JsonProperty("state")
    @Valid
    public StateEnum getState() {
        return state;
    }
    public void setState(StateEnum state) {
        this.state = state;
    }

    /**
    * Milliseconds since epoch until which the consent is valid. If omitted, the consent does not expire.
    **/
    public ConsentInput expiryTime(Long expiryTime) {

        this.expiryTime = expiryTime;
        return this;
    }
    
    @ApiModelProperty(example = "1766383796000", value = "Milliseconds since epoch until which the consent is valid. If omitted, the consent does not expire.")
    @JsonProperty("expiryTime")
    @Valid
    public Long getExpiryTime() {
        return expiryTime;
    }
    public void setExpiryTime(Long expiryTime) {
        this.expiryTime = expiryTime;
    }

    /**
    * Optional free-form key-value properties for this consent.
    **/
    public ConsentInput properties(Map<String, String> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(example = "{\"source\":\"onboarding-flow\"}", value = "Optional free-form key-value properties for this consent.")
    @JsonProperty("properties")
    @Valid
    public Map<String, String> getProperties() {
        return properties;
    }
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }


    public ConsentInput putPropertiesItem(String key, String propertiesItem) {
        if (this.properties == null) {
            this.properties = new HashMap<>();
        }
        this.properties.put(key, propertiesItem);
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
        ConsentInput consentInput = (ConsentInput) o;
        return Objects.equals(this.serviceId, consentInput.serviceId) &&
            Objects.equals(this.language, consentInput.language) &&
            Objects.equals(this.purposes, consentInput.purposes) &&
            Objects.equals(this.state, consentInput.state) &&
            Objects.equals(this.expiryTime, consentInput.expiryTime) &&
            Objects.equals(this.properties, consentInput.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, language, purposes, state, expiryTime, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConsentInput {\n");
        
        sb.append("    serviceId: ").append(toIndentedString(serviceId)).append("\n");
        sb.append("    language: ").append(toIndentedString(language)).append("\n");
        sb.append("    purposes: ").append(toIndentedString(purposes)).append("\n");
        sb.append("    state: ").append(toIndentedString(state)).append("\n");
        sb.append("    expiryTime: ").append(toIndentedString(expiryTime)).append("\n");
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

