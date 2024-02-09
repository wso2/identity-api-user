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

package org.wso2.carbon.identity.api.user.onboard.common.error;

import org.apache.commons.logging.Log;
import org.wso2.carbon.identity.api.user.onboard.common.util.Utils;

/**
 * ErrorResponse class for all API related errors.
 */
public class ErrorResponse extends ErrorDTO {

    private static final long serialVersionUID = -3502358623560083025L;

    /**
     * ErrorResponse Builder.
     */
    public static class Builder {

        private String code;
        private String message;
        private String description;

        /**
         * Creates a builder.
         */
        public Builder() {

        }

        /**
         * Creates builder with code.
         *
         * @param code code.
         * @return builder.
         */
        public Builder withCode(String code) {

            this.code = code;
            return this;
        }

        /**
         * Creates builder with message.
         *
         * @param message message.
         * @return builder.
         */
        public Builder withMessage(String message) {

            this.message = message;
            return this;
        }

        /**
         * Creates builder with description.
         *
         * @param description error  description.
         * @return builder.
         */
        public Builder withDescription(String description) {

            this.description = description;
            return this;
        }

        /**
         * This method builds the error response.
         *
         * @return error response.
         */
        public ErrorResponse build() {

            ErrorResponse error = new ErrorResponse();
            error.setCode(this.code);
            error.setMessage(this.message);
            error.setDescription(this.description);
            error.setRef(Utils.getCorrelation());
            return error;
        }

        /**
         * This method builds the error response.
         *
         * @param log log.
         * @param e exception.
         * @param message error message.
         * @param isClientException whether the exception is client exception.
         * @return error response.
         */
        public ErrorResponse build(Log log, Exception e, String message, boolean isClientException) {

            ErrorResponse error = build();
            String errorMessageFormat = "errorCode: %s | message: %s";
            String errorMsg = String.format(errorMessageFormat, error.getCode(), message);
            if (!Utils.isCorrelationIDPresent()) {
                errorMsg = String.format("correlationID: %s | " + errorMsg, error.getRef());
            }
            if (isClientException) {
                if (log.isDebugEnabled()) {
                    log.debug(errorMsg, e);
                }
            } else {
                log.error(errorMsg, e);
            }
            return error;
        }

        /**
         * This method builds the error response.
         *
         * @param log log.
         * @param message error message.
         * @param isClientException whether the exception is client exception.
         * @return error response.
         */
        public ErrorResponse build(Log log, String message, boolean isClientException) {

            ErrorResponse error = build();
            String errorMessageFormat = "errorCode: %s | message: %s";
            String errorMsg = String.format(errorMessageFormat, error.getCode(), message);
            if (!Utils.isCorrelationIDPresent()) {
                errorMsg = String.format("correlationID: %s | " + errorMsg, error.getRef());
            }
            if (isClientException) {
                if (log.isDebugEnabled()) {
                    log.debug(errorMsg);
                }
            } else {
                log.error(errorMsg);
            }
            return error;
        }

        /**
         * This method builds the error response.
         *
         * @param log log.
         * @param e Exception.
         * @param message Error message.
         * @return error response.
         */
        public ErrorResponse build(Log log, Exception e, String message) {

            ErrorResponse error = build();
            String errorMessageFormat = "errorCode: %s | message: %s";
            String errorMsg = String.format(errorMessageFormat, error.getCode(), message);
            if (!Utils.isCorrelationIDPresent()) {
                errorMsg = String.format("correlationID: %s | " + errorMsg, error.getRef());
            }
            log.error(errorMsg, e);
            return error;
        }
    }
}
