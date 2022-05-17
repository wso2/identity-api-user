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

package org.wso2.carbon.identity.api.user.mfa.common;

/**
 * Constant class for MFA endpoint.
 */
public class MFAConstants {
    public static final String ENABLED_AUTHENTICATORS_CLAIM = "http://wso2.org/claims/identity/enabledAuthenticators";

    public static final String USER_MFA_PREFIX = "MFA-";

    public static final String AUTHENTICATED_WITH_BASIC_AUTH = "AuthenticatedWithBasicAuth";

    public static final String BACK_CODE_AUTHENTICATOR = "Backup Code Authenticator";
    public static final String TOTP_AUTHENTICATOR = "TOTP";

    /**
     * Enum for MFA related errors in the format of
     * Error Code - code to identify the error
     * Error Message - What went wrong
     * Error Description - Why it went wrong
     */
    public enum ErrorMessage {

        SERVER_ERROR_RETRIEVING_REALM_FOR_USER("15001",
                "Unable to retrieve the user realm.",
                "Can not find the user realm for the authenticated user."),
        SERVER_ERROR_UPDATING_CLAIM_USERSTORE("15002,",
                "Unable to update claims in the userstore",
                "MFAService failed while trying to update claim values in the userstore for the user : %s"),
        SERVER_ERROR_RETRIEVE_CLAIM_USERSTORE("15003,",
                "Unable to retrieve claims from the userstore",
                "MFAService failed while trying to retrieve claim values from the userstore for the user : %s"),
        SERVER_ERROR_RETRIEVING_USERSTORE_MANAGER("15004,",
                "Unable to retrieve userstore manager",
                "MFAService failed while trying to get the userstore manager from user realm of the user : %s"),

        USER_ERROR_UNAUTHORIZED_USER("10001",
                "Access denied.",
                "Unauthorized user"),
        USER_ERROR_INVALID_INPUT("10002",
                "Invalid input provided.",
                "The provided authenticators are not valid."),
        USER_ERROR_ENABLED_AUTHENTICATORS_NOT_EXIST("10003",
                "Enabled authenticators doesn't exist.",
                "No authenticators are enabled for the authenticated user"),

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
            return USER_MFA_PREFIX + code;
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
