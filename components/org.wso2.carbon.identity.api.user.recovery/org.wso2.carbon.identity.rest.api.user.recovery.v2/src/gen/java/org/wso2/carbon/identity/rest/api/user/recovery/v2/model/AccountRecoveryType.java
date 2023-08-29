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
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.APICall;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.RecoveryChannelInformation;
import javax.validation.constraints.*;

/**
 * Object that encapsulates details of the account recovery channel
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Object that encapsulates details of the account recovery channel")
public class AccountRecoveryType  {
  
    private String mode;
    private String flowConfirmationCode;
    private RecoveryChannelInformation channelInfo;
    private List<APICall> links = null;


    /**
    **/
    public AccountRecoveryType mode(String mode) {

        this.mode = mode;
        return this;
    }
    
    @ApiModelProperty(example = "recoverWithNotifications", value = "")
    @JsonProperty("mode")
    @Valid
    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
    **/
    public AccountRecoveryType flowConfirmationCode(String flowConfirmationCode) {

        this.flowConfirmationCode = flowConfirmationCode;
        return this;
    }
    
    @ApiModelProperty(example = "1234-1234-1234-1234", value = "")
    @JsonProperty("flowConfirmationCode")
    @Valid
    public String getFlowConfirmationCode() {
        return flowConfirmationCode;
    }
    public void setFlowConfirmationCode(String flowConfirmationCode) {
        this.flowConfirmationCode = flowConfirmationCode;
    }

    /**
    **/
    public AccountRecoveryType channelInfo(RecoveryChannelInformation channelInfo) {

        this.channelInfo = channelInfo;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("channelInfo")
    @Valid
    public RecoveryChannelInformation getChannelInfo() {
        return channelInfo;
    }
    public void setChannelInfo(RecoveryChannelInformation channelInfo) {
        this.channelInfo = channelInfo;
    }

    /**
    * Contains available api calls
    **/
    public AccountRecoveryType links(List<APICall> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(value = "Contains available api calls")
    @JsonProperty("links")
    @Valid
    public List<APICall> getLinks() {
        return links;
    }
    public void setLinks(List<APICall> links) {
        this.links = links;
    }

    public AccountRecoveryType addLinksItem(APICall linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
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
        AccountRecoveryType accountRecoveryType = (AccountRecoveryType) o;
        return Objects.equals(this.mode, accountRecoveryType.mode) &&
            Objects.equals(this.flowConfirmationCode, accountRecoveryType.flowConfirmationCode) &&
            Objects.equals(this.channelInfo, accountRecoveryType.channelInfo) &&
            Objects.equals(this.links, accountRecoveryType.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, flowConfirmationCode, channelInfo, links);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AccountRecoveryType {\n");
        
        sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
        sb.append("    flowConfirmationCode: ").append(toIndentedString(flowConfirmationCode)).append("\n");
        sb.append("    channelInfo: ").append(toIndentedString(channelInfo)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

