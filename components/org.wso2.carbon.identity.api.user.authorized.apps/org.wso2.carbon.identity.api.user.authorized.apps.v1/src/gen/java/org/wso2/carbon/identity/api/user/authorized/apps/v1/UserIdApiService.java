package org.wso2.carbon.identity.api.user.authorized.apps.v1;

import org.wso2.carbon.identity.api.user.authorized.apps.v1.*;
import org.wso2.carbon.identity.api.user.authorized.apps.v1.dto.*;

import org.wso2.carbon.identity.api.user.authorized.apps.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.api.user.authorized.apps.v1.dto.AuthorizedAppDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import javax.ws.rs.core.Response;

public abstract class UserIdApiService {
    public abstract Response deleteUserAuthorizedApps(String userId);
    public abstract Response deleteUserAuthorizedAppsByAppId(String userId,String applicationId);
    public abstract Response listUserAuthorizedApps(String userId);
    public abstract Response listUserAuthorizedAppsByAppId(String userId,String applicationId);
}

