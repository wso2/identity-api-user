package org.wso2.carbon.identity.rest.api.user.session.v1;

import org.wso2.carbon.identity.rest.api.user.session.v1.*;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.*;

import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionsDTO;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.ErrorDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import javax.ws.rs.core.Response;

public abstract class MeApiService {
    public abstract Response getSessionsOfLoggedInUser(Integer limit,Integer offset,String filter,String sort);
    public abstract Response terminateSessionByLoggedInUser(String sessionId);
    public abstract Response terminateSessionsByLoggedInUser();
}

