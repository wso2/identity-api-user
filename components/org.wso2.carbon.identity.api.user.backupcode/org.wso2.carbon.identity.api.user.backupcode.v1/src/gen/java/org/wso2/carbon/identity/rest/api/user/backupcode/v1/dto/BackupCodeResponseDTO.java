/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.backupcode.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

/**
 * Backup code response.
 **/
@ApiModel(description = "Backup code response.")
public class BackupCodeResponseDTO {

    @Valid 
    private List<String> backupCodes = new ArrayList<String>();

    /**
    * Backup codes of the authenticated user.
    **/
    @ApiModelProperty(value = "Backup codes of the authenticated user.")
    @JsonProperty("backupCodes")
    public List<String> getBackupCodes() {
        return backupCodes;
    }
    public void setBackupCodes(List<String> backupCodes) {
        this.backupCodes = backupCodes;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class BackupCodeResponseDTO {\n");
        
        sb.append("    backupCodes: ").append(backupCodes).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
