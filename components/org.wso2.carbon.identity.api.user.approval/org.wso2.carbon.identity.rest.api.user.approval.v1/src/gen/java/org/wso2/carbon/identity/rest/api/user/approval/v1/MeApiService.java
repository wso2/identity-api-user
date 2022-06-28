/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.approval.v1;

import org.wso2.carbon.identity.workflow.engine.dto.StateDTO;

import java.util.List;
import javax.ws.rs.core.Response;

public abstract class MeApiService {
    public abstract Response getApprovalTaskInfo(String taskId);
    public abstract Response listApprovalTasksForLoggedInUser(Integer limit,Integer offset,List<String> status);
    public abstract Response updateStateOfTask(String taskId, StateDTO nextState);
}

