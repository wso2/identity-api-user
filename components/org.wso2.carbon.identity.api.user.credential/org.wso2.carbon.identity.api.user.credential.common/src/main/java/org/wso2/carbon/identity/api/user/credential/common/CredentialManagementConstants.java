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

import java.text.Collator;
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

        PASSKEY,
        PUSH_AUTH,
        BACKUP_CODE;

        /**
         * Resolve the credential type for a given identifier. Accepts both API values (e.g. "push-auth") and
         * enum names (e.g. "PUSH_AUTH").
         *
         * @param value Credential type provided by the caller.
         * @return Matching credential type if available.
         */
        public static CredentialTypes fromString(String value) {

            if (value == null) {

                return null;
            }

            String candidate = value.trim();
            if (candidate.isEmpty()) {

                return null;
            }

            Collator collator = Collator.getInstance(Locale.ROOT);
            collator.setStrength(Collator.PRIMARY);

            return Arrays.stream(values())
                    .filter(type -> {
                        String typeName = type.name().toLowerCase(Locale.ROOT).replace("_", "-");
                        return collator.compare(type.name(), candidate) == 0
                                || collator.compare(typeName, candidate) == 0;
                    })
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * Enum for error messages.
     */
    public enum ErrorMessages {

        // Server errors.
        ERROR_CODE_GET_PASSKEYS("65001",
                "Error retrieving registered passkeys.",
                "Unexpected server error while fetching registered passkeys for entity ID: %s."),
        ERROR_CODE_DELETE_PASSKEYS("65002",
                "Error deleting registered passkey.",
                "Unexpected server error while deleting registered passkey: %s for entity ID: %s."),
        ERROR_CODE_GET_PUSH_AUTH_DEVICE("65003",
                "Error retrieving registered push auth devices.",
                "Unexpected server error while fetching registered push auth device for entity ID: %s."),
        ERROR_CODE_DELETE_PUSH_AUTH_DEVICE("65004",
                "Error deleting registered push auth devices.",
                "Unexpected server error while deleting registered push auth device: %s for entity ID: %s."),
        ERROR_CODE_GET_BACKUP_CODES("65005",
                "Error retrieving backup codes.",
                "Unexpected server error while fetching backup codes for entity ID: %s."),
        ERROR_CODE_DELETE_BACKUP_CODES("65006",
                "Error deleting backup codes.",
                "Unexpected server error while deleting backup codes for entity ID: %s."),
        ERROR_CODE_CREATE_BACKUP_CODES("65007",
                "Error generating backup codes.",
                "Unexpected server error while generating backup codes for entity ID: %s."),

        // Client errors.
        ERROR_CODE_DELETE_PASSKEY_CREDENTIAL("60001",
                "Error deleting credential.",
                "The request to delete the passkey credential: %s  was invalid."),
        ERROR_CODE_DELETE_PUSH_AUTH_CREDENTIAL("60002",
                "Error deleting credential.",
                "The request to delete the push auth credential: %s  was invalid."),
        ERROR_CODE_GET_USERNAME_FROM_USERID("60003",
                "Error retrieving username from user ID.",
                "The request to retrieve the username from the user ID: %s was invalid."),
        ERROR_CODE_ENTITY_NOT_FOUND("60004",
                "Entity not found",
                "Entity with ID %s not found in the tenant domain."),
        ERROR_CODE_INVALID_CREDENTIAL_TYPE("60005",
                "Invalid credential type.",
                "The provided credential type is not supported."),
        ERROR_CODE_INVALID_CREDENTIAL_ID("60006",
                "Invalid credential ID.",
                "The provided credential ID is invalid."),
        ERROR_CODE_CREDENTIAL_CREATION_NOT_SUPPORTED("60007",
                "Credential creation not supported.",
                "Credential creation is not supported for credential type: %s."),
        ERROR_CODE_CREDENTIAL_DELETION_BY_TYPE_NOT_SUPPORTED("60008",
                "Credential deletion by type not supported.",
                "Credential deletion by type is not supported for credential type: %s."),
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
