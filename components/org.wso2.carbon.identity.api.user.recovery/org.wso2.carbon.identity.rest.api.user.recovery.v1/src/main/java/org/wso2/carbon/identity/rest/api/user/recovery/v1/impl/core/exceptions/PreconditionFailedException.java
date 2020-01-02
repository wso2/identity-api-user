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

import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.ErrorResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.RetryErrorResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.Constants;

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
     * Constructs a new exception from the ErrorDTO{@link ErrorResponseDTO} object.
     *
     * @param errorDTO ErrorDTO{@link ErrorResponseDTO} object holding the error code and the message
     */
    public PreconditionFailedException(ErrorResponseDTO errorDTO) {

        super(Response.status(Response.Status.PRECONDITION_FAILED).entity(errorDTO)
                .header(Constants.HEADER_CONTENT_TYPE, Constants.DEFAULT_RESPONSE_CONTENT_TYPE).build());
        message = errorDTO.getDescription();
    }

    /**
     * Constructs a new exception from the RetryErrorDTO{@link RetryErrorResponseDTO} object.
     *
     * @param errorDTO RetryErrorDTO{@link RetryErrorResponseDTO} object holding the error code and the message
     */
    public PreconditionFailedException(RetryErrorResponseDTO errorDTO) {

        super(Response.status(Response.Status.PRECONDITION_FAILED).entity(errorDTO)
                .header(Constants.HEADER_CONTENT_TYPE, Constants.DEFAULT_RESPONSE_CONTENT_TYPE).build());
        message = errorDTO.getDescription();
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
