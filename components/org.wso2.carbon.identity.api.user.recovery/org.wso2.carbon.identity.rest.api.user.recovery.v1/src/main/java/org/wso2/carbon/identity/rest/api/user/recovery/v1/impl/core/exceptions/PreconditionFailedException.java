/*
 *  Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.exceptions;

import org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.Constants;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.ErrorResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.RetryErrorResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * PreconditionFailedException class.
 */
public class PreconditionFailedException extends WebApplicationException {

    /**
     * Exception message.
     */
    private String message;

    /**
     * Constructs a new exception from the ErrorDTO{@link ErrorResponse} object.
     *
     * @param errorResponse ErrorResponse{@link ErrorResponse} object holding the error code and the message
     */
    public PreconditionFailedException(ErrorResponse errorResponse) {

        super(Response.status(Response.Status.PRECONDITION_FAILED).entity(errorResponse)
                .header(Constants.HEADER_CONTENT_TYPE, Constants.DEFAULT_RESPONSE_CONTENT_TYPE).build());
        message = errorResponse.getDescription();
    }

    /**
     * Constructs a new exception from the RetryErrorDTO{@link RetryErrorResponse} object.
     *
     * @param retryErrorResponse RetryErrorResponse{@link RetryErrorResponse} object holding the error code and the
     *                           message
     */
    public PreconditionFailedException(RetryErrorResponse retryErrorResponse) {

        super(Response.status(Response.Status.PRECONDITION_FAILED).entity(retryErrorResponse)
                .header(Constants.HEADER_CONTENT_TYPE, Constants.DEFAULT_RESPONSE_CONTENT_TYPE).build());
        message = retryErrorResponse.getDescription();
    }

    /**
     * Constructs a new exception instance.
     */
    public PreconditionFailedException() {

        super(Response.Status.PRECONDITION_FAILED);
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
