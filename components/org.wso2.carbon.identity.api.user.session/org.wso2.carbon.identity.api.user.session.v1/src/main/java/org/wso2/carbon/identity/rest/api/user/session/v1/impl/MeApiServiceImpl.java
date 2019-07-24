package org.wso2.carbon.identity.rest.api.user.session.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.function.UserIdToUser;
import org.wso2.carbon.identity.rest.api.user.session.v1.ApiResponseMessage;
import org.wso2.carbon.identity.rest.api.user.session.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.session.v1.core.SessionManagementService;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.ContextLoader.getUserFromContext;
import static org.wso2.carbon.identity.api.user.common.ContextLoader.getUsernameFromContext;

public class MeApiServiceImpl extends MeApiService {

    @Autowired
    private SessionManagementService sessionManagementService;

    @Override
    public Response getSessionsOfLoggedInUser(Integer limit, Integer offset, String filter, String sort) {

        return Response.ok().entity(sessionManagementService.getSessionsBySessionId(getUserFromContext(), limit,
                offset, filter, sort)).build();
    }

    @Override
    public Response terminateSessionByLoggedInUser(String sessionId) {

        sessionManagementService.terminateSessionBySessionId(getUserFromContext(), sessionId);

        return Response.noContent().build();
    }

    @Override
    public Response terminateSessionsByLoggedInUser() {

        sessionManagementService.terminateSessionsByUserId(getUserFromContext());

        return Response.noContent().build();
    }
}
