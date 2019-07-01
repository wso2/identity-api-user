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

package org.wso2.carbon.identity.api.user.authorized.apps.v1.core;

public class Constants {

    // OAuth Authorized Apps error code prefix.
    public static final String OAUTH_AUTHORIZED_APPS_ERROR_CODE_PREFIX = "OAA-";

    public enum ErrorMessages {

        ERROR_CODE_INVALID_APPLICATION_ID("10001", "Invalid application ID", "An application with ID: %s cannot " +
                                                                                 "be found for user:%s."),
        ERROR_CODE_GET_APP_BY_ID_BY_USER("10002", "Error retrieving authorized application",
                                         "A system error occurred while retrieving authorized application: %s for " +
                                         "user: %s"),
        ERROR_CODE_GET_APP_BY_USER("10003", "Error retrieving authorized applications",
                                         "A system error occurred while retrieving authorized applications for " +
                                         "user: %s"),
        ERROR_CODE_REVOKE_APP_BY_ID_BY_USER("10004", "Error revoking authorized application",
                                         "A system error occurred while revoking authorized application: %s for " +
                                         "user: %s"),
        ERROR_CODE_REVOKE_APP_BY_USER("10005", "Error revoking authorized applications",
                                   "A system error occurred while revoking authorized applications for " +
                                   "user: %s");

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
