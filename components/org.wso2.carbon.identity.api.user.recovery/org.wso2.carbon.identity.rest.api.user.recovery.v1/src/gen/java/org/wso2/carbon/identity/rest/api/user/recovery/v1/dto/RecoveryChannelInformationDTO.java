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

package org.wso2.carbon.identity.rest.api.user.recovery.v1.dto;

import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.RecoveryChannelDTO;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

    /**
    * Response with the recovery ID and the available recovery channels
    **/
@ApiModel(description = "Response with the recovery ID and the available recovery channels")
public class RecoveryChannelInformationDTO {

    @Valid 
    private String recoveryCode = null;

    @Valid 
    private List<RecoveryChannelDTO> channels = new ArrayList<RecoveryChannelDTO>();

    /**
    * Code to recovery the user account
    **/
    @ApiModelProperty(value = "Code to recovery the user account")
    @JsonProperty("recoveryCode")
    public String getRecoveryCode() {
        return recoveryCode;
    }
    public void setRecoveryCode(String recoveryCode) {
        this.recoveryCode = recoveryCode;
    }

    /**
    * Availabel recovery channels for the user
    **/
    @ApiModelProperty(value = "Availabel recovery channels for the user")
    @JsonProperty("channels")
    public List<RecoveryChannelDTO> getChannels() {
        return channels;
    }
    public void setChannels(List<RecoveryChannelDTO> channels) {
        this.channels = channels;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RecoveryChannelInformationDTO {\n");
        
        sb.append("    recoveryCode: ").append(recoveryCode).append("\n");
        sb.append("    channels: ").append(channels).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
