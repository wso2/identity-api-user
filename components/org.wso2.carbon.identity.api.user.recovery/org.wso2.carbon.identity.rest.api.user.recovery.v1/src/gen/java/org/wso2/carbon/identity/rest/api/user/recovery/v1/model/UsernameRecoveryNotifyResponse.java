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

package org.wso2.carbon.identity.rest.api.user.recovery.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * API response for successful username recovery
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "API response for successful username recovery")
public class UsernameRecoveryNotifyResponse  {
  
    private String code;
    private String message;
    private String notificationChannel;
    private String username;

    /**
    * Success status code
    **/
    public UsernameRecoveryNotifyResponse code(String code) {

        this.code = code;
        return this;
    }
    
    @ApiModelProperty(example = "UNR-02001", required = true, value = "Success status code")
    @JsonProperty("code")
    @Valid
    @NotNull(message = "Property code cannot be null.")

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    /**
    * Success status message
    **/
    public UsernameRecoveryNotifyResponse message(String message) {

        this.message = message;
        return this;
    }
    
    @ApiModelProperty(example = "successful_request", required = true, value = "Success status message")
    @JsonProperty("message")
    @Valid
    @NotNull(message = "Property message cannot be null.")

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    /**
    * Channel which the recovery information is sent to the user
    **/
    public UsernameRecoveryNotifyResponse notificationChannel(String notificationChannel) {

        this.notificationChannel = notificationChannel;
        return this;
    }
    
    @ApiModelProperty(example = "EXTERNAL", required = true, value = "Channel which the recovery information is sent to the user")
    @JsonProperty("notificationChannel")
    @Valid
    @NotNull(message = "Property notificationChannel cannot be null.")

    public String getNotificationChannel() {
        return notificationChannel;
    }
    public void setNotificationChannel(String notificationChannel) {
        this.notificationChannel = notificationChannel;
    }

    /**
    * - Username of the user - Username will be returned _ONLY IF_ the notification channel is &#x60;EXTERNAL&#x60; 
    **/
    public UsernameRecoveryNotifyResponse username(String username) {

        this.username = username;
        return this;
    }
    
    @ApiModelProperty(example = "user1", required = true, value = "- Username of the user - Username will be returned _ONLY IF_ the notification channel is `EXTERNAL` ")
    @JsonProperty("username")
    @Valid
    @NotNull(message = "Property username cannot be null.")

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UsernameRecoveryNotifyResponse usernameRecoveryNotifyResponse = (UsernameRecoveryNotifyResponse) o;
        return Objects.equals(this.code, usernameRecoveryNotifyResponse.code) &&
            Objects.equals(this.message, usernameRecoveryNotifyResponse.message) &&
            Objects.equals(this.notificationChannel, usernameRecoveryNotifyResponse.notificationChannel) &&
            Objects.equals(this.username, usernameRecoveryNotifyResponse.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, notificationChannel, username);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UsernameRecoveryNotifyResponse {\n");
        
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    notificationChannel: ").append(toIndentedString(notificationChannel)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
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

