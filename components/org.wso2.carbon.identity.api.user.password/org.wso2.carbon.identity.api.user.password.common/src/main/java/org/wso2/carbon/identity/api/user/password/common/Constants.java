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

    public static final String PASSWORD_API_BASE_PATH = "/me/password";
    public static final String V1_API_PATH_COMPONENT = "/v1";
    public static final String PASSWORD_API_PATH = V1_API_PATH_COMPONENT + PASSWORD_API_BASE_PATH;

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        ERROR_CODE_INVALID_CURRENT_PASSWORD("60001",
                "Invalid current password",
                "The current password you entered is incorrect."),
        ERROR_CODE_PASSWORD_POLICY_VIOLATION("60002",
                "Password policy violation",
                "The new password does not meet the password policy requirements."),
        ERROR_CODE_INVALID_INPUT("60003",
                "Invalid input",
                "One or more input parameters are invalid."),
        ERROR_CODE_ERROR_UPDATING_PASSWORD("60004",
                "Error updating password",
                "An error occurred while updating the password."),
        ERROR_CODE_USER_NOT_FOUND("60005",
                "User not found",
                "The authenticated user could not be found.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {
            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public String getDescription() {
            return description;
        }
    }
}

