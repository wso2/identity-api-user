package org.wso2.carbon.identity.rest.api.user.approval.v1.core.model;

import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;

/**
 * Model Class for task
 */
public class TaskModel {

    private String id;
    private String taskSubject;
    private String taskDescription;
    private String priority;
    private String htInitiator;
    private String approvalStatus;
    private Map<String, String> assignees;

    @XmlElement(name = "xsd-complex-type-wrapper")
    private List<TaskParam> parameters;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskSubject() {
        return taskSubject;
    }

    public void setTaskSubject(String taskSubject) {
        this.taskSubject = taskSubject;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getHtInitiator() {
        return htInitiator;
    }

    public void setHtInitiator(String htInitiator) {
        this.htInitiator = htInitiator;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Map<String, String> getAssignees() {
        return assignees;
    }

    public void setAssignees(Map<String, String> assignees) {
        this.assignees = assignees;
    }

    public List<TaskParam> getParametersList() {
        return parameters;
    }

    public void setParametersList(List<TaskParam> parametersList) {
        this.parameters = parametersList;
    }
}
