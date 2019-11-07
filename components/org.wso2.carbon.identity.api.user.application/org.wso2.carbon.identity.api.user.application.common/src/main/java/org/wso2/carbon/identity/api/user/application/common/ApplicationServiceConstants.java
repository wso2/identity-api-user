/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.user.application.common;

/**
 * Constants related to Application Service
 */
public class ApplicationServiceConstants {

    private static final String APPLICATION_SERVICE_RESPONSE_CODE_PREFIX = "APP-";

    /**
     * Enum for application service related errors in the format of
     * Error Code - code to identify the error
     * Error Message - What went wrong
     * Error Description - Why it went wrong
     */
    public enum ErrorMessage {

        ERROR_CODE_APPLICATION_NOT_FOUND("10001",
                "Resource not found.",
                "Unable to find any application with the provided identifier %s."),
        ERROR_CODE_UNSUPPORTED_FILTER_OPERATION("10002",
                "Filter operation not supported.",
                "Filter operation: %s is not supported."),
        ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE("10002", "Filtering not supported for the given attribute.",
                "Filtering from %s is not supported."),
        ERROR_CODE_INVALID_FILTER_QUERY("10003", "Invalid filter query.",
                "Provided filter query is invalid."),
        ERROR_CODE_ERROR_RETRIEVING_APPLICATIONS("15001",
                "Unable to get applications.",
                "Server Encountered an error while retrieving applications."),
        ERROR_CODE_ERROR_RETRIEVING_APPLICATION("150002",
                "Unable to get application.",
                "Server Encountered an error while retrieving application."),
        ERROR_CODE_ATTRIBUTE_FILTERING_NOT_IMPLEMENTED("15004",
                "Attribute filtering not supported.",
                "Attribute filtering capabilities are not supported in this version of the API."),
        ERROR_CODE_SORTING_NOT_IMPLEMENTED("15007",
                "Sorting not supported.",
                "Sorting capability is not supported in this version of the API.");
        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return APPLICATION_SERVICE_RESPONSE_CODE_PREFIX + code;
        }

        public String getMessage() {

            return message;
        }

        public String getDescription() {

            return description;
        }

        @Override
        public String toString() {

            return code + " | " + message;
        }
    }

}
