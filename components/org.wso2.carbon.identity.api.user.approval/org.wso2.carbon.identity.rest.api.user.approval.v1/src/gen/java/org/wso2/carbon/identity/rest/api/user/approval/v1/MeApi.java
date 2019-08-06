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
import org.wso2.carbon.identity.rest.api.user.approval.v1.dto.TaskSummeryDTO;
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
    
    
    @io.swagger.annotations.ApiOperation(value = "retrieves an approval task by id", notes = "Retrieve information of a specific human task.\n", response = TaskDataDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Detailed information of the approval task"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Element Already Exists"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response getApprovalTaskInfo(@ApiParam(value = "Task Id",required=true ) @PathParam("task-id")  String taskId)
    {
    return delegate.getApprovalTaskInfo(taskId);
    }
    @GET
    @Path("/approval-tasks")
    
    
    @io.swagger.annotations.ApiOperation(value = "searches available approvals for the authenticated user", notes = "Retrieve the available approval tasks in the system for the authenticated user. This API returns, all the claimable, assigned and completed approval tasks. User may invoke the endpoint with\n", response = TaskSummeryDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response listApprovalTasksForLoggedInUser(@ApiParam(value = "maximum number of records to return") @QueryParam("limit")  Integer limit,
    @ApiParam(value = "number of records to skip for pagination") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "Approval task's status:\n * RESERVED - Tasks assigned to the authenticated user.\n * READY - Tasks that can be assigned to and approved by the authenticated user.\n * COMPLETED - Tasks that are completed by the user\n * <empty> - All the task available to view will be retrieved if this parameter is not specified.\n") @QueryParam("status")  List<String> status)
    {
    return delegate.listApprovalTasksForLoggedInUser(limit,offset,status);
    }
    @PUT
    @Path("/approval-tasks/{task-id}/state")
    
    
    @io.swagger.annotations.ApiOperation(value = "change the state of an approval task", notes = "Update the approval task status by,\n CLAIM - Reserve for the user\n RELEASE - release for other users to claim\n APPROVE - approve the task\n REJECT - deny the task\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response updateStateOfTask(@ApiParam(value = "Task Id",required=true ) @PathParam("task-id")  String taskId,
    @ApiParam(value = "to which state the task should be changed"  ) StateDTO nextState)
    {
    return delegate.updateStateOfTask(taskId,nextState);
    }
}

