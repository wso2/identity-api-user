package org.wso2.carbon.identity.api.user.authorized.apps.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.user.authorized.apps.v1.*;
import org.wso2.carbon.identity.api.user.authorized.apps.v1.core.AuthorizedAppsService;
import org.wso2.carbon.identity.api.user.authorized.apps.v1.dto.*;


import org.wso2.carbon.identity.api.user.authorized.apps.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.api.user.authorized.apps.v1.dto.AuthorizedAppDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.function.UserIdToUser;
import org.wso2.carbon.identity.application.common.model.User;

import javax.ws.rs.core.Response;

public class UserIdApiServiceImpl extends UserIdApiService {

    @Autowired
    private AuthorizedAppsService authorizedAppsService;

    @Override
    public Response deleteUserAuthorizedApps(String userId) {

        authorizedAppsService.deleteUserAuthorizedApps(getUser(userId));
        return Response.ok().build();
    }

    @Override
    public Response deleteUserAuthorizedAppsByAppId(String userId, String applicationId) {

        authorizedAppsService.deleteUserAuthorizedApps(getUser(userId), applicationId);
        return Response.ok().build();
    }

    @Override
    public Response listUserAuthorizedApps(String userId) {

        List<AuthorizedAppDTO> authorizedAppDTOs = authorizedAppsService.listUserAuthorizedApps(getUser(userId));
        return Response.ok().entity(authorizedAppDTOs).build();
    }

    @Override
    public Response listUserAuthorizedAppsByAppId(String userId, String applicationId) {

        AuthorizedAppDTO authorizedAppDTO = authorizedAppsService.listUserAuthorizedAppsByAppId(getUser(userId),
                                                                                                applicationId);
        return Response.ok().entity(authorizedAppDTO).build();
    }

    private User getUser(String userId) {

        return new UserIdToUser().apply(userId, ContextLoader.getTenantDomainFromContext());
    }
}
