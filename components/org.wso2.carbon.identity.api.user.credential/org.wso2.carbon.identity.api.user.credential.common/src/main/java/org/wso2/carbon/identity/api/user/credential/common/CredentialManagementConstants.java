/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.user.credential.common;

import java.util.Arrays;
import java.util.Locale;

/**
 * Credential Management related constants.
 */
public class CredentialManagementConstants {

    private CredentialManagementConstants() {

    }

    /**
     * Enum for supported credential types.
     */
    public enum CredentialTypes {

        PASSKEY("passkey"),
        PUSH_AUTH("push-auth"),
        BACKUP_CODE("backup-code");

        private final String apiValue;

        CredentialTypes(String apiValue) {

            this.apiValue = apiValue;
        }

        public String apiValue() {

            return apiValue;
        }

        /**
         * Resolve the credential type from its API value (e.g. "push-auth").
         *
         * @param value API value provided by the caller.
         * @return Matching credential type if available.
         */
        public static CredentialTypes fromString(String value) {

            if (value == null) {

                return null;
            }

            String candidate = value.trim().toLowerCase(Locale.ROOT);
            if (candidate.isEmpty()) {

                return null;
            }

            return Arrays.stream(values())
                    .filter(type -> type.apiValue().equals(candidate))
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * Enum for error messages.
     */
    public enum ErrorMessages {

        // Server errors.
        ERROR_CODE_GET_PASSKEYS_FAILURE("65001",
                "Error retrieving registered passkeys.",
                "Unexpected server error while fetching registered passkeys for user: %s."),
        ERROR_CODE_DELETE_PASSKEYS_FAILURE("65002",
                "Error deleting registered passkey.",
                "Unexpected server error while deleting registered passkey: %s for user: %s."),
        ERROR_CODE_GET_PUSH_AUTH_DEVICE_FAILURE("65003",
                "Error retrieving registered push auth devices.",
                "Unexpected server error while fetching registered push auth device for user: %s."),
        ERROR_CODE_DELETE_PUSH_AUTH_DEVICE_FAILURE("65004",
                "Error deleting registered push auth devices.",
                "Unexpected server error while deleting registered push auth device: %s for user: %s."),
        ERROR_CODE_GET_BACKUP_CODES_FAILURE("65005",
                "Error retrieving backup codes.",
                "Unexpected server error while fetching backup codes for user: %s."),
        ERROR_CODE_UPDATE_BACKUP_CODES_FAILURE("65006",
                "Error updating backup codes.",
                "Unexpected server error while updating backup codes for user: %s."),
        ERROR_CODE_HANDLER_NOT_REGISTERED("65007",
                "No handler registered for credential type.",
                "No handler is registered on the server for credential type: %s."),

        // Client errors.
       ERROR_CODE_USER_NOT_FOUND("60001",
                "User not found",
                "User with id %s not found in the tenant domain."),
        ERROR_CODE_INVALID_CREDENTIAL_TYPE("60002",
                "Invalid credential type.",
                "The provided credential type is not supported."),
        ERROR_CODE_INVALID_CREDENTIAL_ID("60003",
                "Invalid credential ID.",
                "The provided credential ID is invalid."),
        ERROR_CODE_UNSUPPORTED_OPERATION("60004",
                "Unsupported operation.",
                "The requested operation to %s is not supported."),
        ERROR_CODE_CREDENTIAL_CREATION_NOT_SUPPORTED("60005",
                "Credential creation not supported.",
                "Credential creation is not supported for credential type: %s."),
        ERROR_CODE_CREDENTIAL_DELETION_BY_TYPE_NOT_SUPPORTED("60006",
                "Credential deletion by type not supported.",
                "Credential deletion by type is not supported for credential type: %s."),
        ERROR_CODE_CREDENTIAL_DELETION_BY_ID_NOT_SUPPORTED("60007",
                "Credential deletion by id not supported.",
                "Credential deletion by id is not supported for credential type: %s."),
        ERROR_CODE_BACKUP_CODE_MGT_FAILURE("60008",
                "Error managing backup codes.",
                "The request to manage backup codes for user %s is invalid."),
        ERROR_CODE_DELETE_PASSKEYS_CLIENT_FAILURE("60009",
                "Error deleting registered passkey.",
                "The request to delete the passkey credential: %s for user %s is invalid."),
        ERROR_CODE_DELETE_PUSH_AUTH_DEVICE_CLIENT_FAILURE("60010",
                "Error deleting registered push auth devices.",
                "The request to delete the push auth credential: %s for user %s is invalid."),
        ERROR_CODE_GET_CREDENTIALS_CLIENT_FAILURE("60011",
                "Error retrieving credentials.",
                "The request to retrieve credentials by type: %s for user %s is invalid.")
        ;

        private static final String ERROR_PREFIX = "UCM";
        private final String code;
        private final String message;
        private final String description;

        ErrorMessages(String code, String message, String description) {

            this.code = ERROR_PREFIX + "-" + code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return code;
        }

        public String getMessage() {

            return message;
        }

        public String getDescription() {

            return description;
        }

        @Override
        public String toString() {

            return code + ":" + message;
        }
    }
}
