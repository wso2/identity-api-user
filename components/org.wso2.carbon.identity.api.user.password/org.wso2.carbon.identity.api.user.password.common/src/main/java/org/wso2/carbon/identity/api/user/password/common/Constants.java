/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.user.password.common;

/**
 * Password API constants.
 */
public class Constants {

    public static final String PASSWORD_API_BASE_PATH = "/me/change-password";
    public static final String V1_API_PATH_COMPONENT = "/v1";
    public static final String PASSWORD_API_PATH = V1_API_PATH_COMPONENT + PASSWORD_API_BASE_PATH;
    public static final String PASSWORD_ERROR_PREFIX = "PWD-";

    /**
     * Diagnostic log constants.
     */
    public static class LogConstants {

        public static final String USER_PASSWORD_API = "user-password-api";
        public static final String CHANGE_PASSWORD = "change-password";
    }

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        // Client errors.
        ERROR_CODE_INVALID_REQUEST_BODY("10000",
                "Invalid request",
                "The request is missing required data."),

        ERROR_CODE_INVALID_CURRENT_PASSWORD("10001",
                "Invalid current password",
                "The current password provided is incorrect."),

        ERROR_CODE_SAME_AS_CURRENT_PASSWORD("10002",
                "Cannot reuse current password",
                "The new password must be different from the current password."),

        ERROR_CODE_PASSWORD_POLICY_VIOLATION("10003",
                "Password policy violation",
                "The new password does not meet the complexity requirements of the current password policy."),

        ERROR_CODE_PASSWORD_UPDATE_ACTION_FAILURE("10004",
                "Password validation failed",
                "The new password does not meet the custom requirements configured for this organization."),

        ERROR_CODE_PASSWORD_UPDATE_CLIENT_ERROR("10005",
                "Password update failed",
                "The password update request could not be processed due to invalid data."),

        ERROR_CODE_READ_ONLY_USERSTORE("10006",
                "Password update not supported",
                "Password updates are not permitted in the current configuration state."),

        // Server errors.
        ERROR_CODE_ERROR_UPDATING_PASSWORD("15001",
                "Error updating password",
                "An unexpected error occurred while processing the password update request.");

        private final String code;
        private final String message;
        private final String description;

        /**
         * Constructor for ErrorMessage enum.
         *
         * @param code        Error code.
         * @param message     Error message.
         * @param description Error description.
         */
        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        /**
         * Get the error code with the password error prefix.
         *
         * @return Error code.
         */
        public String getCode() {

            return PASSWORD_ERROR_PREFIX + code;
        }

        /**
         * Get the error message.
         *
         * @return Error message.
         */
        public String getMessage() {

            return message;
        }

        /**
         * Get the error description.
         *
         * @return Error description.
         */
        public String getDescription() {

            return description;
        }
    }
}
