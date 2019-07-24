package org.wso2.carbon.identity.rest.api.user.session.v1.impl;

import org.wso2.carbon.identity.rest.api.user.session.v1.ApiResponseMessage;
import org.wso2.carbon.identity.rest.api.user.session.v1.MeApiService;

import javax.ws.rs.core.Response;

public class MeApiServiceImpl extends MeApiService {

    @Override
    public Response getSessionsOfLoggedInUser() {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    @Override
    public Response terminateSessionByLoggedInUser(String sessionId) {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    @Override
    public Response terminateSessionsByLoggedInUser() {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
