package org.wso2.carbon.identity.rest.api.user.approval.v1.core.functions;

import org.wso2.carbon.identity.rest.api.user.approval.v1.core.model.TaskModel;
import org.wso2.carbon.identity.rest.api.user.approval.v1.core.model.TaskParam;
import org.wso2.carbon.identity.rest.api.user.approval.v1.dto.PropertyDTO;
import org.wso2.carbon.identity.rest.api.user.approval.v1.dto.TaskDataDTO;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Transform TaskModel to TaskDataDTO
 */
public class TaskModelToExternal implements Function<TaskModel, TaskDataDTO> {

    @Override
    public TaskDataDTO apply(TaskModel taskModel) {

        TaskDataDTO taskDataDTO = new TaskDataDTO();
        taskDataDTO.setId(taskModel.getId());
        taskDataDTO.setSubject(taskModel.getTaskSubject());
        taskDataDTO.setDescription(taskModel.getTaskDescription());
        taskDataDTO.setApprovalStatus(TaskDataDTO.ApprovalStatusEnum.valueOf(taskModel.getApprovalStatus()));
        taskDataDTO.setInitiator(taskModel.getHtInitiator());
        taskDataDTO.setPriority(Integer.parseInt(taskModel.getPriority()));
        taskDataDTO.setAssignees(getPropertyDTOs(taskModel.getAssignees()));
        taskDataDTO.setProperties(getPropertyDTOs(taskModel.getParametersList()));
        return taskDataDTO;
    }

    private List<PropertyDTO> getPropertyDTOs(Map<String, String> props) {

        return props.entrySet().stream().map(p -> getPropertyDTO(p.getKey(), p.getValue()))
                .collect(Collectors.toList());
    }


    private List<PropertyDTO> getPropertyDTOs(List<TaskParam> props) {

        return props.stream().map(p -> getPropertyDTO(p.getItemName(), p.getItemValue()))
                .collect(Collectors.toList());
    }

    private PropertyDTO getPropertyDTO(String key, String value) {

        PropertyDTO prop = new PropertyDTO();
        prop.setKey(key);
        prop.setValue(value);
        return prop;
    }
}
