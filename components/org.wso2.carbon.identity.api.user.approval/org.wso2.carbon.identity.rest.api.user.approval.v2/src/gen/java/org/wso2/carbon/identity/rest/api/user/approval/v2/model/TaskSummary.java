/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.rest.api.user.approval.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class TaskSummary  {
  
    private String id;
    private String name;
    private String presentationSubject;
    private String presentationName;
    private String taskType;

@XmlType(name="StatusEnum")
@XmlEnum(String.class)
public enum StatusEnum {

    @XmlEnumValue("READY") READY(String.valueOf("READY")), @XmlEnumValue("RESERVED") RESERVED(String.valueOf("RESERVED")), @XmlEnumValue("COMPLETED") COMPLETED(String.valueOf("COMPLETED"));


    private String value;

    StatusEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static StatusEnum fromValue(String value) {
        for (StatusEnum b : StatusEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private StatusEnum status;
    private Integer priority;
    private String createdTimeInMillis;

    /**
    * Unique ID to represent an Approval Task
    **/
    public TaskSummary id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "451", value = "Unique ID to represent an Approval Task")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Unique name for the Approval Task
    **/
    public TaskSummary name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "s367:testTask", value = "Unique name for the Approval Task")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Display value for Approval Operation
    **/
    public TaskSummary presentationSubject(String presentationSubject) {

        this.presentationSubject = presentationSubject;
        return this;
    }
    
    @ApiModelProperty(example = "Add new Role", value = "Display value for Approval Operation")
    @JsonProperty("presentationSubject")
    @Valid
    public String getPresentationSubject() {
        return presentationSubject;
    }
    public void setPresentationSubject(String presentationSubject) {
        this.presentationSubject = presentationSubject;
    }

    /**
    * Display value for Approval Task
    **/
    public TaskSummary presentationName(String presentationName) {

        this.presentationName = presentationName;
        return this;
    }
    
    @ApiModelProperty(example = "sampleTask", value = "Display value for Approval Task")
    @JsonProperty("presentationName")
    @Valid
    public String getPresentationName() {
        return presentationName;
    }
    public void setPresentationName(String presentationName) {
        this.presentationName = presentationName;
    }

    /**
    * Type of the Approval
    **/
    public TaskSummary taskType(String taskType) {

        this.taskType = taskType;
        return this;
    }
    
    @ApiModelProperty(example = "TASK", value = "Type of the Approval")
    @JsonProperty("taskType")
    @Valid
    public String getTaskType() {
        return taskType;
    }
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    /**
    * State of the Approval task
    **/
    public TaskSummary status(StatusEnum status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(example = "READY", value = "State of the Approval task")
    @JsonProperty("status")
    @Valid
    public StatusEnum getStatus() {
        return status;
    }
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
    * Priority of the Approval task
    **/
    public TaskSummary priority(Integer priority) {

        this.priority = priority;
        return this;
    }
    
    @ApiModelProperty(example = "0", value = "Priority of the Approval task")
    @JsonProperty("priority")
    @Valid
    public Integer getPriority() {
        return priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
    * The time that the operation for approval initiated
    **/
    public TaskSummary createdTimeInMillis(String createdTimeInMillis) {

        this.createdTimeInMillis = createdTimeInMillis;
        return this;
    }
    
    @ApiModelProperty(example = "1565597569021", value = "The time that the operation for approval initiated")
    @JsonProperty("createdTimeInMillis")
    @Valid
    public String getCreatedTimeInMillis() {
        return createdTimeInMillis;
    }
    public void setCreatedTimeInMillis(String createdTimeInMillis) {
        this.createdTimeInMillis = createdTimeInMillis;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskSummary taskSummary = (TaskSummary) o;
        return Objects.equals(this.id, taskSummary.id) &&
            Objects.equals(this.name, taskSummary.name) &&
            Objects.equals(this.presentationSubject, taskSummary.presentationSubject) &&
            Objects.equals(this.presentationName, taskSummary.presentationName) &&
            Objects.equals(this.taskType, taskSummary.taskType) &&
            Objects.equals(this.status, taskSummary.status) &&
            Objects.equals(this.priority, taskSummary.priority) &&
            Objects.equals(this.createdTimeInMillis, taskSummary.createdTimeInMillis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, presentationSubject, presentationName, taskType, status, priority, createdTimeInMillis);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class TaskSummary {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    presentationSubject: ").append(toIndentedString(presentationSubject)).append("\n");
        sb.append("    presentationName: ").append(toIndentedString(presentationName)).append("\n");
        sb.append("    taskType: ").append(toIndentedString(taskType)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
        sb.append("    createdTimeInMillis: ").append(toIndentedString(createdTimeInMillis)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}

