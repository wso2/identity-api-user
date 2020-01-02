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

import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.APICallDTO;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(description = "")
public class PasswordRecoveryExternalNotifyResponseDTO {

    @Valid 
    @NotNull(message = "Property code cannot be null.") 
    private String code = null;

    @Valid 
    @NotNull(message = "Property message cannot be null.") 
    private String message = null;

    @Valid 
    @NotNull(message = "Property notificationChannel cannot be null.") 
    private String notificationChannel = null;

    @Valid 
    @NotNull(message = "Property confirmationCode cannot be null.") 
    private String confirmationCode = null;

    @Valid 
    private List<APICallDTO> links = new ArrayList<APICallDTO>();

    /**
    * Success status code
    **/
    @ApiModelProperty(required = true, value = "Success status code")
    @JsonProperty("code")
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    /**
    * Success status message
    **/
    @ApiModelProperty(required = true, value = "Success status message")
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    /**
    * Channel that is used to send recovery information
    **/
    @ApiModelProperty(required = true, value = "Channel that is used to send recovery information")
    @JsonProperty("notificationChannel")
    public String getNotificationChannel() {
        return notificationChannel;
    }
    public void setNotificationChannel(String notificationChannel) {
        this.notificationChannel = notificationChannel;
    }

    /**
    * - Confirmation code for password recovery when the notifications are externally managed.\n- The confirmation code will be returned only if the notification channel is _EXTERNAL_. Use this code with password confirm API to get a password reset code.\n
    **/
    @ApiModelProperty(required = true, value = "- Confirmation code for password recovery when the notifications are externally managed.\n- The confirmation code will be returned only if the notification channel is _EXTERNAL_. Use this code with password confirm API to get a password reset code.\n")
    @JsonProperty("confirmationCode")
    public String getConfirmationCode() {
        return confirmationCode;
    }
    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
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
        sb.append("class PasswordRecoveryExternalNotifyResponseDTO {\n");
        
        sb.append("    code: ").append(code).append("\n");
        sb.append("    message: ").append(message).append("\n");
        sb.append("    notificationChannel: ").append(notificationChannel).append("\n");
        sb.append("    confirmationCode: ").append(confirmationCode).append("\n");
        sb.append("    links: ").append(links).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
