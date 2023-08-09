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

package org.wso2.carbon.identity.rest.api.user.organization.v1.util;

import org.apache.commons.lang.ArrayUtils;
import org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementException;
import org.wso2.carbon.identity.rest.api.user.organization.v1.model.Error;

/**
 * This class provides util functions to the user organization management endpoint.
 */
public class Util {

    /**
     * Returns a generic error object.
     *
     * @param errorMessages The error enum.
     * @param data          The error message data.
     * @return A generic error with the specified details.
     */
    public static Error getError(OrganizationManagementConstants.ErrorMessages errorMessages, String... data) {

        Error error = new Error();
        error.setCode(errorMessages.getCode());
        error.setMessage(errorMessages.getMessage());
        String description = errorMessages.getDescription();
        if (ArrayUtils.isNotEmpty(data)) {
            description = String.format(description, data);
        }
        error.setDescription(description);
        return error;
    }

    /**
     * Returns a generic error object.
     *
     * @param exception OrganizationManagementException.
     * @return A generic error with the specified details.
     */
    public static Error getError(OrganizationManagementException exception) {

        Error error = new Error();
        error.setCode(exception.getErrorCode());
        error.setMessage(exception.getMessage());
        error.setDescription(exception.getDescription());
        return error;
    }
}
