/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.recovery.v2.impl.core.exceptions;

import org.wso2.carbon.identity.rest.api.user.recovery.v2.impl.core.Constants;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.ErrorResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * NotFoundException class.
 */
public class NotFoundException extends WebApplicationException {

    /**
     * Exception message.
     */
    private String message;

    /**
     * Constructs a new exception from the ErrorDTO{@link ErrorResponse} object.
     *
     * @param errorResponse ErrorResponse{@link ErrorResponse} object holding the error code and the message
     */
    public NotFoundException(ErrorResponse errorResponse) {

        super(Response.status(Response.Status.NOT_FOUND).entity(errorResponse)
                .header(Constants.HEADER_CONTENT_TYPE, Constants.DEFAULT_RESPONSE_CONTENT_TYPE).build());
        message = errorResponse.getDescription();
    }

    /**
     * Constructs a new exception instance.
     */
    public NotFoundException() {
        super(Response.Status.NOT_ACCEPTABLE);
    }

    /**
     * Get exception message.
     *
     * @return Exception message
     */
    @Override
    public String getMessage() {
        return message;
    }
}
