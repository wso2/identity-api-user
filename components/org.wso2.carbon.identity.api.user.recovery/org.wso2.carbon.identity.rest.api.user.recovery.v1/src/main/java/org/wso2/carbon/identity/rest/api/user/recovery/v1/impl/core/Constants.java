/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core;

import static org.wso2.carbon.identity.api.user.common.Constants.USER_API_PATH_COMPONENT;

/**
 * Constants for the recovery endpoint.
 */
public class Constants {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String SUCCESS = "SUCCESS";
    public static final String INVALID = "INVALID";
    public static final String FORBIDDEN = "FORBIDDEN";
    public static final String FAILED = "FAILED";
    public static final String SERVER_ERROR = "Error occurred in the server while performing the task.";
    public static final String APPLICATION_JSON = "application/json";
    public static final String DEFAULT_RESPONSE_CONTENT_TYPE = APPLICATION_JSON;
    public static final String HEADER_CONTENT_TYPE = "Content-Type";

    // Default error messages
    public static final String STATUS_FORBIDDEN_MESSAGE_DEFAULT = "Forbidden";
    public static final String STATUS_NOT_FOUND_MESSAGE_DEFAULT = "Not Found";
    public static final String STATUS_INTERNAL_SERVER_ERROR_MESSAGE_DEFAULT = "Internal server error";
    public static final String STATUS_METHOD_NOT_ALLOWED_MESSAGE_DEFAULT = "Method Not Allowed";
    public static final String STATUS_METHOD_NOT_ACCPETED_MESSAGE_DEFAULT = "Not Accepted";
    public static final String STATUS_BAD_REQUEST_MESSAGE_DEFAULT = "Invalid Request";
    public static final String STATUS_CONFLICT_MESSAGE_RESOURCE_ALREADY_EXISTS = "Resource Already Exists";
    public static final String STATUS_CONFLICT_MESSAGE_DEFAULT = "Conflict";
    public static final String TENANT_NAME_FROM_CONTEXT = "TenantNameFromContext";
    public static final String STATUS_PRECONDITION_FAILED_MESSAGE_DEFAULT = "Precondition Failed";
    public static final String STATUS_INTERNAL_SERVER_ERROR_DESCRIPTION_DEFAULT =
            "The server encountered an internal error. Please contact administrator.";

    // Recovery type
    public static final String USERNAME_RECOVERY = "username-recovery";
    public static final String PASSWORD_RECOVERY = "password-recovery";
    public static final String RECOVERY_WITH_NOTIFICATIONS = "recoverWithNotifications";
    public static final String RECOVER_WITH_CHALLENGE_QUESTIIONS = "recoverWithChallengeQuestions";

    public static final String ACCOUNT_RECOVERY_ENDPOINT_BASEPATH = USER_API_PATH_COMPONENT + "/v1";
    public static final String CHALLENGE_QUESTIONS_ENDPOINT_BASEPATH = "identity/recovery/v0.9";

    /**
     * Relation states for the APIs.
     */
    public static class RelationStates {

        public static final String NEXT_REL = "next";
        public static final String RETRY_REL = "retry";
        public static final String RESEND_REL = "resend";
    }


    /**
     * Enum contains the recovery api names and the corresponding urls.
     */
    public enum APICall {

        INITIATE_USERNAME_RECOVERY_API("POST", "initiate_username_recovery",
                "/recovery/username/init"),
        INITIATE_PASSWORD_RECOVERY_API("POST", "initiate_username_recovery",
                "/recovery/password/init"),
        RECOVER_USERNAME_API("POST", "recover_username",
                "/recovery/username/recover"),
        RECOVER_PASSWORD_API("POST", "recover_password",
                "/recovery/password/recover"),
        CONFIRM_PASSWORD_RECOVERY_API("POST", "confirm_password_recovery",
                "/recovery/password/confirm"),
        RESET_PASSWORD_API("POST", "reset_password",
                "/recovery/password/reset"),
        RESEND_CONFIRMATION_API("POST", "resend_confirmation",
                "/recovery/password/resend"),
        RECOVER_WITH_SECURITY_QUESTIONS_API("GET", "security_questions_api",
                "/security-question?username=%s");

        /**
         * Name of the API.
         */
        private final String apiName;

        /**
         * Url of the API.
         */
        private final String apiUrl;

        /**
         * Http method call.
         */
        private String type;

        APICall(String type, String apiName, String apiUrl) {

            this.type = type;
            this.apiName = apiName;
            this.apiUrl = apiUrl;
        }

        /**
         * Get http method type.
         *
         * @return Get http method type
         */
        public String getType() {
            return type;
        }

        /**
         * Get the url of the API.
         *
         * @return API url
         */
        public String getApiUrl() {

            return apiUrl;
        }
    }
}
