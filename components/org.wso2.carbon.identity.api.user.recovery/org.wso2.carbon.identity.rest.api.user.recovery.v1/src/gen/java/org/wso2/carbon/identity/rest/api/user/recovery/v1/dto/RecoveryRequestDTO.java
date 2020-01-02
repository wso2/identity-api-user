/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.recovery.v1.dto;

import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.PropertyDTO;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

    /**
    * Request to start the recovery for the user who matches the recovery code via the given channelID
    **/
@ApiModel(description = "Request to start the recovery for the user who matches the recovery code via the given channelID")
public class RecoveryRequestDTO {

    @Valid 
    @NotNull(message = "Property recoveryCode cannot be null.") 
    private String recoveryCode = null;

    @Valid 
    @NotNull(message = "Property channelId cannot be null.") 
    private String channelId = null;

    @Valid 
    private List<PropertyDTO> properties = new ArrayList<PropertyDTO>();

    /**
    * Recovery code for the user
    **/
    @ApiModelProperty(required = true, value = "Recovery code for the user")
    @JsonProperty("recoveryCode")
    public String getRecoveryCode() {
        return recoveryCode;
    }
    public void setRecoveryCode(String recoveryCode) {
        this.recoveryCode = recoveryCode;
    }

    /**
    * Id of the notification channel that user preferrs to get recovery notifications.
    **/
    @ApiModelProperty(required = true, value = "Id of the notification channel that user preferrs to get recovery notifications.")
    @JsonProperty("channelId")
    public String getChannelId() {
        return channelId;
    }
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
    * (OPTIONAL) Additional META properties
    **/
    @ApiModelProperty(value = "(OPTIONAL) Additional META properties")
    @JsonProperty("properties")
    public List<PropertyDTO> getProperties() {
        return properties;
    }
    public void setProperties(List<PropertyDTO> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RecoveryRequestDTO {\n");
        
        sb.append("    recoveryCode: ").append(recoveryCode).append("\n");
        sb.append("    channelId: ").append(channelId).append("\n");
        sb.append("    properties: ").append(properties).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
