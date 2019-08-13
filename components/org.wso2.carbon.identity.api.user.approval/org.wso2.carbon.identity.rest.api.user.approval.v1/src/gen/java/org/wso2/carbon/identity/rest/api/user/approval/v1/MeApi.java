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

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.approval.v1.dto.*;
import org.wso2.carbon.identity.rest.api.user.approval.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.approval.v1.factories.MeApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.rest.api.user.approval.v1.dto.TaskDataDTO;
import org.wso2.carbon.identity.rest.api.user.approval.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.user.approval.v1.dto.TaskSummaryDTO;
import org.wso2.carbon.identity.rest.api.user.approval.v1.dto.StateDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/me")


@io.swagger.annotations.Api(value = "/me", description = "the me API")
public class MeApi  {

   @Autowired
   private MeApiService delegate;

    @GET
    @Path("/approval-tasks/{task-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "Retrieves an approval task by the task-id", notes = "Retrieves information of a specific approval task identified by the task-id <br/>\n<b>Permission required:</b>\n * /permission/admin/login\n", response = TaskDataDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Detailed information of the approval task identified by the task-id"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Element Already Exists"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response getApprovalTaskInfo(@ApiParam(value = "Task ID",required=true ) @PathParam("task-id")  String taskId)
    {
    return delegate.getApprovalTaskInfo(taskId);
    }
    @GET
    @Path("/approval-tasks")
    
    
    @io.swagger.annotations.ApiOperation(value = "Retrieves available approvals for the authenticated user", notes = "Retrieve the available approval tasks in the system for the authenticated user. This API returns the following types of approvals:\n  * READY - Tasks that are _claimable_ by the user. User is eligible to assign the task to himself and complete it, if a particular task is in READY state.\n  * RESERVED -  Tasks that are _assigned_ to the user and to be approved by this user.\n  * COMPLETED - Tasks that are already _completed_ (approved or denied) by this user.\n\n<b>Permission required:</b>\n * /permission/admin/login\n\n A user can also invoke the endpoint with the following query parameters.\n", response = TaskSummaryDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Array of approval tasks matching the search criteria"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response listApprovalTasksForLoggedInUser(@ApiParam(value = "Maximum number of records to return") @QueryParam("limit")  Integer limit,
    @ApiParam(value = "Number of records to skip for pagination") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "Approval task's status to filter tasks by their status:\n * **RESERVED** - Tasks that are **assigned to** the authenticated user.\n * **READY** - Tasks that **can be assigned to** and **can be approved by** the authenticated user.\n * **COMPLETED** - Tasks that are **completed by** the user\n * \\<empty\\> - **All** the viewable tasks will be retrieved if this parameter is not specified.\n") @QueryParam("status")  List<String> status)
    {
    return delegate.listApprovalTasksForLoggedInUser(limit,offset,status);
    }
    @PUT
    @Path("/approval-tasks/{task-id}/state")
    
    
    @io.swagger.annotations.ApiOperation(value = "Changes the state of an approval task", notes = "Update the approval task status by defining one of the following actions:\n * CLAIM - Reserve the task for the user. Status of the task will be changed from READY to RESERVED.\n * RELEASE - Release the task for other users to claim. Status of the task will be changed from RESERVED to READY.\n * APPROVE - Approve the task. Status of the task will be changed to COMPLETED.\n * REJECT - Deny the task. Status of the task will be changed to COMPLETED.\n <br/>\n\n<b>Permission required:</b>\n * /permission/admin/login\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response updateStateOfTask(@ApiParam(value = "Task ID",required=true ) @PathParam("task-id")  String taskId,
    @ApiParam(value = "To which state the task should be changed."  ) StateDTO nextState)
    {
    return delegate.updateStateOfTask(taskId,nextState);
    }
}

