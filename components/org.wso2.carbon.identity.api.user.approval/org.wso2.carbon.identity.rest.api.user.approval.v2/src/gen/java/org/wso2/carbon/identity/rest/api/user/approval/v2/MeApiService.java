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

package org.wso2.carbon.identity.rest.api.user.approval.v2;

import java.util.List;
import org.wso2.carbon.identity.rest.api.user.approval.v2.model.StateDTO;
import javax.ws.rs.core.Response;


public interface MeApiService {

      public Response getApprovalTaskInfo(String taskId);

      public Response listApprovalTasksForLoggedInUser(Integer limit, Integer offset, List<String> status);

      public Response updateStateOfTask(String taskId, StateDTO stateDTO);
}
