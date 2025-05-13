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

package org.wso2.carbon.identity.rest.api.user.approval.v1;

import java.util.List;

import org.wso2.carbon.identity.rest.api.user.approval.v1.factories.MeApiServiceFactory;
import org.wso2.carbon.identity.rest.api.user.approval.v1.model.Error;
import org.wso2.carbon.identity.rest.api.user.approval.v1.model.StateDTO;
import org.wso2.carbon.identity.rest.api.user.approval.v1.model.TaskData;
import org.wso2.carbon.identity.rest.api.user.approval.v1.model.TaskSummary;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/me")
@Api(description = "The me API")

public class MeApi  {

    private final MeApiService delegate;

    public MeApi(){

        this.delegate = MeApiServiceFactory.getMeApi();

    }

    @Valid
    @GET
    @Path("/approval-tasks/{task-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves an approval task by the task-id", notes = "Retrieves information of a specific approval task identified by the task-id  ", response = TaskData.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "me", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Detailed information of the approval task identified by the task-id", response = TaskData.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 409, message = "Element Already Exists", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getApprovalTaskInfo(@ApiParam(value = "Task ID",required=true) @PathParam("task-id") String taskId) {

        return delegate.getApprovalTaskInfo(taskId );
    }

    @Valid
    @GET
    @Path("/approval-tasks")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves available approvals for the authenticated user", notes = "Retrieve the available approval tasks in the system for the authenticated user. This API returns the following types of approvals:   * READY - Tasks that are _claimable_ by the user. If a particular task is in the READY state, the user is eligible to self-assign the task and complete it.   * RESERVED -  Tasks that are _assigned_ to the user and to be approved by this user.   * COMPLETED - Tasks that are already _completed_ (approved or denied) by this user.   A user can also invoke the endpoint with the following query parameters. ", response = TaskSummary.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "me", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Array of approval tasks matching the search criteria", response = TaskSummary.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response listApprovalTasksForLoggedInUser(    @Valid @Min(0)@ApiParam(value = "Maximum number of records to return")  @QueryParam("limit") Integer limit,     @Valid @Min(0)@ApiParam(value = "Number of records to skip for pagination")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Approval task's status to filter tasks by their status:  * **RESERVED** - Tasks that are **assigned to** the authenticated user.  * **READY** - Tasks that **can be assigned to** and **can be approved by** the authenticated user.  * **COMPLETED** - Tasks that are **completed by** the user  * \\<empty\\> - **All** the viewable tasks will be retrieved if this parameter is not specified. ")  @QueryParam("status") List<String> status) {

        return delegate.listApprovalTasksForLoggedInUser(limit,  offset,  status );
    }

    @Valid
    @PUT
    @Path("/approval-tasks/{task-id}/state")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Changes the state of an approval task", notes = "Update the approval task status by defining one of the following actions:  * CLAIM - Reserve the task for the user. Status of the task will be changed from READY to RESERVED.  * RELEASE - Release the task for other users to claim. Status of the task will be changed from RESERVED to READY.  * APPROVE - Approve the task. Status of the task will be changed to COMPLETED.  * REJECT - Deny the task. Status of the task will be changed to COMPLETED. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response updateStateOfTask(@ApiParam(value = "Task ID",required=true) @PathParam("task-id") String taskId, @ApiParam(value = "To which state the task should be changed." ) @Valid StateDTO stateDTO) {

        return delegate.updateStateOfTask(taskId,  stateDTO );
    }

}
