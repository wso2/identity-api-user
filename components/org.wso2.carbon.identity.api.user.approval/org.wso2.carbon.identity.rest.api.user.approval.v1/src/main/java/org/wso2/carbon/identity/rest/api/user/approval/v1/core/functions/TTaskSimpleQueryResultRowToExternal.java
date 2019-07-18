/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
