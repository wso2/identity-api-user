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

package org.wso2.carbon.identity.rest.api.user.approval.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.user.approval.common.ApprovalConstant;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.rest.api.user.approval.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.approval.v1.core.UserApprovalService;
import org.wso2.carbon.identity.workflow.engine.ApprovalEventService;
import org.wso2.carbon.identity.workflow.engine.dto.StateDTO;

import java.util.List;
import javax.ws.rs.core.Response;

/**
 * API service implementation of a logged in user's approval operations
 */
public class MeApiServiceImpl extends MeApiService {

    private ApprovalEventService approvalEventService;
    private UserApprovalService userApprovalService;
    private static boolean enableSimpleWorkflowEngine = Boolean.parseBoolean(IdentityUtil.getProperty(
            ApprovalConstant.SIMPLE_WORKFLOW_ENGINE));

    public MeApiServiceImpl() {

    }

    @Autowired
    public MeApiServiceImpl(ApprovalEventService approvalEventService, UserApprovalService userApprovalService) {

        super();
        this.approvalEventService = approvalEventService;
        this.userApprovalService = userApprovalService;
    }

    @Override
    public Response getApprovalTaskInfo(String taskId) {

        if (enableSimpleWorkflowEngine) {
            return Response.ok().entity(approvalEventService.getTaskData(taskId)).build();
        }
        return Response.ok().entity(userApprovalService.getTaskData(taskId)).build();
    }

    @Override
    public Response listApprovalTasksForLoggedInUser(Integer limit, Integer offset, List<String> status) {

        if (enableSimpleWorkflowEngine) {
            return Response.ok().entity(approvalEventService.listTasks(limit, offset, status)).build();
        }
        return Response.ok().entity(userApprovalService.listTasks(limit, offset, status)).build();
    }

    @Override
    public Response updateStateOfTask(String taskId, StateDTO nextState) {

        if (enableSimpleWorkflowEngine) {
            approvalEventService.updateStatus(taskId, nextState );
            return Response.ok().build();
        }
        new UserApprovalService().updateStatus(taskId, nextState);
        return Response.ok().build();
    }
}
