/*
 * Copyright (c) 2019-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.session.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.user.common.function.UserToUniqueId;
import org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants;
import org.wso2.carbon.identity.application.authentication.framework.UserSessionManagementService;
import org.wso2.carbon.identity.application.authentication.framework.exception.UserSessionException;
import org.wso2.carbon.identity.application.authentication.framework.exception.session.mgt.SessionManagementClientException;
import org.wso2.carbon.identity.application.authentication.framework.exception.session.mgt.SessionManagementException;
import org.wso2.carbon.identity.application.authentication.framework.model.UserSession;
import org.wso2.carbon.identity.application.authentication.framework.store.UserSessionStore;
import org.wso2.carbon.identity.application.authentication.framework.util.SessionMgtConstants;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.model.ExpressionNode;
import org.wso2.carbon.identity.core.model.FilterTreeBuilder;
import org.wso2.carbon.identity.core.model.Node;
import org.wso2.carbon.identity.core.model.OperationNode;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.rest.api.user.session.v1.core.function.UserSessionToExternal;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SearchResponseDTO;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionDTO;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionsDTO;
import org.wso2.carbon.user.core.service.RealmService;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.user.common.ContextLoader.buildURIForBody;
import static org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants.ErrorMessage.ERROR_CODE_FILTERING_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants.ErrorMessage.ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants.ErrorMessage.ERROR_CODE_SESSION_TERMINATE_FORBIDDEN;
import static org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants.ErrorMessage.ERROR_CODE_SORTING_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants.ErrorMessage.ERROR_CODE_UNABLE_TO_RETRIEVE_FEDERATED_USERID;
import static org.wso2.carbon.identity.api.user.session.common.constant.SessionManagementConstants.USER_SESSION_MANAGEMENT_PREFIX;
import static org.wso2.carbon.identity.application.authentication.framework.util.SessionMgtConstants.ErrorMessages.ERROR_CODE_INVALID_DATA;
import static org.wso2.carbon.identity.application.authentication.framework.util.SessionMgtConstants.ErrorMessages.ERROR_CODE_INVALID_SESSION;
import static org.wso2.carbon.identity.application.authentication.framework.util.SessionMgtConstants.ErrorMessages.ERROR_CODE_INVALID_USER;

/**
 * Call internal osgi services to perform user session related operations.
 */
public class SessionManagementService {

    private final UserSessionManagementService userSessionManagementService;
    private final RealmService realmService;

    private static final String FEDERATED_USER_DOMAIN = "FEDERATED";
    private static final String IS_FEDERATED_USER = "isFederatedUser";
    private static final String IDP_NAME = "idpName";
    private static final Log log = LogFactory.getLog(SessionManagementService.class);
    private static final String SESSIONS_SEARCH_ENDPOINT = "/v1/sessions";
    private static final Integer SESSIONS_SEARCH_DEFAULT_LIMIT = 20;

    public SessionManagementService(UserSessionManagementService userSessionManagementService,
                                    RealmService realmService) {

        this.userSessionManagementService = userSessionManagementService;
        this.realmService = realmService;
    }

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
        if (isFederatedUser() && !isOrganizationUser()) {
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
        if (isFederatedUser() && !isOrganizationUser()) {
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
    public SessionsDTO getSessionsByUserId(String userId, Integer limit, Integer offset, String filter, String sort) {

        handleNotImplementedCapabilities(limit, offset, filter, sort);

        List<UserSession> sessionsForUser;
        SessionsDTO sessions = null;
        try {
            if (!StringUtils.isBlank(userId)) {
                sessions = new SessionsDTO();
                sessionsForUser = userSessionManagementService.getSessionsByUserId(userId);
                sessions.setUserId(userId);
                sessions.setSessions(buildSessionDTOs(sessionsForUser));
            }
            return sessions;

        } catch (SessionManagementException e) {
            throw handleSessionManagementException(e);
        }
    }

    /**
     * Get a specific session of a given user.
     *
     * @param userId    unique id of the user
     * @param sessionId unique id of the session
     * @return SessionDTO
     */
    public Optional<SessionDTO> getSessionBySessionId(String userId, String sessionId) {

        try {
            if (StringUtils.isBlank(userId)) {
                throw new SessionManagementClientException(ERROR_CODE_INVALID_USER,
                        ERROR_CODE_INVALID_USER.getDescription());
            }
            if (StringUtils.isBlank(sessionId)) {
                String message = "Session ID is not provided to perform session management tasks.";
                throw new SessionManagementClientException(ERROR_CODE_INVALID_SESSION, message);
            }
            return userSessionManagementService.getSessionBySessionId(userId, sessionId)
                    .map(new UserSessionToExternal());
        } catch (SessionManagementException e) {
            throw handleSessionManagementException(e);
        }
    }

    /**
     * Search active sessions on the system.
     *
     * @param tenantDomain context tenant domain
     * @param filter       filter (optional)
     * @param limit        limit (optional)
     * @param since        pointer to previous page of results (optional)
     * @param until        pointer to next page of results (optional)
     * @return SearchResponseDTO
     */
    public SearchResponseDTO getSessions(String tenantDomain, String filter, Integer limit, Long since, Long until) {

        try {
            List<ExpressionNode> filterNodes = getExpressionNodes(filter, since, until);
            validateSearchFilter(filterNodes);

            limit = limit == null || limit <= 0 ? SESSIONS_SEARCH_DEFAULT_LIMIT : limit;
            String sortOrder = since != null ? SessionMgtConstants.ASC : SessionMgtConstants.DESC;
            SearchResponseDTO response = new SearchResponseDTO();

            List<UserSession> results =
                    userSessionManagementService.getSessions(tenantDomain, filterNodes, limit + 1, sortOrder);

            if (!results.isEmpty()) {
                boolean hasMoreItems = results.size() > limit;
                boolean needsReverse = since != null;
                boolean isFirstPage = (since == null && until == null) || (since != null && !hasMoreItems);
                boolean isLastPage = !hasMoreItems && (until != null || since == null);

                String qs = "?limit=" + limit;
                if (StringUtils.isNotBlank(filter)) {
                    qs += "&filter=" + URLEncoder.encode(filter);
                }

                if (hasMoreItems) {
                    results.remove(results.size() - 1);
                }
                if (needsReverse) {
                    Collections.reverse(results);
                }

                response.setResources(results.stream().map(new UserSessionToExternal()).collect(Collectors.toList()));
                if (!isFirstPage) {
                    response.setPrevious(buildURIForBody(SESSIONS_SEARCH_ENDPOINT + qs + "&since=" +
                            results.get(0).getCreationTime()));
                }
                if (!isLastPage) {
                    response.setNext(buildURIForBody(SESSIONS_SEARCH_ENDPOINT + qs + "&until=" +
                            results.get(results.size() - 1).getCreationTime()));
                }
            }

            return response;
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
                userSessionManagementService.terminateSessionBySessionId(userId, sessionId);
            } else {
                throw handleForbiddenAction();
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
                userSessionManagementService.terminateSessionsByUserId(userId);
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

        if (limit != null || offset != null) {
            errorEnum = ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
        } else if (filter != null) {
            errorEnum = ERROR_CODE_FILTERING_NOT_IMPLEMENTED;
        } else if (sort != null) {
            errorEnum = ERROR_CODE_SORTING_NOT_IMPLEMENTED;
        }

        if (errorEnum != null) {
            ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(log, errorEnum.getDescription());
            throw new APIError(Response.Status.NOT_IMPLEMENTED, errorResponse);
        }
    }

    private APIError handleForbiddenAction() {

        ErrorResponse errorResponse = getErrorBuilder(ERROR_CODE_SESSION_TERMINATE_FORBIDDEN).build(log,
                ERROR_CODE_SESSION_TERMINATE_FORBIDDEN.getDescription());
        return new APIError(Response.Status.FORBIDDEN, errorResponse);
    }

    private String getUserIdFromUser(User user) {

        return new UserToUniqueId().apply(realmService, user);
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

    /**
     * Get the filter nodes as a list.
     *
     * @param filter value of the filter (optional)
     * @param since  pointer to previous page of results (optional)
     * @param until  pointer to next page of results (optional)
     * @return list of filter expressions
     * @throws SessionManagementClientException if an error occurs while parsing the filter criteria
     */
    private List<ExpressionNode> getExpressionNodes(String filter, Long since, Long until)
            throws SessionManagementClientException {

        List<ExpressionNode> expressionNodes = new ArrayList<>();
        FilterTreeBuilder filterTreeBuilder;
        String paginatedFilter = StringUtils.isNotBlank(filter) ? filter : "";

        try {
            if (since != null) {
                paginatedFilter += StringUtils.isNotBlank(paginatedFilter)
                        ? " and since gt " + since
                        : "since gt " + since;
            } else if (until != null) {
                paginatedFilter += StringUtils.isNotBlank(paginatedFilter)
                        ? " and until lt " + until
                        : "until lt " + until;
            }
            if (StringUtils.isNotBlank(paginatedFilter)) {
                filterTreeBuilder = new FilterTreeBuilder(paginatedFilter);
                Node rootNode = filterTreeBuilder.buildTree();
                setExpressionNodeList(rootNode, expressionNodes);
            }
        } catch (IOException | IdentityException e) {
            String message = "check filter parameter syntax";
            throw new SessionManagementClientException(ERROR_CODE_INVALID_DATA,
                    String.format(ERROR_CODE_INVALID_DATA.getDescription(), message), e);
        }

        return expressionNodes;
    }

    /**
     * Set the node values as list of expression.
     *
     * @param node       filter node.
     * @param expression list of filter expressions
     */
    private void setExpressionNodeList(Node node, List<ExpressionNode> expression) {

        if (node instanceof ExpressionNode) {
            expression.add((ExpressionNode) node);
        } else if (node instanceof OperationNode) {
            setExpressionNodeList(node.getLeftNode(), expression);
            setExpressionNodeList(node.getRightNode(), expression);
        }
    }

    public void validateSearchFilter(List<ExpressionNode> expressionNodes) throws SessionManagementClientException {

        Collator collator = Collator.getInstance();
        collator.setStrength(Collator.PRIMARY);

        for (ExpressionNode expressionNode : expressionNodes) {
            String operation = expressionNode.getOperation();
            String value = expressionNode.getValue();
            String attribute = expressionNode.getAttributeValue();

            if (StringUtils.isBlank(attribute) || StringUtils.isBlank(operation) || StringUtils.isBlank(value)) {

                String message = String.format("'%s %s %s' is not a valid filter", attribute, operation, value);
                throw new SessionManagementClientException(ERROR_CODE_INVALID_DATA,
                        String.format(ERROR_CODE_INVALID_DATA.getDescription(), message));

            } else if (!collator.equals(attribute, SessionMgtConstants.FLD_APPLICATION) &&
                    !collator.equals(attribute, SessionMgtConstants.FLD_IP_ADDRESS) &&
                    !collator.equals(attribute, SessionMgtConstants.FLD_LOGIN_ID) &&
                    !collator.equals(attribute, SessionMgtConstants.FLD_SESSION_ID) &&
                    !collator.equals(attribute, SessionMgtConstants.FLD_USER_AGENT) &&
                    !collator.equals(attribute, SessionMgtConstants.FLD_LAST_ACCESS_TIME) &&
                    !collator.equals(attribute, SessionMgtConstants.FLD_LOGIN_TIME) &&
                    !collator.equals(attribute, SessionMgtConstants.FLD_TIME_CREATED_SINCE) &&
                    !collator.equals(attribute, SessionMgtConstants.FLD_TIME_CREATED_UNTIL)) {

                String message = attribute + " is not a valid filter attribute name";
                throw new SessionManagementClientException(ERROR_CODE_INVALID_DATA,
                        String.format(ERROR_CODE_INVALID_DATA.getDescription(), message));

            } else if (collator.equals(attribute, SessionMgtConstants.FLD_LAST_ACCESS_TIME) ||
                    collator.equals(attribute, SessionMgtConstants.FLD_LOGIN_TIME)) {

                if (!collator.equals(operation, SessionMgtConstants.LE) &&
                        !collator.equals(operation, SessionMgtConstants.GE)) {
                    String message = operation + " is not a supported operation for " + attribute;
                    throw new SessionManagementClientException(ERROR_CODE_INVALID_DATA,
                            String.format(ERROR_CODE_INVALID_DATA.getDescription(), message));
                }
                if (!StringUtils.isNumeric(value)) {
                    String message = attribute + "'s value is not a valid number: " + value;
                    throw new SessionManagementClientException(ERROR_CODE_INVALID_DATA,
                            String.format(ERROR_CODE_INVALID_DATA.getDescription(), message));
                }

            } else if (collator.equals(attribute, SessionMgtConstants.FLD_IP_ADDRESS) &&
                    !collator.equals(operation, SessionMgtConstants.EQ)) {

                String message = operation + " is not a supported operation for " + attribute;
                throw new SessionManagementClientException(ERROR_CODE_INVALID_DATA,
                        String.format(ERROR_CODE_INVALID_DATA.getDescription(), message));

            } else if ((collator.equals(attribute, SessionMgtConstants.FLD_APPLICATION) ||
                    collator.equals(attribute, SessionMgtConstants.FLD_LOGIN_ID) ||
                    collator.equals(attribute, SessionMgtConstants.FLD_SESSION_ID) ||
                    collator.equals(attribute, SessionMgtConstants.FLD_USER_AGENT)) &&
                    !collator.equals(operation, SessionMgtConstants.EQ) &&
                    !collator.equals(operation, SessionMgtConstants.SW) &&
                    !collator.equals(operation, SessionMgtConstants.EW) &&
                    !collator.equals(operation, SessionMgtConstants.CO)) {

                String message = operation + " is not a supported operation for " + attribute;
                throw new SessionManagementClientException(ERROR_CODE_INVALID_DATA,
                        String.format(ERROR_CODE_INVALID_DATA.getDescription(), message));
            }
        }
    }

    private boolean isOrganizationUser() {

        return StringUtils.isNotEmpty(PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getUserResidentOrganizationId());
    }
}
