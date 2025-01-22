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

package org.wso2.carbon.identity.rest.api.user.push.v1.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.notification.push.device.handler.exception.PushDeviceHandlerClientException;
import org.wso2.carbon.identity.notification.push.device.handler.exception.PushDeviceHandlerException;

import javax.ws.rs.core.Response;

/**
 * Util class for push device management.
 */
public class Util {

    private static final Log LOG = LogFactory.getLog(Util.class);

    /**
     * Handle push device handler exception.
     *
     * @param e PushDeviceHandlerException.
     * @return APIError.
     */
    public static APIError handlePushDeviceHandlerException(PushDeviceHandlerException e) {

        if (e instanceof PushDeviceHandlerClientException) {
            ErrorResponse errorResponse = getErrorBuilder(e).build(LOG, e.getMessage());
            return new APIError(Response.Status.BAD_REQUEST, errorResponse);
        }
        ErrorResponse errorResponse = getErrorBuilder(e).build(LOG, e, e.getMessage());
        return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
    }

    /**
     * Get error builder.
     *
     * @param e PushDeviceHandlerException.
     * @return ErrorResponse.Builder.
     */
    private static ErrorResponse.Builder getErrorBuilder(PushDeviceHandlerException e) {

        return new ErrorResponse.Builder().withCode(e.getErrorCode()).withMessage(e.getMessage());
    }
}
