package org.wso2.carbon.identity.rest.api.user.session.v1.dto;


import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;





@ApiModel(description = "")
public class ApplicationDTO  {
  
  
  @NotNull
  private String subject = null;
  
  @NotNull
  private String appName = null;
  
  @NotNull
  private String appId = null;

  
  /**
   * Username for the application
   **/
  @ApiModelProperty(required = true, value = "Username for the application")
  @JsonProperty("subject")
  public String getSubject() {
    return subject;
  }
  public void setSubject(String subject) {
    this.subject = subject;
  }

  
  /**
   * Name of the application
   **/
  @ApiModelProperty(required = true, value = "Name of the application")
  @JsonProperty("appName")
  public String getAppName() {
    return appName;
  }
  public void setAppName(String appName) {
    this.appName = appName;
  }

  
  /**
   * ID of the application
   **/
  @ApiModelProperty(required = true, value = "ID of the application")
  @JsonProperty("appId")
  public String getAppId() {
    return appId;
  }
  public void setAppId(String appId) {
    this.appId = appId;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicationDTO {\n");
    
    sb.append("  subject: ").append(subject).append("\n");
    sb.append("  appName: ").append(appName).append("\n");
    sb.append("  appId: ").append(appId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
