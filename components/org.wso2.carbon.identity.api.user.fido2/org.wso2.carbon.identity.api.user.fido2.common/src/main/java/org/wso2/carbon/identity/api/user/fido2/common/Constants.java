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

    public static final String AUTHENTICATED_WITH_BASIC_AUTH = "AuthenticatedWithBasicAuth";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessages {

        ERROR_CODE_START_REGISTRATION("50001", "Error starting the fido device registration flow",
                "A system error occurred while serializing start registration response for the appid : %s"),
        ERROR_CODE_FINISH_REGISTRATION("50002", "Error finishing fido2 device registration process",
                "A system error occurred while finishing device registration"),
        ERROR_CODE_FINISH_REGISTRATION_BY_USER("50003", "Error finishing fido2 device " +
                "registration process", "A system error occurred while finishing device registration for " +
                "the finish registration response %s"),
        ERROR_CODE_FETCH_CREDENTIALS("50004", "Error while retrieving user credentials",
                                              "A system error occurred while retrieving user credentials " +
                                                      "for the user : %s"),
        ERROR_CODE_DELETE_CREDENTIALS("50005", "Error while deleting user credentials",
                "A system error occurred while deleting fido credential with credentialId : %s "),

        ERROR_CODE_USER_NOT_REGISTERED("10006", "Error while retrieving user credentials",
                                              "User doesn't have fido credentials"),
        ERROR_CODE_ACCESS_DENIED_FOR_BASIC_AUTH("50015", "Access denied.", "This method is "
                + "blocked for the requests with basic authentication.");

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
