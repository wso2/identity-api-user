package org.wso2.carbon.identity.rest.api.user.fido2.v1;

import org.wso2.carbon.identity.rest.api.user.fido2.v1.*;
import org.wso2.carbon.identity.rest.api.user.fido2.v1.dto.*;

import org.wso2.carbon.identity.rest.api.user.fido2.v1.dto.ErrorDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import javax.ws.rs.core.Response;

public abstract class MeApiService {
    public abstract Response meWebauthnCredentialIdDelete(String credentialId);
    public abstract Response meWebauthnFinishRegistrationPost(String response);
    public abstract Response meWebauthnGet(String username);
    public abstract Response meWebauthnStartRegistrationPost(String appId);
}

