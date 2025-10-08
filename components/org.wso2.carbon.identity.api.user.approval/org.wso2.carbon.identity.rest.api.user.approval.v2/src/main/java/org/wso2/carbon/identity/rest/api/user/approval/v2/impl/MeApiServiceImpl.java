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

package org.wso2.carbon.identity.rest.api.user.approval.v2.impl;

import org.wso2.carbon.identity.api.user.approval.common.ApprovalConstant;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.rest.api.user.approval.v2.MeApiService;
import org.wso2.carbon.identity.rest.api.user.approval.v2.core.factories.ApprovalEventServiceFactory;
import org.wso2.carbon.identity.rest.api.user.approval.v2.model.StateDTO;
import org.wso2.carbon.identity.workflow.engine.ApprovalTaskService;
import org.wso2.carbon.identity.workflow.engine.dto.ApprovalTaskDTO;
import org.wso2.carbon.identity.workflow.engine.exception.WorkflowEngineException;

import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.approval.common.Util.handleError;

/**
 * Class for approval management.
 */
public class MeApiServiceImpl implements MeApiService {

    private final ApprovalTaskService approvalEventService;

    public MeApiServiceImpl() {

        this.approvalEventService = ApprovalEventServiceFactory.getApprovalEventService();
    }

    @Override
    public Response getApprovalTaskInfo(String taskId) {

        try {
            ApprovalTaskDTO task = approvalEventService.getApprovalTaskByTaskId(taskId);
            if (task == null) {
                throw new APIError(Response.Status.NOT_FOUND, new ErrorResponse.Builder().withCode(
                                ApprovalConstant.ErrorMessage.USER_ERROR_NON_EXISTING_TASK_ID.getCode())
                        .withMessage(ApprovalConstant.ErrorMessage.USER_ERROR_NON_EXISTING_TASK_ID.getMessage())
                        .build());
            }
            return Response.ok().entity(task).build();
        } catch (WorkflowEngineException e) {
            throw handleError(e);
        }
    }

    @Override
    public Response listApprovalTasksForLoggedInUser(Integer limit, Integer offset, List<String> status) {

        try {
            return Response.ok().entity(approvalEventService.listApprovalTasks(limit, offset, status)).build();
        } catch (WorkflowEngineException e) {
            throw handleError(e);
        }
    }

    @Override
    public Response updateStateOfTask(String taskId, StateDTO nextState) {

        org.wso2.carbon.identity.workflow.engine.dto.StateDTO nextStateDTO = convertState(nextState);
        try {
            approvalEventService.updateApprovalTaskStatus(taskId, nextStateDTO);
            return Response.ok().build();
        } catch (WorkflowEngineException e) {
            throw handleError(e);
        }
    }

    private org.wso2.carbon.identity.workflow.engine.dto.StateDTO convertState(StateDTO nextState) {

        org.wso2.carbon.identity.workflow.engine.dto.StateDTO nextStateDTO =
                new org.wso2.carbon.identity.workflow.engine.dto.StateDTO();

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
