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

import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.APICallDTO;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(description = "")
public class ResetCodeResponseDTO {

    @Valid 
    private String resetCode = null;

    @Valid 
    private List<APICallDTO> links = new ArrayList<APICallDTO>();

    /**
    * Password reset code to reset the password
    **/
    @ApiModelProperty(value = "Password reset code to reset the password")
    @JsonProperty("resetCode")
    public String getResetCode() {
        return resetCode;
    }
    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
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
        sb.append("class ResetCodeResponseDTO {\n");
        
        sb.append("    resetCode: ").append(resetCode).append("\n");
        sb.append("    links: ").append(links).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
