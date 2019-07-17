package org.wso2.carbon.identity.rest.api.user.approval.v1.core.functions;

import org.wso2.carbon.humantask.client.api.types.TTaskSimpleQueryResultRow;
import org.wso2.carbon.identity.rest.api.user.approval.v1.dto.TaskSummeryDTO;

import java.util.function.Function;

/**
 * Transform TTaskSimpleQueryResultRow to TaskSummeryDTO
 */
public class TTaskSimpleQueryResultRowToExternal implements Function<TTaskSimpleQueryResultRow, TaskSummeryDTO> {

    @Override
    public TaskSummeryDTO apply(TTaskSimpleQueryResultRow tTaskSimpleQueryResultRow) {
        TaskSummeryDTO summeryDTO = new TaskSummeryDTO();
        summeryDTO.setId(tTaskSimpleQueryResultRow.getId().getPath());
        summeryDTO.setName(tTaskSimpleQueryResultRow.getName().toString());
        summeryDTO.setTaskType(tTaskSimpleQueryResultRow.getTaskType());
        summeryDTO.setPresentationName(tTaskSimpleQueryResultRow.getPresentationName().getTPresentationName());
        summeryDTO.setPresentationSubject(tTaskSimpleQueryResultRow.getPresentationSubject().getTPresentationSubject());
        summeryDTO.setCreatedTimeInMillis(String.valueOf(tTaskSimpleQueryResultRow.getCreatedTime().getTimeInMillis()));
        summeryDTO.setPriority(tTaskSimpleQueryResultRow.getPriority().getTPriority().intValue());
        summeryDTO.setStatus(tTaskSimpleQueryResultRow.getStatus().getTStatus());
        return summeryDTO;
    }
}
