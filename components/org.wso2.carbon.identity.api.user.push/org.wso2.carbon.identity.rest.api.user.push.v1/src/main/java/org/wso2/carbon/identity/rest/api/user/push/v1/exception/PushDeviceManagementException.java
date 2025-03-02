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

package org.wso2.carbon.identity.rest.api.user.push.v1.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Custom exception for push device management.
 */
public class PushDeviceManagementException extends WebApplicationException {

    public PushDeviceManagementException(Response.Status status, Error error) {

        super(Response.status(status).entity(error).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .build());
    }

    public PushDeviceManagementException(Response.Status status) {

        super(Response.status(status).build());
    }
}
