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

package org.wso2.carbon.identity.api.user.challenge.common;

/**
 * Contains all the user challenge management related constants
 */
public class Constant {

    public static final String CHALLENGE_QUESTION_PREFIX = "CQM-";
    public static final String USER_CHALLENGE_ANSWERS_PATH_COMPONENT = "/%s/challenge-answers";
    public static final String V1_API_PATH_COMPONENT = "/v1";
    public static final String ME_CONTEXT = "me";

    /**
     * Enum for user challenge management related errors in the format of
     * Error Code - code to identify the error
     * Error Message - What went wrong
     * Error Description) - Why it went wrong
     */
    public enum ErrorMessage {

        ERROR_CODE_ERROR_RETRIEVING_CHALLENGES_FOR_USER("10002",
                "Unable to get user challenges.",
                "Server Encountered an error while retrieving challenges for user."),
        ERROR_CODE_ERROR_RETRIEVING_CHALLENGE_ANSWERS_OF_USER("10003",
                "Unable to get user challenge answers.",
                "Server Encountered an error while retrieving challenge answers of user."),
        ERROR_CODE_ERROR_RETRIEVING_CHALLENGE_ANSWER_OF_USER("10004",
                "Unable to get user challenge answer.",
                "Server Encountered an error while retrieving challenge answer of user."),
        ERROR_CODE_ERROR_SETTING_CHALLENGE_ANSWERS_OF_USER("10005",
                "Unable to set user challenge answers.",
                "Server Encountered an error while setting answers to the user challenges."),
        ERROR_CODE_ERROR_UPDATING_CHALLENGE_ANSWERS_OF_USER("10006",
                "Unable to update user challenge answers.",
                "Server Encountered an error while updating the answers to the user challenges."),
        ERROR_CODE_ERROR_DELETING_CHALLENGE_ANSWERS_OF_USER("10007",
                "Unable to remove user challenge answers.",
                "Server Encountered an error while removing answers of the user challenges."),
        ERROR_CODE_ERROR_SETTING_CHALLENGE_ANSWER_OF_USER("10008",
                "Unable to update user challenge answer.",
                "Server Encountered an error while updating the answer of the user challenge."),
        ERROR_CODE_ERROR_UPDATING_CHALLENGE_ANSWER_OF_USER("10009",
                "Unable to update user challenge answer.",
                "Server Encountered an error while updating the answer of the user challenge."),
        ERROR_CODE_ERROR_DELETING_CHALLENGE_ANSWER_OF_USER("10010",
                "Unable to remove user challenge answer.",
                "Server Encountered an error while removing answer of the user challenge."),
        ERROR_CHALLENGE_ANSWER_MISSING("10011",
                "Invalid Request.",
                "Challenge question is missing in the user challenge answer request."),
        ERROR_CODE_USER_ALREADY_ANSWERED_CHALLENGES("10012",
                "Challenge Answers Already set.",
                "User has already answered some challenges. Hence, Unable to add new Answers."),
        ERROR_CODE_USER_HAS_NOT_ANSWERED_CHALLENGES("10013",
                "Challenge Answers Not set.", "User has not" +
                " answered any challenges. Hence, Unable to process."),
        ERROR_CODE_USER_ALREADY_ANSWERED_CHALLENGE("10014",
                "Challenge Answer Already set.", "User has already " +
                "answered this challenge. Hence, Unable to as a new challenge answer."),
        ERROR_CODE_USER_HAS_NOT_ANSWERED_CHALLENGE("10015",
                "Challenge Answer Not set.", "User has not " +
                "answered this challenge. Hence, Unable to process.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {
            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {
            return CHALLENGE_QUESTION_PREFIX + code;
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
