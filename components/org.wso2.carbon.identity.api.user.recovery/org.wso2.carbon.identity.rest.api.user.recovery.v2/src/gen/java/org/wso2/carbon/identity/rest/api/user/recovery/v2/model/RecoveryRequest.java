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
 * Request to start the recovery for the user who matches the recovery code via the given channelID
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Request to start the recovery for the user who matches the recovery code via the given channelID")
public class RecoveryRequest  {
  
    private String recoveryCode;
    private String channelId;
    private List<Property> properties = null;


    /**
    * Recovery code for the user
    **/
    public RecoveryRequest recoveryCode(String recoveryCode) {

        this.recoveryCode = recoveryCode;
        return this;
    }
    
    @ApiModelProperty(example = "1234-5678-2455-3433", required = true, value = "Recovery code for the user")
    @JsonProperty("recoveryCode")
    @Valid
    @NotNull(message = "Property recoveryCode cannot be null.")

    public String getRecoveryCode() {
        return recoveryCode;
    }
    public void setRecoveryCode(String recoveryCode) {
        this.recoveryCode = recoveryCode;
    }

    /**
    * Id of the notification channel that user preferrs to get recovery notifications.
    **/
    public RecoveryRequest channelId(String channelId) {

        this.channelId = channelId;
        return this;
    }
    
    @ApiModelProperty(example = "1", required = true, value = "Id of the notification channel that user preferrs to get recovery notifications.")
    @JsonProperty("channelId")
    @Valid
    @NotNull(message = "Property channelId cannot be null.")

    public String getChannelId() {
        return channelId;
    }
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
    * (OPTIONAL) Additional META properties
    **/
    public RecoveryRequest properties(List<Property> properties) {

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

    public RecoveryRequest addPropertiesItem(Property propertiesItem) {
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
        RecoveryRequest recoveryRequest = (RecoveryRequest) o;
        return Objects.equals(this.recoveryCode, recoveryRequest.recoveryCode) &&
            Objects.equals(this.channelId, recoveryRequest.channelId) &&
            Objects.equals(this.properties, recoveryRequest.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recoveryCode, channelId, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RecoveryRequest {\n");
        
        sb.append("    recoveryCode: ").append(toIndentedString(recoveryCode)).append("\n");
        sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
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

