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

package org.wso2.carbon.identity.rest.api.user.recovery.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.Property;
import javax.validation.constraints.*;

/**
 * Object to reset the password of a user
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Object to reset the password of a user")
public class ResetRequest  {
  
    private String resetCode;
    private String flowConfirmationCode;
    private String password;
    private List<Property> properties = null;


    /**
    * resetCode given by the confim API
    **/
    public ResetRequest resetCode(String resetCode) {

        this.resetCode = resetCode;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "resetCode given by the confim API")
    @JsonProperty("resetCode")
    @Valid
    @NotNull(message = "Property resetCode cannot be null.")

    public String getResetCode() {
        return resetCode;
    }
    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    /**
    * Confirmation code of the recovery flow
    **/
    public ResetRequest flowConfirmationCode(String flowConfirmationCode) {

        this.flowConfirmationCode = flowConfirmationCode;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Confirmation code of the recovery flow")
    @JsonProperty("flowConfirmationCode")
    @Valid
    @NotNull(message = "Property flowConfirmationCode cannot be null.")

    public String getFlowConfirmationCode() {
        return flowConfirmationCode;
    }
    public void setFlowConfirmationCode(String flowConfirmationCode) {
        this.flowConfirmationCode = flowConfirmationCode;
    }

    /**
    * New password given by the user
    **/
    public ResetRequest password(String password) {

        this.password = password;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "New password given by the user")
    @JsonProperty("password")
    @Valid
    @NotNull(message = "Property password cannot be null.")

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /**
    * (OPTIONAL) Additional META properties
    **/
    public ResetRequest properties(List<Property> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "(OPTIONAL) Additional META properties")
    @JsonProperty("properties")
    @Valid
    public List<Property> getProperties() {
        return properties;
    }
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public ResetRequest addPropertiesItem(Property propertiesItem) {
        if (this.properties == null) {
            this.properties = new ArrayList<>();
        }
        this.properties.add(propertiesItem);
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
        ResetRequest resetRequest = (ResetRequest) o;
        return Objects.equals(this.resetCode, resetRequest.resetCode) &&
            Objects.equals(this.flowConfirmationCode, resetRequest.flowConfirmationCode) &&
            Objects.equals(this.password, resetRequest.password) &&
            Objects.equals(this.properties, resetRequest.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resetCode, flowConfirmationCode, password, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ResetRequest {\n");
        
        sb.append("    resetCode: ").append(toIndentedString(resetCode)).append("\n");
        sb.append("    flowConfirmationCode: ").append(toIndentedString(flowConfirmationCode)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
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

