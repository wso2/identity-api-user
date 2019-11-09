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

package org.wso2.carbon.identity.rest.api.user.application.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.application.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.application.v1.core.ApplicationService;

import javax.ws.rs.core.Response;

/**
 * User application service.
 */
public class MeApiServiceImpl implements MeApiService {

    @Autowired
    private ApplicationService applicationService;

    @Override
    public Response getApplication(String applicationId) {

        return Response.ok()
                .entity(applicationService.getApplication(applicationId)).build();
    }

    @Override
    public Response getApplications(String attributes, Integer limit, Integer offset, String filter, String
            sortOrder, String sortBy) {

        return Response.ok().entity(applicationService.getApplications(attributes, limit, offset, filter, sortOrder,
                sortBy)).build();
    }
}
