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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.APICall;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PasswordRecoveryExternalNotifyResponse  {
  
    private String code;
    private String message;
    private String notificationChannel;
    private String confirmationCode;
    private String resendCode;
    private List<APICall> links = null;


    /**
    * Success status code
    **/
    public PasswordRecoveryExternalNotifyResponse code(String code) {

        this.code = code;
        return this;
    }
    
    @ApiModelProperty(example = "PWR-02002", value = "Success status code")
    @JsonProperty("code")
    @Valid
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    /**
    * Success status message
    **/
    public PasswordRecoveryExternalNotifyResponse message(String message) {

        this.message = message;
        return this;
    }
    
    @ApiModelProperty(example = "successful_request", value = "Success status message")
    @JsonProperty("message")
    @Valid
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    /**
    * Channel that is used to send recovery information
    **/
    public PasswordRecoveryExternalNotifyResponse notificationChannel(String notificationChannel) {

        this.notificationChannel = notificationChannel;
        return this;
    }
    
    @ApiModelProperty(example = "EXTERNAL", value = "Channel that is used to send recovery information")
    @JsonProperty("notificationChannel")
    @Valid
    public String getNotificationChannel() {
        return notificationChannel;
    }
    public void setNotificationChannel(String notificationChannel) {
        this.notificationChannel = notificationChannel;
    }

    /**
    * - Confirmation code for password recovery when the notifications are externally managed. - The confirmation code will be returned only if the notification channel is _EXTERNAL_. Use this code with password confirm API to get a password reset code. 
    **/
    public PasswordRecoveryExternalNotifyResponse confirmationCode(String confirmationCode) {

        this.confirmationCode = confirmationCode;
        return this;
    }
    
    @ApiModelProperty(example = "12345-45678-6789098-8765", value = "- Confirmation code for password recovery when the notifications are externally managed. - The confirmation code will be returned only if the notification channel is _EXTERNAL_. Use this code with password confirm API to get a password reset code. ")
    @JsonProperty("confirmationCode")
    @Valid
    public String getConfirmationCode() {
        return confirmationCode;
    }
    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    /**
    * Code to get a new confirmation code.
    **/
    public PasswordRecoveryExternalNotifyResponse resendCode(String resendCode) {

        this.resendCode = resendCode;
        return this;
    }
    
    @ApiModelProperty(example = "1234-123456-12345-12345", value = "Code to get a new confirmation code.")
    @JsonProperty("resendCode")
    @Valid
    public String getResendCode() {
        return resendCode;
    }
    public void setResendCode(String resendCode) {
        this.resendCode = resendCode;
    }

    /**
    * Contains available api calls
    **/
    public PasswordRecoveryExternalNotifyResponse links(List<APICall> links) {

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

    public PasswordRecoveryExternalNotifyResponse addLinksItem(APICall linksItem) {
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
        PasswordRecoveryExternalNotifyResponse passwordRecoveryExternalNotifyResponse = (PasswordRecoveryExternalNotifyResponse) o;
        return Objects.equals(this.code, passwordRecoveryExternalNotifyResponse.code) &&
            Objects.equals(this.message, passwordRecoveryExternalNotifyResponse.message) &&
            Objects.equals(this.notificationChannel, passwordRecoveryExternalNotifyResponse.notificationChannel) &&
            Objects.equals(this.confirmationCode, passwordRecoveryExternalNotifyResponse.confirmationCode) &&
            Objects.equals(this.resendCode, passwordRecoveryExternalNotifyResponse.resendCode) &&
            Objects.equals(this.links, passwordRecoveryExternalNotifyResponse.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, notificationChannel, confirmationCode, resendCode, links);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PasswordRecoveryExternalNotifyResponse {\n");
        
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    notificationChannel: ").append(toIndentedString(notificationChannel)).append("\n");
        sb.append("    confirmationCode: ").append(toIndentedString(confirmationCode)).append("\n");
        sb.append("    resendCode: ").append(toIndentedString(resendCode)).append("\n");
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

