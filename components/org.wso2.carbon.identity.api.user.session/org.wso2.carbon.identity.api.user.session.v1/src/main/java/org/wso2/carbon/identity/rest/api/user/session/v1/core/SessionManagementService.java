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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.user.common.function.UserToUniqueId;
import org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants;
import org.wso2.carbon.identity.api.user.session.common.util.SessionManagementServiceHolder;
import org.wso2.carbon.identity.application.authentication.framework.exception.UserSessionException;
import org.wso2.carbon.identity.application.authentication.framework.exception.session.mgt
        .SessionManagementClientException;
import org.wso2.carbon.identity.application.authentication.framework.exception.session.mgt.SessionManagementException;
import org.wso2.carbon.identity.application.authentication.framework.model.UserSession;
import org.wso2.carbon.identity.application.authentication.framework.store.UserSessionStore;
import org.wso2.carbon.identity.application.authentication.framework.util.SessionMgtConstants;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.rest.api.user.session.v1.core.function.UserSessionToExternal;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionDTO;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionsDTO;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants.ErrorMessage
        .ERROR_CODE_FILTERING_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants.ErrorMessage
        .ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants.ErrorMessage
        .ERROR_CODE_SESSION_TERMINATE_FORBIDDEN;
import static org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants.ErrorMessage
        .ERROR_CODE_SORTING_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants.ErrorMessage.
        ERROR_CODE_UNABLE_TO_RETRIEVE_FEDERATED_USERID;
import static org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants.
        USER_SESSION_MANAGEMENT_PREFIX;

/**
 * Call internal osgi services to perform user session related operations.
 */
public class SessionManagementService {

    private static final String FEDERATED_USER_DOMAIN = "FEDERATED";
    private static final String IS_FEDERATED_USER = "isFederatedUser";
    private static final String IDP_NAME = "idpName";
    private static final Log log = LogFactory.getLog(SessionManagementService.class);

    /**
     * Get all the active sessions of a given user.
     *
     * @param user   user
     * @param limit  limit (optional)
     * @param offset offset (optional)
     * @param filter filter (optional)
     * @param sort   sort (optional)
     * @return SessionsDTO
     */
    public SessionsDTO getSessionsBySessionId(User user, Integer limit, Integer offset, String filter, String sort) {

        String userId;
        if (isFederatedUser()) {
            userId = getFederatedUserIdFromUser(user);
        } else {
            userId = getUserIdFromUser(user);
        }
        return getSessionsByUserId(userId, limit, offset, filter, sort);
    }

    /**
     * Terminate the session of the given session id.
     *
     * @param user      user
     * @param sessionId session id
     */
    public void terminateSessionBySessionId(User user, String sessionId) {

        String userId;
        if (isFederatedUser()) {
            userId = getFederatedUserIdFromUser(user);
        } else {
            userId = getUserIdFromUser(user);
        }
        terminateSessionBySessionId(userId, sessionId);
    }

    /**
     * Terminate all the sessions of the given user.
     *
     * @param user user
     */
    public void terminateSessionsByUserId(User user) {

        String userId;
        if (isFederatedUser()) {
            userId = getFederatedUserIdFromUser(user);
        } else {
            userId = getUserIdFromUser(user);
        }
        terminateSessionsByUserId(userId);
    }

    /**
     * Get all the active sessions of a given user.
     *
     * @param userId unique id of the user
     * @param limit  limit (optional)
     * @param offset offset (optional)
     * @param filter filter (optional)
     * @param sort   sort (optional)
     * @return SessionsDTO
     */
    public SessionsDTO getSessionsByUserId(String userId, Integer limit, Integer offset, String filter, String
            sort) {

        handleNotImplementedCapabilities(limit, offset, filter, sort);

        List<UserSession> sessionsForUser;
        SessionsDTO sessions = null;
        try {
            if (userId != null) {
                sessions = new SessionsDTO();
                sessionsForUser = SessionManagementServiceHolder.getUserSessionManagementService()
                        .getSessionsByUserId(userId);
                sessions.setUserId(userId);
                sessions.setSessions(buildSessionDTOs(sessionsForUser));
            }
            return sessions;

        } catch (SessionManagementException e) {
            throw handleSessionManagementException(e);
        }
    }

    /**
     * Terminate the session of the given session id.
     *
     * @param userId    unique id of the user
     * @param sessionId session id
     */
    public void terminateSessionBySessionId(String userId, String sessionId) {

        try {
            if (userId != null && sessionId != null) {
                SessionManagementServiceHolder.getUserSessionManagementService().terminateSessionBySessionId(userId,
                        sessionId);
            } else {
                throw handleInvalidParameters();
            }
        } catch (SessionManagementClientException e) {
            if (log.isDebugEnabled()) {
                log.debug(e);
            }
        } catch (SessionManagementException e) {
            throw handleSessionManagementException(e);
        }
    }

    /**
     * Terminate all the sessions of the given user.
     *
     * @param userId unique id of the user
     */
    public void terminateSessionsByUserId(String userId) {

        try {
            if (userId != null) {
                SessionManagementServiceHolder.getUserSessionManagementService().terminateSessionsByUserId(userId);
            }
        } catch (SessionManagementException e) {
            throw handleSessionManagementException(e);
        }
    }

    private List<SessionDTO> buildSessionDTOs(List<UserSession> userSessionList) {

        return userSessionList.stream().map(new UserSessionToExternal()).collect(Collectors.toList());
    }

    private APIError handleSessionManagementException(SessionManagementException e) {

        ErrorResponse errorResponse = getErrorBuilder(e).build(log, e, e.getDescription());

        if (e.getErrorCode() != null) {
            String errorCode = e.getErrorCode();
            errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode : SessionManagementConstants
                    .USER_SESSION_MANAGEMENT_PREFIX + SessionManagementConstants.ERROR_CODE_DELIMITER + errorCode;
            errorResponse.setCode(errorCode);
        }

        Response.Status status;

        if (e instanceof SessionManagementClientException) {
            if (StringUtils.equals(SessionMgtConstants.ErrorMessages.ERROR_CODE_FORBIDDEN_ACTION.getCode(),
                    e.getErrorCode())) {
                status = Response.Status.FORBIDDEN;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }

        return new APIError(status, errorResponse);
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

        if (limit != null) {
            errorEnum = ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
        } else if (offset != null) {
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

    private APIError handleInvalidParameters() {

        ErrorResponse errorResponse = getErrorBuilder(ERROR_CODE_SESSION_TERMINATE_FORBIDDEN).build(log,
                ERROR_CODE_SESSION_TERMINATE_FORBIDDEN.getDescription());
        Response.Status status = Response.Status.FORBIDDEN;
        return new APIError(status, errorResponse);
    }

    private String getUserIdFromUser(User user) {

        return new UserToUniqueId().apply(SessionManagementServiceHolder.getRealmService(), user);
    }

    private String getFederatedUserIdFromUser(User user) {

        if (!IdentityUtil.threadLocalProperties.get().containsKey(IDP_NAME)) {
            if (log.isDebugEnabled()) {
                log.debug("Idp name cannot be found in thread local.");
            }
            return null;
        }
        String idpName = (String) IdentityUtil.threadLocalProperties.get().get(IDP_NAME);
        try {
            int idpId = UserSessionStore.getInstance()
                    .getIdPId(idpName, IdentityTenantUtil.getTenantId(user.getTenantDomain()));
            return UserSessionStore.getInstance()
                    .getUserId(user.getUserName(), IdentityTenantUtil.getTenantId(user.getTenantDomain()),
                            FEDERATED_USER_DOMAIN, idpId);
        } catch (UserSessionException e) {
            String errorDescription =
                    String.format(ERROR_CODE_UNABLE_TO_RETRIEVE_FEDERATED_USERID.getDescription(), user.getUserName(),
                            user.getTenantDomain());
            ErrorResponse errorResponse = new ErrorResponse.Builder().withCode(
                            USER_SESSION_MANAGEMENT_PREFIX + SessionManagementConstants.ERROR_CODE_DELIMITER +
                                    ERROR_CODE_UNABLE_TO_RETRIEVE_FEDERATED_USERID.getCode())
                    .withMessage(ERROR_CODE_UNABLE_TO_RETRIEVE_FEDERATED_USERID.getMessage())
                    .withDescription(errorDescription).build(log, e, errorDescription);
            throw new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        }
    }

    private boolean isFederatedUser() {

        return IdentityUtil.threadLocalProperties.get().containsKey(IS_FEDERATED_USER) &&
                (boolean) IdentityUtil.threadLocalProperties.get().get(IS_FEDERATED_USER);
    }
}
