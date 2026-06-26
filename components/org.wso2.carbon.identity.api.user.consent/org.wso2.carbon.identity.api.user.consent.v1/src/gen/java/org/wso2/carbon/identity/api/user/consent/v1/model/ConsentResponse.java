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
import org.wso2.carbon.identity.api.user.consent.v1.model.AuthorizationResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentPurposeResponse;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ConsentResponse  {
  
    private String id;
    private String subjectId;
    private String serviceId;

@XmlType(name="StateEnum")
@XmlEnum(String.class)
public enum StateEnum {

    @XmlEnumValue("PENDING") PENDING(String.valueOf("PENDING")), @XmlEnumValue("ACTIVE") ACTIVE(String.valueOf("ACTIVE")), @XmlEnumValue("REJECTED") REJECTED(String.valueOf("REJECTED")), @XmlEnumValue("REVOKED") REVOKED(String.valueOf("REVOKED")), @XmlEnumValue("EXPIRED") EXPIRED(String.valueOf("EXPIRED"));


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

    private StateEnum state;
    private String language;
    private Long timestamp;
    private Long expiryTime;
    private List<ConsentPurposeResponse> purposes = null;

    private List<AuthorizationResponse> authorizations = null;

    private Map<String, String> properties = null;


    /**
    * Unique identifier of the consent.
    **/
    public ConsentResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "d75i1g32-5h4i-7e6d-2i9h-6d4e5f7g8h9i", value = "Unique identifier of the consent.")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Identifier of the user who gave consent.
    **/
    public ConsentResponse subjectId(String subjectId) {

        this.subjectId = subjectId;
        return this;
    }
    
    @ApiModelProperty(example = "john", value = "Identifier of the user who gave consent.")
    @JsonProperty("subjectId")
    @Valid
    public String getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    /**
    * Identifier of the service the consent was given for.
    **/
    public ConsentResponse serviceId(String serviceId) {

        this.serviceId = serviceId;
        return this;
    }
    
    @ApiModelProperty(example = "sampleApp", value = "Identifier of the service the consent was given for.")
    @JsonProperty("serviceId")
    @Valid
    public String getServiceId() {
        return serviceId;
    }
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
    * Current state of the consent.
    **/
    public ConsentResponse state(StateEnum state) {

        this.state = state;
        return this;
    }
    
    @ApiModelProperty(example = "ACTIVE", value = "Current state of the consent.")
    @JsonProperty("state")
    @Valid
    public StateEnum getState() {
        return state;
    }
    public void setState(StateEnum state) {
        this.state = state;
    }

    /**
    * Language code for the consent.
    **/
    public ConsentResponse language(String language) {

        this.language = language;
        return this;
    }
    
    @ApiModelProperty(example = "en", value = "Language code for the consent.")
    @JsonProperty("language")
    @Valid
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
    * Milliseconds since epoch when the consent was created.
    **/
    public ConsentResponse timestamp(Long timestamp) {

        this.timestamp = timestamp;
        return this;
    }
    
    @ApiModelProperty(example = "1750156200000", value = "Milliseconds since epoch when the consent was created.")
    @JsonProperty("timestamp")
    @Valid
    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
    * Milliseconds since epoch until which the consent is valid. Null if no expiry.
    **/
    public ConsentResponse expiryTime(Long expiryTime) {

        this.expiryTime = expiryTime;
        return this;
    }
    
    @ApiModelProperty(example = "1766383796000", value = "Milliseconds since epoch until which the consent is valid. Null if no expiry.")
    @JsonProperty("expiryTime")
    @Valid
    public Long getExpiryTime() {
        return expiryTime;
    }
    public void setExpiryTime(Long expiryTime) {
        this.expiryTime = expiryTime;
    }

    /**
    * List of purposes included in the consent.
    **/
    public ConsentResponse purposes(List<ConsentPurposeResponse> purposes) {

        this.purposes = purposes;
        return this;
    }
    
    @ApiModelProperty(value = "List of purposes included in the consent.")
    @JsonProperty("purposes")
    @Valid
    public List<ConsentPurposeResponse> getPurposes() {
        return purposes;
    }
    public void setPurposes(List<ConsentPurposeResponse> purposes) {
        this.purposes = purposes;
    }

    public ConsentResponse addPurposesItem(ConsentPurposeResponse purposesItem) {
        if (this.purposes == null) {
            this.purposes = new ArrayList<>();
        }
        this.purposes.add(purposesItem);
        return this;
    }

        /**
    * Authorization records associated with this consent.
    **/
    public ConsentResponse authorizations(List<AuthorizationResponse> authorizations) {

        this.authorizations = authorizations;
        return this;
    }
    
    @ApiModelProperty(value = "Authorization records associated with this consent.")
    @JsonProperty("authorizations")
    @Valid
    public List<AuthorizationResponse> getAuthorizations() {
        return authorizations;
    }
    public void setAuthorizations(List<AuthorizationResponse> authorizations) {
        this.authorizations = authorizations;
    }

    public ConsentResponse addAuthorizationsItem(AuthorizationResponse authorizationsItem) {
        if (this.authorizations == null) {
            this.authorizations = new ArrayList<>();
        }
        this.authorizations.add(authorizationsItem);
        return this;
    }

        /**
    * Properties associated with this consent.
    **/
    public ConsentResponse properties(Map<String, String> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "Properties associated with this consent.")
    @JsonProperty("properties")
    @Valid
    public Map<String, String> getProperties() {
        return properties;
    }
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }


    public ConsentResponse putPropertiesItem(String key, String propertiesItem) {
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
        ConsentResponse consentResponse = (ConsentResponse) o;
        return Objects.equals(this.id, consentResponse.id) &&
            Objects.equals(this.subjectId, consentResponse.subjectId) &&
            Objects.equals(this.serviceId, consentResponse.serviceId) &&
            Objects.equals(this.state, consentResponse.state) &&
            Objects.equals(this.language, consentResponse.language) &&
            Objects.equals(this.timestamp, consentResponse.timestamp) &&
            Objects.equals(this.expiryTime, consentResponse.expiryTime) &&
            Objects.equals(this.purposes, consentResponse.purposes) &&
            Objects.equals(this.authorizations, consentResponse.authorizations) &&
            Objects.equals(this.properties, consentResponse.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subjectId, serviceId, state, language, timestamp, expiryTime, purposes, authorizations, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConsentResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    subjectId: ").append(toIndentedString(subjectId)).append("\n");
        sb.append("    serviceId: ").append(toIndentedString(serviceId)).append("\n");
        sb.append("    state: ").append(toIndentedString(state)).append("\n");
        sb.append("    language: ").append(toIndentedString(language)).append("\n");
        sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
        sb.append("    expiryTime: ").append(toIndentedString(expiryTime)).append("\n");
        sb.append("    purposes: ").append(toIndentedString(purposes)).append("\n");
        sb.append("    authorizations: ").append(toIndentedString(authorizations)).append("\n");
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

