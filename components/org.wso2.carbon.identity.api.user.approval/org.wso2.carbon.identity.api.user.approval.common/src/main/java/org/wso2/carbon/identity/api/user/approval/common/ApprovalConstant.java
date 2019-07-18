/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.user.approval.common;

/**
 * Contains all the user challenge management related constants
 */
public class ApprovalConstant {

    public static final String USER_APPROVAL_PREFIX = "HTA-";
    public static final String USER_APPROVAL_TASK_PATH_COMPONENT = "/%s/approval-tasks";
    public static final String V1_API_PATH_COMPONENT = "/v1";
    public static final String ME_CONTEXT = "me";

    /**
     * Enum for user challenge management related errors in the format of
     * Error Code - code to identify the error
     * Error Message - What went wrong
     * Error Description - Why it went wrong
     */
    public enum ErrorMessage {

        SERVER_ERROR_RETRIEVING_APPROVALS_FOR_USER("50002",
                "Unable to retrieve approvals for the user.",
                "Server Encountered an error while retrieving approvals for user."),
        SERVER_ERROR_RETRIEVING_APPROVAL_OF_USER("50003",
                "Unable to retrieve the user approval.",
                "Server Encountered an error while retrieving information on the approval task."),
        SERVER_ERROR_CHANGING_APPROVALS_STATE("50004",
                "Unable to update the approval status.",
                "Server Encountered an error while updating the approval task status."),
        USER_ERROR_UNAUTHORIZED_USER("10001",
                "Access Denied.",
                "You are not authorized to perform this task."),
        USER_ERROR_INVALID_TASK_ID("10002",
                "Invalid Input Provided.",
                "The provided Task ID is not in the correct format."),
        USER_ERROR_NON_EXISTING_TASK_ID("10003",
                "Task does not exists.",
                null),
        USER_ERROR_INVALID_INPUT("10004",
                "Invalid input data provided.",
                null),
        USER_ERROR_NOT_ACCEPTABLE_INPUT_FOR_NEXT_STATE("10005",
                "Unacceptable input provided",
                "Only [CLAIM, RELEASE, APPROVED, REJECTED] are acceptable."),
        USER_ERROR_INVALID_STATE_CHANGE("10006",
                "Unable to change the approval status.",
                "Invalid state change is requested for the given task."),
        USER_ERROR_INVALID_OPERATION("10007",
                "Unable to update the approval status",
                "Invalid state change is requested for the given task.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {
            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {
            return USER_APPROVAL_PREFIX + code;
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
