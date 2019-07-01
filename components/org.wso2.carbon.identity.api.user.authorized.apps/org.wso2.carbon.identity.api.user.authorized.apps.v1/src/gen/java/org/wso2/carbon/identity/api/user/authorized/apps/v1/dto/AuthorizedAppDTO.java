package org.wso2.carbon.identity.api.user.authorized.apps.v1.dto;


import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;





@ApiModel(description = "")
public class AuthorizedAppDTO  {
  
  
  @NotNull
  private String appId = null;
  
  @NotNull
  private String description = null;
  
  @NotNull
  private String clientId = null;

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("appId")
  public String getAppId() {
    return appId;
  }
  public void setAppId(String appId) {
    this.appId = appId;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("clientId")
  public String getClientId() {
    return clientId;
  }
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthorizedAppDTO {\n");
    
    sb.append("  appId: ").append(appId).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  clientId: ").append(clientId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
