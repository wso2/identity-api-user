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

/**
 * Enum contains the recovery api names and the corresponding urls.
 */
public enum APICalls {

    INITIATE_USERNAME_RECOVERY_API("POST", "initiate_username_recovery",
            "/recovery/username/init"),
    INITIATE_PASSWORD_RECOVERY_API("POST", "initiate_password_recovery",
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

    APICalls(String type, String apiName, String apiUrl) {

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
