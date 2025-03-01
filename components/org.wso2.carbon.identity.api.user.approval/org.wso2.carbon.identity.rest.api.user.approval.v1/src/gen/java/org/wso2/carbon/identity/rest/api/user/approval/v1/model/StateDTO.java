/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.approval.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class StateDTO  {
  

@XmlType(name="ActionEnum")
@XmlEnum(String.class)
public enum ActionEnum {

    @XmlEnumValue("CLAIM") CLAIM(String.valueOf("CLAIM")), @XmlEnumValue("RELEASE") RELEASE(String.valueOf("RELEASE")), @XmlEnumValue("APPROVE") APPROVE(String.valueOf("APPROVE")), @XmlEnumValue("REJECT") REJECT(String.valueOf("REJECT"));


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

    /**
    * Action to perform on the task.
    **/
    public StateDTO action(ActionEnum action) {

        this.action = action;
        return this;
    }
    
    @ApiModelProperty(example = "APPROVE", value = "Action to perform on the task.")
    @JsonProperty("action")
    @Valid
    public ActionEnum getAction() {
        return action;
    }
    public void setAction(ActionEnum action) {
        this.action = action;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StateDTO stateDTO = (StateDTO) o;
        return Objects.equals(this.action, stateDTO.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class StateDTO {\n");
        
        sb.append("    action: ").append(toIndentedString(action)).append("\n");
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

