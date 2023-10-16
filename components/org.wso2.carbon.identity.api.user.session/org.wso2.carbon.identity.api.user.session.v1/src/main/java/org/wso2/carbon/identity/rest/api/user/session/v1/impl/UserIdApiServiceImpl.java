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

package org.wso2.carbon.identity.rest.api.user.session.v1.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.api.user.common.Util;
import org.wso2.carbon.identity.api.user.session.common.util.SessionManagementServiceHolder;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.rest.api.user.session.v1.UserIdApiService;
import org.wso2.carbon.identity.rest.api.user.session.v1.core.SessionManagementService;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionDTO;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionsDTO;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.common.AbstractUserStoreManager;

import java.util.Optional;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * API service implementation of a specific user's session related operations.
 */
public class UserIdApiServiceImpl extends UserIdApiService {

    private static final Log log = LogFactory.getLog(UserIdApiServiceImpl.class);

    @Autowired
    private SessionManagementService sessionManagementService;

    @Override
    public Response getSessionBySessionId(String userId, String sessionId) {

        Util.validateUserId(SessionManagementServiceHolder.getRealmService(), userId,
                IdentityTenantUtil.resolveTenantDomain());

        Optional<SessionDTO> session = sessionManagementService.getSessionBySessionId(userId, sessionId);
        if (session.isPresent()) {
            return Response.ok().entity(session.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response getSessionsByUserId(String userId, Integer limit, Integer offset, String filter, String sort) {

        Util.validateUserId(SessionManagementServiceHolder.getRealmService(), userId,
                IdentityTenantUtil.resolveTenantDomain());

        SessionsDTO sessionsOfUser = sessionManagementService.getSessionsByUserId(userId, limit, offset, filter, sort);
        if (sessionsOfUser == null || sessionsOfUser.getSessions().isEmpty()) {
            return Response.ok().entity("{}").type(MediaType.APPLICATION_JSON).build();
        } else {
            return Response.ok().entity(sessionsOfUser).build();
        }
    }

    @Override
    public Response terminateSessionBySessionId(String userId, String sessionId) {

        Util.validateUserId(SessionManagementServiceHolder.getRealmService(), userId,
                IdentityTenantUtil.resolveTenantDomain());
        sessionManagementService.terminateSessionBySessionId(userId, sessionId);
        return Response.noContent().build();
    }

    @Override
    public Response terminateSessionsByUserId(String userId) {

        try {
            UserRealm userRealm = CarbonContext.getThreadLocalCarbonContext().getUserRealm();
            AbstractUserStoreManager userStoreManager = (AbstractUserStoreManager) userRealm.getUserStoreManager();

            if (userStoreManager == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Userstore Manager is null");
                }
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }

            String username = CarbonContext.getThreadLocalCarbonContext().getUsername();
            String adminUserName = userRealm.getRealmConfiguration().getAdminUserName();
            String adminUserID = userStoreManager.getUserIDFromUserName(adminUserName);

            if (!StringUtils.equals(username, adminUserName) && StringUtils.equals(userId, adminUserID)) {
                if (log.isDebugEnabled()) {
                    log.debug("Forbidden operation. Admin user is not allowed to " +
                            "terminate the organization owner's sessions");
                }
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            Util.validateUserId(SessionManagementServiceHolder.getRealmService(), userId,
                    IdentityTenantUtil.resolveTenantDomain());
            sessionManagementService.terminateSessionsByUserId(userId);
            return Response.noContent().build();
        } catch (UserStoreException e) {
            log.error("Error occurred while invoking userstore manager.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }
}
