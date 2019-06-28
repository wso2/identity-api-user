package org.wso2.carbon.identity.rest.api.user.association.v1.dto;


import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;


@ApiModel(description = "")
public class UserDTO  {
  
  
  
  private String userId = null;
  
  
  private String username = null;
  
  
  private String userStoreDomain = null;

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("userId")
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("userStoreDomain")
  public String getUserStoreDomain() {
    return userStoreDomain;
  }
  public void setUserStoreDomain(String userStoreDomain) {
    this.userStoreDomain = userStoreDomain;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserDTO {\n");
    
    sb.append("  userId: ").append(userId).append("\n");
    sb.append("  username: ").append(username).append("\n");
    sb.append("  userStoreDomain: ").append(userStoreDomain).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
