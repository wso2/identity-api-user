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

package org.wso2.carbon.identity.rest.api.user.totp.v1.dto;


import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;





@ApiModel(description = "")
public class TOTPResponseDTO  {
  
  
  @Valid 
  private String qrCodeUrl = null;
  
  @Valid 
  private Boolean isValid = null;

  
  /**
   * QR Code URL for the authenticated user
   **/
  @ApiModelProperty(value = "QR Code URL for the authenticated user")
  @JsonProperty("qrCodeUrl")
  public String getQrCodeUrl() {
    return qrCodeUrl;
  }
  public void setQrCodeUrl(String qrCodeUrl) {
    this.qrCodeUrl = qrCodeUrl;
  }

  
  /**
   * Secret key of the authenticated user
   **/
  @ApiModelProperty(value = "Secret key of the authenticated user")
  @JsonProperty("isValid")
  public Boolean getIsValid() {
    return isValid;
  }
  public void setIsValid(Boolean isValid) {
    this.isValid = isValid;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class TOTPResponseDTO {\n");
    
    sb.append("  qrCodeUrl: ").append(qrCodeUrl).append("\n");
    sb.append("  isValid: ").append(isValid).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
