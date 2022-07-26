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

package org.wso2.carbon.identity.api.user.approval.common;

import org.wso2.carbon.humantask.core.TaskOperationService;
import org.wso2.carbon.identity.workflow.engine.ApprovalEventService;

/**
 * Service holder class for user approvals.
 */
public class UserApprovalServiceHolder {

    private static TaskOperationService taskOperationService;
    private static ApprovalEventService approvalEventService;

    /**
     * Set TaskOperationService as OSGI service.
     *
     * @param taskOperationService taskOperationService.
     */
    public static void setTaskOperationService(TaskOperationService taskOperationService) {

        UserApprovalServiceHolder.taskOperationService = taskOperationService;
    }

    /**
     * Set ApprovalEventService as OSGI service.
     *
     * @param approvalEventService approvalEventService.
     */
    public static void setApprovalEventService(ApprovalEventService approvalEventService) {

        UserApprovalServiceHolder.approvalEventService = approvalEventService;
    }

    /**
     * Get TaskOperationService osgi service.
     *
     * @return TaskOperationService
     */
    public static TaskOperationService getTaskOperationService() {

        return taskOperationService;
    }

    /**
     * Get ApprovalEventService osgi service.
     *
     * @return ApprovalEventService
     */
    public static ApprovalEventService getApprovalEventService() {

        return approvalEventService;
    }

}

