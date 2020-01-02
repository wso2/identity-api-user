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
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.APICallDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.RecoveryChannelInformationDTO;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

    /**
    * Object that encapsulates details of the account recovery channel
    **/
@ApiModel(description = "Object that encapsulates details of the account recovery channel")
public class AccountRecoveryTypeDTO {

    @Valid 
    private String mode = null;

    @Valid 
    private RecoveryChannelInformationDTO channelInfo = null;

    @Valid 
    private List<APICallDTO> links = new ArrayList<APICallDTO>();

    /**
    **/
    @ApiModelProperty(value = "")
    @JsonProperty("mode")
    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
    **/
    @ApiModelProperty(value = "")
    @JsonProperty("channelInfo")
    public RecoveryChannelInformationDTO getChannelInfo() {
        return channelInfo;
    }
    public void setChannelInfo(RecoveryChannelInformationDTO channelInfo) {
        this.channelInfo = channelInfo;
    }

    /**
    * Contains available api calls
    **/
    @ApiModelProperty(value = "Contains available api calls")
    @JsonProperty("links")
    public List<APICallDTO> getLinks() {
        return links;
    }
    public void setLinks(List<APICallDTO> links) {
        this.links = links;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AccountRecoveryTypeDTO {\n");
        
        sb.append("    mode: ").append(mode).append("\n");
        sb.append("    channelInfo: ").append(channelInfo).append("\n");
        sb.append("    links: ").append(links).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
