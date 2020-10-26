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

public class UserStatusChangeRequest  {
  

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

    /**
    * LOCK/UNLOCK
    **/
    public UserStatusChangeRequest action(ActionEnum action) {

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
    public UserStatusChangeRequest timeToLock(String timeToLock) {

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



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserStatusChangeRequest userStatusChangeRequest = (UserStatusChangeRequest) o;
        return Objects.equals(this.action, userStatusChangeRequest.action) &&
            Objects.equals(this.timeToLock, userStatusChangeRequest.timeToLock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, timeToLock);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserStatusChangeRequest {\n");
        
        sb.append("    action: ").append(toIndentedString(action)).append("\n");
        sb.append("    timeToLock: ").append(toIndentedString(timeToLock)).append("\n");
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

