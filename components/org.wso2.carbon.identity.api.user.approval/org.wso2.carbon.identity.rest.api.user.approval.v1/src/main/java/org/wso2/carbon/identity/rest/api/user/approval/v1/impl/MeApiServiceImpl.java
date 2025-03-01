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

package org.wso2.carbon.identity.rest.api.user.approval.v1.impl;

import org.wso2.carbon.identity.rest.api.user.approval.v1.*;
import org.wso2.carbon.identity.rest.api.user.approval.v1.core.factories.ApprovalEventServiceFactory;
import org.wso2.carbon.identity.rest.api.user.approval.v1.model.*;
import org.wso2.carbon.identity.workflow.engine.ApprovalEventService;

import java.util.List;

import javax.ws.rs.core.Response;

public class MeApiServiceImpl implements MeApiService {

    private final ApprovalEventService approvalEventService;
    public MeApiServiceImpl(){
        this.approvalEventService = ApprovalEventServiceFactory.getApprovalEventService();
    }

    @Override
    public Response getApprovalTaskInfo(String taskId) {

        // do some magic!
        return Response.ok().entity(approvalEventService.getTaskData(taskId)).build();
    }

    @Override
    public Response listApprovalTasksForLoggedInUser(Integer limit, Integer offset, List<String> status) {

        // do some magic!
        return Response.ok().entity(approvalEventService.listTasks(limit, offset, status)).build();
    }

    @Override
    public Response updateStateOfTask(String taskId, StateDTO nextState) {

        org.wso2.carbon.identity.workflow.engine.dto.StateDTO nextStateDTO = convertState(nextState);
        approvalEventService.updateStatus(taskId, nextStateDTO);

        return Response.ok().build();
    }

    private org.wso2.carbon.identity.workflow.engine.dto.StateDTO convertState(StateDTO nextState) {

        org.wso2.carbon.identity.workflow.engine.dto.StateDTO nextStateDTO = new org.wso2.carbon.identity.workflow.engine.dto.StateDTO();

        if (nextState.getAction() == StateDTO.ActionEnum.APPROVE) {
            nextStateDTO.setAction(org.wso2.carbon.identity.workflow.engine.dto.StateDTO.ActionEnum.APPROVE);
        } else if (nextState.getAction() == StateDTO.ActionEnum.REJECT) {
            nextStateDTO.setAction(org.wso2.carbon.identity.workflow.engine.dto.StateDTO.ActionEnum.REJECT);
        } else if (nextState.getAction() == StateDTO.ActionEnum.RELEASE) {
            nextStateDTO.setAction(org.wso2.carbon.identity.workflow.engine.dto.StateDTO.ActionEnum.RELEASE);
        } else if (nextState.getAction() == StateDTO.ActionEnum.CLAIM) {
            nextStateDTO.setAction(org.wso2.carbon.identity.workflow.engine.dto.StateDTO.ActionEnum.CLAIM);
        }

        return nextStateDTO;
    }
}
