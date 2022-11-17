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
import java.io.InputStream;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;
import javax.ws.rs.*;

@Path("/me")
@Api(description = "The me API")

public class MeApi  {

    @Autowired
    private MeApiService delegate;

    @Valid
    @GET
    @Path("/approval-tasks/{task-id}")
    
    
    @ApiOperation(value = "Removed from API", notes = "Retrieves information of a specific approval task identified by the task(Removed from API) ", response = Void.class, tags={ "me", })
    @ApiResponses(value = { 
        @ApiResponse(code = 410, message = "Gone", response = Void.class)
    })
    public Response getApprovalTaskInfo(@ApiParam(value = "Task ID",required=true) @PathParam("task-id") String taskId) {

        return delegate.getApprovalTaskInfo(taskId );
    }

    @Valid
    @GET
    @Path("/approval-tasks")
    
    
    @ApiOperation(value = "Removed from API", notes = "Retrieve the available approval tasks in the system for the authenticated user.  (Removed from API) ", response = Void.class, tags={ "me", })
    @ApiResponses(value = { 
        @ApiResponse(code = 410, message = "Gone", response = Void.class)
    })
    public Response listApprovalTasksForLoggedInUser() {

        return delegate.listApprovalTasksForLoggedInUser();
    }

    @Valid
    @PUT
    @Path("/approval-tasks/{task-id}/state")
    
    
    @ApiOperation(value = "Removed from API", notes = "Update the approval task status (Removed from API) ", response = Void.class, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 410, message = "Gone", response = Void.class)
    })
    public Response updateStateOfTask(@ApiParam(value = "Task ID",required=true) @PathParam("task-id") String taskId) {

        return delegate.updateStateOfTask(taskId );
    }

}
