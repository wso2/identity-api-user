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
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ConsentCreateResponse  {
  
    private String id;
    private String subjectId;
    private String serviceId;

@XmlType(name="StateEnum")
@XmlEnum(String.class)
public enum StateEnum {

    @XmlEnumValue("ACTIVE") ACTIVE(String.valueOf("ACTIVE")), @XmlEnumValue("REVOKED") REVOKED(String.valueOf("REVOKED")), @XmlEnumValue("EXPIRED") EXPIRED(String.valueOf("EXPIRED"));


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

    /**
    * Unique identifier of the created consent.
    **/
    public ConsentCreateResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "d75i1g32-5h4i-7e6d-2i9h-6d4e5f7g8h9i", value = "Unique identifier of the created consent.")
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
    public ConsentCreateResponse subjectId(String subjectId) {

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
    public ConsentCreateResponse serviceId(String serviceId) {

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
    * Initial state of the consent.
    **/
    public ConsentCreateResponse state(StateEnum state) {

        this.state = state;
        return this;
    }
    
    @ApiModelProperty(example = "ACTIVE", value = "Initial state of the consent.")
    @JsonProperty("state")
    @Valid
    public StateEnum getState() {
        return state;
    }
    public void setState(StateEnum state) {
        this.state = state;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConsentCreateResponse consentCreateResponse = (ConsentCreateResponse) o;
        return Objects.equals(this.id, consentCreateResponse.id) &&
            Objects.equals(this.subjectId, consentCreateResponse.subjectId) &&
            Objects.equals(this.serviceId, consentCreateResponse.serviceId) &&
            Objects.equals(this.state, consentCreateResponse.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subjectId, serviceId, state);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConsentCreateResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    subjectId: ").append(toIndentedString(subjectId)).append("\n");
        sb.append("    serviceId: ").append(toIndentedString(serviceId)).append("\n");
        sb.append("    state: ").append(toIndentedString(state)).append("\n");
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

