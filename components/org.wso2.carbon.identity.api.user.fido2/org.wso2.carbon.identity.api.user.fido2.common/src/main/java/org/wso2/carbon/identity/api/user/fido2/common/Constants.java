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

package org.wso2.carbon.identity.api.user.fido2.common;

/**
 * Common constants for authorized apps APIs.
 */
public class Constants {

    public static final String EQUAL_OPERATOR = "=";

    // OAuth Authorized Apps error code prefix.
    public static final String FIDO2_ERROR_CODE_PREFIX = "FID-";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessages {

        ERROR_CODE_START_REGISTRATION("10001", "Error starting the fido device registration flow",
                "A system error occurred while serializing start registration response for the appid : %s"),
        ERROR_CODE_FINISH_REGISTRATION("10002", "Error finishing fido2 device registration process",
                "A system error occurred while finishing device registration"),
        ERROR_CODE_FINISH_REGISTRATION_BY_USER("10003", "Error finishing fido2 device " +
                "registration process", "A system error occurred while finishing device registration for " +
                "the finish registration response %s"),
        ERROR_CODE_FETCH_CREDENTIALS("10004", "Error while retrieving user credentials",
                                              "A system error occurred while retrieving user credentials " +
                                                      "for the user : %s"),
        ERROR_CODE_DELETE_CREDENTIALS("10005", "Error while deleting user credentials",
                "A system error occurred while deleting fido credential with credentialId : %s ");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessages(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {
            return FIDO2_ERROR_CODE_PREFIX + code;
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
