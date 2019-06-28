package org.wso2.carbon.identity.rest.api.user.association.v1.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;


@ApiModel(description = "")
public class AssociationRequestDTO  {
  
  
  
  private String associateUserId = null;
  
  
  private List<PropertyDTO> properties = new ArrayList<PropertyDTO>();

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("associateUserId")
  public String getAssociateUserId() {
    return associateUserId;
  }
  public void setAssociateUserId(String associateUserId) {
    this.associateUserId = associateUserId;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("properties")
  public List<PropertyDTO> getProperties() {
    return properties;
  }
  public void setProperties(List<PropertyDTO> properties) {
    this.properties = properties;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssociationRequestDTO {\n");
    
    sb.append("  associateUserId: ").append(associateUserId).append("\n");
    sb.append("  properties: ").append(properties).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
