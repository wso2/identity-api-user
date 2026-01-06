/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.recovery.v2.impl.core;

import static org.wso2.carbon.identity.api.user.common.Constants.USER_API_PATH_COMPONENT;

/**
 * Constants for the recovery endpoint.
 */
public class Constants {

    public static final String SERVER_ERROR = "Error occurred in the server while performing the task.";
    public static final String DEFAULT_RESPONSE_CONTENT_TYPE = "application/json";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";

    // Default error messages.
    public static final String STATUS_FORBIDDEN_MESSAGE_DEFAULT = "Forbidden";
    public static final String STATUS_NOT_FOUND_MESSAGE_DEFAULT = "Not Found";
    public static final String STATUS_INTERNAL_SERVER_ERROR_MESSAGE_DEFAULT = "Internal server error";
    public static final String STATUS_METHOD_NOT_ACCEPTED_MESSAGE_DEFAULT = "Not Accepted";
    public static final String STATUS_CONFLICT_MESSAGE_DEFAULT = "Conflict";
    public static final String STATUS_PRECONDITION_FAILED_MESSAGE_DEFAULT = "Precondition Failed";
    public static final String STATUS_INTERNAL_SERVER_ERROR_DESCRIPTION_DEFAULT =
            "The server encountered an internal error. Please contact administrator.";
    public static final String STATUS_BAD_REQUEST_DEFAULT = "Bad Request";

    public static final String ENABLE_NORMALIZED_RETRY_ERROR_RESPONSE =
            "Recovery.ErrorMessage.EnableNormalizedRetryErrorResponse";

    // Recovery type.
    public static final String RECOVERY_WITH_NOTIFICATIONS = "recoverWithNotifications";
    public static final String RECOVER_WITH_CHALLENGE_QUESTIONS = "recoverWithChallengeQuestions";

    public static final String ACCOUNT_RECOVERY_ENDPOINT_BASEPATH = USER_API_PATH_COMPONENT + "/v2";
    public static final String CHALLENGE_QUESTIONS_ENDPOINT_BASEPATH = "identity/recovery/v0.9";

    /**
     * Relation states for the APIs.
     */
    public static class RelationStates {

        public static final String NEXT_REL = "next";
        public static final String RESEND_REL = "resend";
    }
}
