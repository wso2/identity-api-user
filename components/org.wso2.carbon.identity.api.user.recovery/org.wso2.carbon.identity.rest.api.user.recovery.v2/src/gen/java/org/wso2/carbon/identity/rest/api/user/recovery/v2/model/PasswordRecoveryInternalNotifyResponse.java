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
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PasswordRecoveryInternalNotifyResponse  {
  
    private String code;
    private String message;
    private String flowConfirmationCode;
    private String notificationChannel;
    private String resendCode;
    private List<APICall> links = null;


    /**
    * Success status code
    **/
    public PasswordRecoveryInternalNotifyResponse code(String code) {

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
    public PasswordRecoveryInternalNotifyResponse message(String message) {

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
     * Recovery flow confirmation code
     **/
    public PasswordRecoveryInternalNotifyResponse flowConfirmationCode(String flowConfirmationCode) {

        this.flowConfirmationCode = flowConfirmationCode;
        return this;
    }

    @ApiModelProperty(example = "1234-1234-1234-1234", value = "Flow confirmation code of the recovery flow")
    @JsonProperty("flowConfirmationCode")
    @Valid
    public String getFlowConfirmationCode() {
        return flowConfirmationCode;
    }
    public void setFlowConfirmationCode(String flowConfirmationCode) {
        this.flowConfirmationCode = flowConfirmationCode;
    }

    /**
    * Channel that is used to send recovery information
    **/
    public PasswordRecoveryInternalNotifyResponse notificationChannel(String notificationChannel) {

        this.notificationChannel = notificationChannel;
        return this;
    }
    
    @ApiModelProperty(example = "EMAIL", value = "Channel that is used to send recovery information")
    @JsonProperty("notificationChannel")
    @Valid
    public String getNotificationChannel() {
        return notificationChannel;
    }
    public void setNotificationChannel(String notificationChannel) {
        this.notificationChannel = notificationChannel;
    }

    /**
    * Code to resend the confirmation code to the user via user user selected channel
    **/
    public PasswordRecoveryInternalNotifyResponse resendCode(String resendCode) {

        this.resendCode = resendCode;
        return this;
    }
    
    @ApiModelProperty(example = "1234-123456-12345-12345", value = "Code to resend the confirmation code to the user via user user selected channel")
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
    public PasswordRecoveryInternalNotifyResponse links(List<APICall> links) {

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

    public PasswordRecoveryInternalNotifyResponse addLinksItem(APICall linksItem) {
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
        PasswordRecoveryInternalNotifyResponse passwordRecoveryInternalNotifyResponse = (PasswordRecoveryInternalNotifyResponse) o;
        return Objects.equals(this.code, passwordRecoveryInternalNotifyResponse.code) &&
            Objects.equals(this.message, passwordRecoveryInternalNotifyResponse.message) &&
            Objects.equals(this.flowConfirmationCode, passwordRecoveryInternalNotifyResponse.flowConfirmationCode) &&
            Objects.equals(this.notificationChannel, passwordRecoveryInternalNotifyResponse.notificationChannel) &&
            Objects.equals(this.resendCode, passwordRecoveryInternalNotifyResponse.resendCode) &&
            Objects.equals(this.links, passwordRecoveryInternalNotifyResponse.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, flowConfirmationCode, notificationChannel, resendCode, links);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PasswordRecoveryInternalNotifyResponse {\n");
        
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    flowConfirmationCode: ").append(toIndentedString(flowConfirmationCode)).append("\n");
        sb.append("    notificationChannel: ").append(toIndentedString(notificationChannel)).append("\n");
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

