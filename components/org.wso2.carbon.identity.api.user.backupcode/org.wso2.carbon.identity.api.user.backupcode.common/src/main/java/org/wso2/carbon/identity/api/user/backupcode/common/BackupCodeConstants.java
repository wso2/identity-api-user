/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.api.user.backupcode.common;

/**
 * Contains all the Backup Code related constants.
 */
public class BackupCodeConstants {

    public static final String USER_BACKUP_CODE_PREFIX = "BCA-";
    public static final String AUTHENTICATED_WITH_BASIC_AUTH = "AuthenticatedWithBasicAuth";

    /**
     * Enum for backup code related errors in the format of.
     * Error Code - code to identify the error.
     * Message - What went wrong.
     * Error Description - Why it went wrong.
     */
    public enum ErrorMessage {

        SERVER_ERROR_RETRIEVING_REALM_FOR_USER("15001",
                "Unable to retrieve the user realm.",
                "Can not find the user realm for the authenticated user."),
        SERVER_ERROR_DELETING_BACKUP_CODES("15002",
                "Unable to delete user backup codes.",
                "Cannot delete backup codes of the authenticated user."),
        SERVER_ERROR_RETRIEVE_BACKUP_CODES("15003",
                "Unable to retrieve user backup codes.",
                "Cannot retrieve backup codes of the authenticated user."),
        SERVER_ERROR_REFRESH_BACKUP_CODES("15004",
                "Unable to refresh user backup codes.",
                "Cannot refresh backup codes of the authenticated user."),
        SERVER_ERROR_INIT_BACKUP_CODES("15005",
                "Unable to initialize user backup codes.",
                "Cannot initialize backup codes of the authenticated user."),
        SERVER_ERROR_GENERAL("15006,",
                "Server error occurred",
                "Unable to complete the action due to a server error"),
        USER_ERROR_UNAUTHORIZED_USER("10001",
                "Access denied.",
                "BackupCode verifier cannot find the property value for encodingMethod."),
        USER_ERROR_INVALID_ACTION_ID("10002",
                "Invalid input provided.",
                "The provided action ID does not exist."),
        USER_ERROR_ACCESS_DENIED_FOR_BASIC_AUTH("10005",
                "Access denied.",
                "This method is blocked for the requests with basic authentication.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return USER_BACKUP_CODE_PREFIX + code;
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
