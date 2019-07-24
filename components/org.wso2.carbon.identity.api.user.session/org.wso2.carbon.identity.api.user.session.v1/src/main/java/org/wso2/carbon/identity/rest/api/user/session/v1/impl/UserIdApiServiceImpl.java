package org.wso2.carbon.identity.rest.api.user.session.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.function.UserIdToUser;
import org.wso2.carbon.identity.rest.api.user.session.v1.UserIdApiService;
import org.wso2.carbon.identity.rest.api.user.session.v1.core.SessionManagementService;

import javax.ws.rs.core.Response;

public class UserIdApiServiceImpl extends UserIdApiService {

    @Autowired
    private SessionManagementService sessionManagementService;

    @Override
    public Response getSessionsByUserId(String userId, Integer limit, Integer offset, String filter, String sort) {
        return Response.ok().entity(sessionManagementService.getSessionsBySessionId(
                new UserIdToUser().apply(userId, ContextLoader.getTenantDomainFromContext()), limit, offset, filter,
                sort)).build();
    }

    @Override
    public Response terminateSessionBySessionId(String userId, String sessionId) {

        sessionManagementService.terminateSessionBySessionId(new UserIdToUser().apply(userId, ContextLoader
                .getTenantDomainFromContext()), sessionId);

        return Response.noContent().build();
    }

    @Override
    public Response terminateSessionsByUserId(String userId) {

        sessionManagementService.terminateSessionsByUserId(new UserIdToUser().apply(userId, ContextLoader
                .getTenantDomainFromContext()));

        return Response.noContent().build();
    }
}
