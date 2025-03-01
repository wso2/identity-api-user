/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.approval.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.approval.v1.model.Property;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class TaskData  {
  
    private String id;
    private String subject;
    private String description;
    private Integer priority;
    private String initiator;

@XmlType(name="ApprovalStatusEnum")
@XmlEnum(String.class)
public enum ApprovalStatusEnum {

    @XmlEnumValue("PENDING") PENDING(String.valueOf("PENDING")), @XmlEnumValue("APPROVED") APPROVED(String.valueOf("APPROVED")), @XmlEnumValue("REJECTED") REJECTED(String.valueOf("REJECTED"));


    private String value;

    ApprovalStatusEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ApprovalStatusEnum fromValue(String value) {
        for (ApprovalStatusEnum b : ApprovalStatusEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private ApprovalStatusEnum approvalStatus;
    private List<Property> assignees = null;

    private List<Property> properties = null;


    /**
    * Unique ID to represent a approval task
    **/
    public TaskData id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "451", value = "Unique ID to represent a approval task")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Subject of the Approval
    **/
    public TaskData subject(String subject) {

        this.subject = subject;
        return this;
    }
    
    @ApiModelProperty(example = "Add new Role", value = "Subject of the Approval")
    @JsonProperty("subject")
    @Valid
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
    * Description on the Approval task
    **/
    public TaskData description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Addes a new role to the system", value = "Description on the Approval task")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Priority of the Approval task
    **/
    public TaskData priority(Integer priority) {

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
    * The user who initiated the task
    **/
    public TaskData initiator(String initiator) {

        this.initiator = initiator;
        return this;
    }
    
    @ApiModelProperty(example = "some-user-name", value = "The user who initiated the task")
    @JsonProperty("initiator")
    @Valid
    public String getInitiator() {
        return initiator;
    }
    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    /**
    * Available only for the completed Tasks, APPROVED or REJECTED if the task has been completed, PENDING otherwise 
    **/
    public TaskData approvalStatus(ApprovalStatusEnum approvalStatus) {

        this.approvalStatus = approvalStatus;
        return this;
    }
    
    @ApiModelProperty(example = "APPROVE", value = "Available only for the completed Tasks, APPROVED or REJECTED if the task has been completed, PENDING otherwise ")
    @JsonProperty("approvalStatus")
    @Valid
    public ApprovalStatusEnum getApprovalStatus() {
        return approvalStatus;
    }
    public void setApprovalStatus(ApprovalStatusEnum approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    /**
    * To whom the task is assigned:   * user - username(s) if the task is reserved for specific user(s).   * group - role name(s) if the task is assignable for group(s). 
    **/
    public TaskData assignees(List<Property> assignees) {

        this.assignees = assignees;
        return this;
    }
    
    @ApiModelProperty(value = "To whom the task is assigned:   * user - username(s) if the task is reserved for specific user(s).   * group - role name(s) if the task is assignable for group(s). ")
    @JsonProperty("assignees")
    @Valid
    public List<Property> getAssignees() {
        return assignees;
    }
    public void setAssignees(List<Property> assignees) {
        this.assignees = assignees;
    }

    public TaskData addAssigneesItem(Property assigneesItem) {
        if (this.assignees == null) {
            this.assignees = new ArrayList<>();
        }
        this.assignees.add(assigneesItem);
        return this;
    }

        /**
    **/
    public TaskData properties(List<Property> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("properties")
    @Valid
    public List<Property> getProperties() {
        return properties;
    }
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public TaskData addPropertiesItem(Property propertiesItem) {
        if (this.properties == null) {
            this.properties = new ArrayList<>();
        }
        this.properties.add(propertiesItem);
        return this;
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskData taskData = (TaskData) o;
        return Objects.equals(this.id, taskData.id) &&
            Objects.equals(this.subject, taskData.subject) &&
            Objects.equals(this.description, taskData.description) &&
            Objects.equals(this.priority, taskData.priority) &&
            Objects.equals(this.initiator, taskData.initiator) &&
            Objects.equals(this.approvalStatus, taskData.approvalStatus) &&
            Objects.equals(this.assignees, taskData.assignees) &&
            Objects.equals(this.properties, taskData.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, description, priority, initiator, approvalStatus, assignees, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class TaskData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
        sb.append("    initiator: ").append(toIndentedString(initiator)).append("\n");
        sb.append("    approvalStatus: ").append(toIndentedString(approvalStatus)).append("\n");
        sb.append("    assignees: ").append(toIndentedString(assignees)).append("\n");
        sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
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

