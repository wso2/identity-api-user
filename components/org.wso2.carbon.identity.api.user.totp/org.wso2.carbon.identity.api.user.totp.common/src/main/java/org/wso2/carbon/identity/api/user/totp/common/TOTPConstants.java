/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.user.totp.common;

/**
 * Contains all the TOTP related constants
 */
public class TOTPConstants {

    public static final String USER_TOTP_PREFIX = "OTP-";

    /**
     * Enum for totp related errors in the format of
     * Error Code - code to identify the error
     * Error Message - What went wrong
     * Error Description - Why it went wrong
     */
    public enum ErrorMessage {

        SERVER_ERROR_RETRIEVING_REALM_FOR_USER("15001",
                "Unable to retrieve the user realm.",
                "Can not find the user realm for the authenticated user."),
        SERVER_ERROR_DECRYPTING_SECRET("15002",
                "Error while decrypting the SecretKey",
                "TOTPAdminService failed while decrypt the stored SecretKey"),
        SERVER_ERROR_RETRIEVING_USERSTORE_MANAGER("15003,",
                "Unable to retrieve userstore manager",
                "TOTPService failed while trying to get the userstore manager from user realm of the user : %s"),
        SERVER_ERROR_GENERAL("15004,",
                "Server error occurred",
                "Unable to complete the action due to a server error"),
        USER_ERROR_UNAUTHORIZED_USER("10001",
                "Access denied.",
                "TOTPTokenVerifier cannot find the property value for encodingMethod."),
        USER_ERROR_INVALID_ACTION_ID("10002",
                "Invalid input provided.",
                "The provided action ID does not exist."),
        USER_ERROR_QR_CODE_URL_NOT_EXIST("10003",
                "QR Code URL doesn't exist.",
                "No QR Code URL registered for the authenticated user"),
        USER_ERROR_INVALID_VALIDATION_PARAMS("10004",
                "Verification code not present.",
                "Validation operation requires verification code to be present.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {
            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {
            return USER_TOTP_PREFIX + code;
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
