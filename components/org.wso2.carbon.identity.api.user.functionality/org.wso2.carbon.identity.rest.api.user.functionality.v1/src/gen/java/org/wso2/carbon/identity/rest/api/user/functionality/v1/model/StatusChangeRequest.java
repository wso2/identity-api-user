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

package org.wso2.carbon.identity.rest.api.user.functionality.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class StatusChangeRequest  {
  

@XmlType(name="ActionEnum")
@XmlEnum(String.class)
public enum ActionEnum {

    @XmlEnumValue("LOCK") LOCK(String.valueOf("LOCK")), @XmlEnumValue("UNLOCK") UNLOCK(String.valueOf("UNLOCK"));


    private String value;

    ActionEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ActionEnum fromValue(String value) {
        for (ActionEnum b : ActionEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private ActionEnum action;
    private String timeToLock;
    private String functionalityLockReason;
    private String functionalityLockReasonCode;

    /**
    * LOCK/UNLOCK
    **/
    public StatusChangeRequest action(ActionEnum action) {

        this.action = action;
        return this;
    }
    
    @ApiModelProperty(example = "lock", required = true, value = "LOCK/UNLOCK")
    @JsonProperty("action")
    @Valid
    @NotNull(message = "Property action cannot be null.")

    public ActionEnum getAction() {
        return action;
    }
    public void setAction(ActionEnum action) {
        this.action = action;
    }

    /**
    * Time in millisecond
    **/
    public StatusChangeRequest timeToLock(String timeToLock) {

        this.timeToLock = timeToLock;
        return this;
    }
    
    @ApiModelProperty(example = "1594987178", value = "Time in millisecond")
    @JsonProperty("timeToLock")
    @Valid
    public String getTimeToLock() {
        return timeToLock;
    }
    public void setTimeToLock(String timeToLock) {
        this.timeToLock = timeToLock;
    }

    /**
    **/
    public StatusChangeRequest functionalityLockReason(String functionalityLockReason) {

        this.functionalityLockReason = functionalityLockReason;
        return this;
    }
    
    @ApiModelProperty(example = "SecurityQuestionBasedBased", value = "")
    @JsonProperty("functionalityLockReason")
    @Valid
    public String getFunctionalityLockReason() {
        return functionalityLockReason;
    }
    public void setFunctionalityLockReason(String functionalityLockReason) {
        this.functionalityLockReason = functionalityLockReason;
    }

    /**
    **/
    public StatusChangeRequest functionalityLockReasonCode(String functionalityLockReasonCode) {

        this.functionalityLockReasonCode = functionalityLockReasonCode;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("functionalityLockReasonCode")
    @Valid
    public String getFunctionalityLockReasonCode() {
        return functionalityLockReasonCode;
    }
    public void setFunctionalityLockReasonCode(String functionalityLockReasonCode) {
        this.functionalityLockReasonCode = functionalityLockReasonCode;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatusChangeRequest statusChangeRequest = (StatusChangeRequest) o;
        return Objects.equals(this.action, statusChangeRequest.action) &&
            Objects.equals(this.timeToLock, statusChangeRequest.timeToLock) &&
            Objects.equals(this.functionalityLockReason, statusChangeRequest.functionalityLockReason) &&
            Objects.equals(this.functionalityLockReasonCode, statusChangeRequest.functionalityLockReasonCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, timeToLock, functionalityLockReason, functionalityLockReasonCode);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class StatusChangeRequest {\n");
        
        sb.append("    action: ").append(toIndentedString(action)).append("\n");
        sb.append("    timeToLock: ").append(toIndentedString(timeToLock)).append("\n");
        sb.append("    functionalityLockReason: ").append(toIndentedString(functionalityLockReason)).append("\n");
        sb.append("    functionalityLockReasonCode: ").append(toIndentedString(functionalityLockReasonCode)).append("\n");
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

