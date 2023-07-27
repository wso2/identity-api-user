/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com). All Rights Reserved.
 *
 * This software is the property of WSO2 LLC. and its suppliers, if any.
 * Dissemination of any information or reproduction of any material contained
 * herein in any form is strictly forbidden, unless permitted by WSO2 expressly.
 * You may not alter or remove any copyright or other notice from copies of this content.
 */

package org.wso2.carbon.identity.api.user.organization.v1.util;

import org.apache.commons.lang.ArrayUtils;
import org.wso2.carbon.identity.api.user.organization.v1.model.Error;
import org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementException;

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
