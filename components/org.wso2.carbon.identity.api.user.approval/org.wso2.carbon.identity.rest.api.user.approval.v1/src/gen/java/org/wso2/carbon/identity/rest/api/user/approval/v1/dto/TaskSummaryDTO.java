/*
 * Copyright (c) 2019, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.rest.api.user.approval.v1.dto;


import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;





@ApiModel(description = "")
public class TaskSummaryDTO  {
  
  
  
  private String id = null;
  
  
  private String name = null;
  
  
  private String presentationSubject = null;
  
  
  private String presentationName = null;
  
  
  private String taskType = null;
  
  public enum StatusEnum {
     READY,  RESERVED,  COMPLETED, 
  };
  
  private StatusEnum status = null;
  
  
  private Integer priority = null;
  
  
  private String createdTimeInMillis = null;

  
  /**
   * Unique ID to represent an Approval Task
   **/
  @ApiModelProperty(value = "Unique ID to represent an Approval Task")
  @JsonProperty("id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  
  /**
   * Unique name for the Approval Task
   **/
  @ApiModelProperty(value = "Unique name for the Approval Task")
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  
  /**
   * Display value for Approval Operation
   **/
  @ApiModelProperty(value = "Display value for Approval Operation")
  @JsonProperty("presentationSubject")
  public String getPresentationSubject() {
    return presentationSubject;
  }
  public void setPresentationSubject(String presentationSubject) {
    this.presentationSubject = presentationSubject;
  }

  
  /**
   * Display value for Approval Task
   **/
  @ApiModelProperty(value = "Display value for Approval Task")
  @JsonProperty("presentationName")
  public String getPresentationName() {
    return presentationName;
  }
  public void setPresentationName(String presentationName) {
    this.presentationName = presentationName;
  }

  
  /**
   * Type of the Approval
   **/
  @ApiModelProperty(value = "Type of the Approval")
  @JsonProperty("taskType")
  public String getTaskType() {
    return taskType;
  }
  public void setTaskType(String taskType) {
    this.taskType = taskType;
  }

  
  /**
   * State of the Approval task
   **/
  @ApiModelProperty(value = "State of the Approval task")
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }
  public void setStatus(StatusEnum status) {
    this.status = status;
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
   * The time that the operation for approval initiated
   **/
  @ApiModelProperty(value = "The time that the operation for approval initiated")
  @JsonProperty("createdTimeInMillis")
  public String getCreatedTimeInMillis() {
    return createdTimeInMillis;
  }
  public void setCreatedTimeInMillis(String createdTimeInMillis) {
    this.createdTimeInMillis = createdTimeInMillis;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class TaskSummaryDTO {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  presentationSubject: ").append(presentationSubject).append("\n");
    sb.append("  presentationName: ").append(presentationName).append("\n");
    sb.append("  taskType: ").append(taskType).append("\n");
    sb.append("  status: ").append(status).append("\n");
    sb.append("  priority: ").append(priority).append("\n");
    sb.append("  createdTimeInMillis: ").append(createdTimeInMillis).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
