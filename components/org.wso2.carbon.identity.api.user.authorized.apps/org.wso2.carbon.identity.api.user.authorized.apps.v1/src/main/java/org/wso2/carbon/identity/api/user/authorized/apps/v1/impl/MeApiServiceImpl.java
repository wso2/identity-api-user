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

public class MeApiServiceImpl extends MeApiService {

    @Autowired
    private AuthorizedAppsService authorizedAppsService;

    @Override
    public Response deleteLoggedInUserAuthorizedAppByAppId(String applicationId) {

        authorizedAppsService.deleteUserAuthorizedApps(getUser(), applicationId);
        return Response.ok().build();
    }

    @Override
    public Response deleteLoggedInUserAuthorizedApps() {

        authorizedAppsService.deleteUserAuthorizedApps(getUser());
        return Response.ok().build();
    }

    @Override
    public Response getLoggedInUserAuthorizedAppByAppId(String applicationId) {

        authorizedAppsService.deleteUserAuthorizedApps(getUser(), applicationId);
        return Response.ok().build();
    }

    @Override
    public Response listLoggedInUserAuthorizedApps() {

        List<AuthorizedAppDTO> authorizedAppDTOs = authorizedAppsService.listUserAuthorizedApps(getUser());
        return Response.ok().entity(authorizedAppDTOs).build();
    }

    private User getUser() {

        return new UserIdToUser().apply(ContextLoader.getUsernameFromContext(), ContextLoader
                .getTenantDomainFromContext());
    }
}
