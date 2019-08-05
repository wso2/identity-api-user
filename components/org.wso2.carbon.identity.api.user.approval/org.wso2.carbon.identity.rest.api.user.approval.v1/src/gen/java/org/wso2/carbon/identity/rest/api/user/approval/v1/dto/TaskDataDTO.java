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

package org.wso2.carbon.identity.rest.api.user.approval.v1.dto;

import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.approval.v1.dto.PropertyDTO;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;





@ApiModel(description = "")
public class TaskDataDTO  {
  
  
  
  private String id = null;
  
  
  private String subject = null;
  
  
  private String description = null;
  
  
  private Integer priority = null;
  
  
  private String initiator = null;
  
  public enum ApprovalStatusEnum {
     PENDING,  APPROVED,  REJECTED, 
  };
  
  private ApprovalStatusEnum approvalStatus = null;
  
  
  private List<PropertyDTO> assignees = new ArrayList<PropertyDTO>();
  
  
  private List<PropertyDTO> properties = new ArrayList<PropertyDTO>();

  
  /**
   * Unique Id to represent a approval task
   **/
  @ApiModelProperty(value = "Unique Id to represent a approval task")
  @JsonProperty("id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  
  /**
   * Subject of the Approval
   **/
  @ApiModelProperty(value = "Subject of the Approval")
  @JsonProperty("subject")
  public String getSubject() {
    return subject;
  }
  public void setSubject(String subject) {
    this.subject = subject;
  }

  
  /**
   * Description on the Approval task
   **/
  @ApiModelProperty(value = "Description on the Approval task")
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  
  /**
   * Priority of the Approval task
   **/
  @ApiModelProperty(value = "Priority of the Approval task")
  @JsonProperty("priority")
  public Integer getPriority() {
    return priority;
  }
  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  
  /**
   * The user who initiated the task
   **/
  @ApiModelProperty(value = "The user who initiated the task")
  @JsonProperty("initiator")
  public String getInitiator() {
    return initiator;
  }
  public void setInitiator(String initiator) {
    this.initiator = initiator;
  }

  
  /**
   * Available only for the completed Tasks, APPROVED or REJECTED if the task has been completed, PENDING otherwise
   **/
  @ApiModelProperty(value = "Available only for the completed Tasks, APPROVED or REJECTED if the task has been completed, PENDING otherwise")
  @JsonProperty("approvalStatus")
  public ApprovalStatusEnum getApprovalStatus() {
    return approvalStatus;
  }
  public void setApprovalStatus(ApprovalStatusEnum approvalStatus) {
    this.approvalStatus = approvalStatus;
  }

  
  /**
   * To whome the task is assigned:\n  * user - username(s) if the task is reserved for specific user(s).\n  * group - role name(s) if the task is assignable for group(s).\n
   **/
  @ApiModelProperty(value = "To whome the task is assigned:\n  * user - username(s) if the task is reserved for specific user(s).\n  * group - role name(s) if the task is assignable for group(s).\n")
  @JsonProperty("assignees")
  public List<PropertyDTO> getAssignees() {
    return assignees;
  }
  public void setAssignees(List<PropertyDTO> assignees) {
    this.assignees = assignees;
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
    sb.append("class TaskDataDTO {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  subject: ").append(subject).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  priority: ").append(priority).append("\n");
    sb.append("  initiator: ").append(initiator).append("\n");
    sb.append("  approvalStatus: ").append(approvalStatus).append("\n");
    sb.append("  assignees: ").append(assignees).append("\n");
    sb.append("  properties: ").append(properties).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
