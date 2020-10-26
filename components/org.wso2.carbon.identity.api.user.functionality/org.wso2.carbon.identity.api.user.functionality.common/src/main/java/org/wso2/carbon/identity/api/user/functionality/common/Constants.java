/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.user.functionality.common;

/**
 * Common Constants class.
 */
public class Constants {

    public static final String FUNCTIONALITY_ERROR_PREFIX = "UFM-";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessages {

        // Client errors.
        ERROR_CODE_UNSUPPORTED_PARAMETERS_FOR_UNLOCK("10001", "Unsupported parameter",
                "Only action parameter is needed for unlocking a functionality."),
        ERROR_CODE_USER_NOT_PERMITTED_TO_UNLOCK("10002", "User is not permitted to unlock", "The user is not " +
                "permitted to unlock the functionality."),
        ERROR_CODE_INVALID_USERID("10003", "Invalid UserID provided.", "The provided userId is invalid."),
        ERROR_CODE_BAD_REQUEST("10004", "Invalid Request.", "The request provided is invalid."),
        ERROR_CODE_FUNCTIONALITY_ALREADY_LOCKED("10005", "Functionality is already locked.", "This functionality is " +
                "already locked"),

        // Server errors.
        ERROR_CODE_GET_LOCK_STATUS_FAILED("15001", "Failed to get the lock status", "A system error occurred while " +
                "getting status of the functionality."),
        ERROR_CODE_LOCK_THE_FUNCTIONALITY_FAILED("15002", "Failed to lock the functionality",
                "A system error occurred while locking the functionality."),
        ERROR_CODE_UNLOCK_THE_FUNCTIONALITY_FAILED("15003", "Failed to unlock the functionality",
                "A system error occurred while locking the functionality.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessages(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return FUNCTIONALITY_ERROR_PREFIX + code;
        }

        public String getMessage() {

            return message;
        }

        public String getDescription() {

            return description;
        }

        @Override
        public String toString() {

            return getCode() + " | " + getMessage() + " | " + getDescription();
        }
    }
}
