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

import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionDTO;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;





@ApiModel(description = "")
public class SessionsDTO  {
  
  
  @NotNull 
  private String userId = null;
  
  
  private List<SessionDTO> sessions = new ArrayList<SessionDTO>();

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("userId")
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }

  
  /**
   * List of active sessions
   **/
  @ApiModelProperty(value = "List of active sessions")
  @JsonProperty("sessions")
  public List<SessionDTO> getSessions() {
    return sessions;
  }
  public void setSessions(List<SessionDTO> sessions) {
    this.sessions = sessions;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class SessionsDTO {\n");
    
    sb.append("  userId: ").append(userId).append("\n");
    sb.append("  sessions: ").append(sessions).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
