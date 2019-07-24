/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.rest.api.user.session.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants;
import org.wso2.carbon.identity.api.user.session.common.util.SessionManagementUtil;
import org.wso2.carbon.identity.application.authentication.framework.exception.session.mgt
        .SessionManagementClientException;
import org.wso2.carbon.identity.application.authentication.framework.exception.session.mgt.SessionManagementException;
import org.wso2.carbon.identity.application.authentication.framework.model.UserSession;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.rest.api.user.session.v1.core.function.UserSessionToExternal;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionDTO;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionsDTO;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.user.common.Util.resolveUserIdFromUser;
import static org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants.ErrorMessage
        .ERROR_CODE_FILTERING_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants.ErrorMessage
        .ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants.ErrorMessage
        .ERROR_CODE_SORTING_NOT_IMPLEMENTED;

/**
 * Call internal osgi services to perform user session related operations
 */
public class SessionManagementService {

    private static final Log log = LogFactory.getLog(SessionManagementService.class);

    public SessionsDTO getSessionsBySessionId(User user, Integer limit, Integer offset, String filter, String
            sort) {

        handleNotImplementedCapabilities(limit, offset, filter, sort);

        List<UserSession> sessionsForUser;
        SessionsDTO sessions = new SessionsDTO();
        try {
            String userId = resolveUserIdFromUser(user);
            sessionsForUser = SessionManagementUtil.getUserSessionManagementService().getSessionsByUserId(userId);
            sessions.setUserId(userId);
            sessions.setSessions(buildSessionDTOs(sessionsForUser));
            return sessions;

        } catch (SessionManagementException e) {
            log.error(e.getMessage());
            handleSessionManagementException(e);
            return null;
        }

    }

    public void terminateSessionBySessionId(User user, String sessionId) {

        String userId = resolveUserIdFromUser(user);
        SessionManagementUtil.getUserSessionManagementService().terminateSessionBySessionId(userId, sessionId);

    }

    public void terminateSessionsByUserId(User user) {
        try {
            String userId = resolveUserIdFromUser(user);
            SessionManagementUtil.getUserSessionManagementService().terminateSessionsByUserId(userId);
        } catch (SessionManagementException e) {
            handleSessionManagementException(e);
        }
    }

    private List<SessionDTO> buildSessionDTOs(List<UserSession> userSessionList) {

        return userSessionList.stream().map(new UserSessionToExternal()).collect(Collectors.toList());
    }

    private void handleSessionManagementException(SessionManagementException e) {

        ErrorResponse errorResponse = getErrorBuilder(e).build(log, e, e.getDescription());

        if (e.getErrorCode() != null) {
            String errorCode = e.getErrorCode();
            errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode : SessionManagementConstants
                    .USER_SESSION_MANAGEMENT_PREFIX + SessionManagementConstants.ERROR_CODE_DELIMITER + errorCode;
            errorResponse.setCode(errorCode);
        }

        Response.Status status;

        if (e instanceof SessionManagementClientException) {
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }

        throw new APIError(status, errorResponse);
    }

    private ErrorResponse.Builder getErrorBuilder(SessionManagementException error) {

        return new ErrorResponse.Builder()
                .withCode(error.getErrorCode())
                .withMessage(error.getMessage())
                .withDescription(error.getDescription());
    }

    private ErrorResponse.Builder getErrorBuilder(SessionManagementConstants.ErrorMessage error) {

        return new ErrorResponse.Builder()
                .withCode(error.getCode())
                .withMessage(error.getMessage())
                .withDescription(error.getDescription());
    }

    private void handleNotImplementedCapabilities(Integer limit, Integer offset, String filter, String sort) {

        SessionManagementConstants.ErrorMessage errorEnum = null;

        if (limit != null && limit != 0) {
            errorEnum = ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
        } else if (offset != null && offset != 0) {
            errorEnum = ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
        } else if (filter != null) {
            errorEnum = ERROR_CODE_FILTERING_NOT_IMPLEMENTED;
        } else if (sort != null) {
            errorEnum = ERROR_CODE_SORTING_NOT_IMPLEMENTED;
        }

        if (errorEnum != null) {
            ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(log, errorEnum.getDescription());
            Response.Status status = Response.Status.NOT_IMPLEMENTED;

            throw new APIError(status, errorResponse);
        }
    }
}
