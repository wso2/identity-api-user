package org.wso2.carbon.identity.rest.api.user.session.v1.dto;

import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionDTO;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;





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
