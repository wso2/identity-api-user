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
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.RecoveryChannel;
import javax.validation.constraints.*;

/**
 * Response with the recovery ID and the available recovery channels
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Response with the recovery ID and the available recovery channels")
public class RecoveryChannelInformation  {
  
    private String recoveryCode;
    private List<RecoveryChannel> channels = null;


    /**
    * Code to recovery the user account
    **/
    public RecoveryChannelInformation recoveryCode(String recoveryCode) {

        this.recoveryCode = recoveryCode;
        return this;
    }
    
    @ApiModelProperty(example = "1234-55678-5668-2345", value = "Code to recovery the user account")
    @JsonProperty("recoveryCode")
    @Valid
    public String getRecoveryCode() {
        return recoveryCode;
    }
    public void setRecoveryCode(String recoveryCode) {
        this.recoveryCode = recoveryCode;
    }

    /**
    * Availabel recovery channels for the user
    **/
    public RecoveryChannelInformation channels(List<RecoveryChannel> channels) {

        this.channels = channels;
        return this;
    }
    
    @ApiModelProperty(value = "Availabel recovery channels for the user")
    @JsonProperty("channels")
    @Valid
    public List<RecoveryChannel> getChannels() {
        return channels;
    }
    public void setChannels(List<RecoveryChannel> channels) {
        this.channels = channels;
    }

    public RecoveryChannelInformation addChannelsItem(RecoveryChannel channelsItem) {
        if (this.channels == null) {
            this.channels = new ArrayList<>();
        }
        this.channels.add(channelsItem);
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
        RecoveryChannelInformation recoveryChannelInformation = (RecoveryChannelInformation) o;
        return Objects.equals(this.recoveryCode, recoveryChannelInformation.recoveryCode) &&
            Objects.equals(this.channels, recoveryChannelInformation.channels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recoveryCode, channels);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RecoveryChannelInformation {\n");
        
        sb.append("    recoveryCode: ").append(toIndentedString(recoveryCode)).append("\n");
        sb.append("    channels: ").append(toIndentedString(channels)).append("\n");
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

