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

package org.wso2.carbon.identity.rest.api.user.session.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@ApiModel(description = "")
public class ApplicationDTO {

    @Valid
    @NotNull(message = "Property id cannot be null.")
    private String id = null;

    @Valid
    @NotNull(message = "Property subject cannot be null.")
    private String subject = null;

    @Valid
    @NotNull(message = "Property appName cannot be null.")
    private String appName = null;

    @Valid
    @NotNull(message = "Property appId cannot be null.")
    private String appId = null;

    /**
     * Unique ID of the application.
     **/
    @ApiModelProperty(required = true, value = "Unique ID of the application.")
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Username of the logged in user for the application.
     **/
    @ApiModelProperty(required = true, value = "Username of the logged in user for the application.")
    @JsonProperty("subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Name of the application.
     **/
    @ApiModelProperty(required = true, value = "Name of the application.")
    @JsonProperty("appName")
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * ID of the application.
     **/
    @ApiModelProperty(required = true, value = "ID of the application.")
    @JsonProperty("appId")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ApplicationDTO {\n");

        sb.append("    id: ").append(id).append("\n");
        sb.append("    subject: ").append(subject).append("\n");
        sb.append("    appName: ").append(appName).append("\n");
        sb.append("    appId: ").append(appId).append("\n");

        sb.append("}\n");
        return sb.toString();
    }
}
