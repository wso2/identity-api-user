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

package org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.core;

/**
 * Class which contains the constants/
 * */
public class Constants {

    // OAuth Authorized Apps error code prefix.
    public static final String OAUTH_AUTHORIZED_APPS_ERROR_CODE_PREFIX = "OAA-";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessages {

        // Client errors.
        ERROR_CODE_INVALID_APPLICATION_ID("10001", "Invalid application ID",
                "An application with ID: %s cannot be found for user: %s."),

        // Server Errors.
        ERROR_CODE_GETTING_APPLICATION_INFORMATION("15001", "Error getting application information",
                "Error while getting the application: %s"),
        ERROR_CODE_REVOKE_APP_BY_USER("15002", "Error revoking authorized applications",
                "A system error occurred while revoking authorized applications for " +
                        "user: %s"),
        ERROR_CODE_REVOKE_APP_BY_ID_BY_USER("15003", "Error revoking authorized application",
                "A system error occurred while revoking authorized application: %s for " +
                        "user: %s"),
        ERROR_CODE_GET_APP_BY_ID_BY_USER("15004", "Error retrieving authorized application",
                "A system error occurred while retrieving authorized application: %s for " +
                        "user: %s"),
        ERROR_CODE_GET_APP_BY_USER("15005", "Error retrieving authorized applications",
                "A system error occurred while retrieving authorized applications for " +
                        "user: %s"),
        ERROR_CODE_REVOKE_TOKEN_BY_APP_ID("10006", "Error revoking issued tokens", "A system " +
                "error occurred while revoking issued tokens for application: %s"),
        ERROR_CODE_INVALID_INBOUND_PROTOCOL("10007", "Inbound protocol not found.", "Inbound " +
                "protocol cannot be found for the provided app id: %s");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessages(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return OAUTH_AUTHORIZED_APPS_ERROR_CODE_PREFIX + code;
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
