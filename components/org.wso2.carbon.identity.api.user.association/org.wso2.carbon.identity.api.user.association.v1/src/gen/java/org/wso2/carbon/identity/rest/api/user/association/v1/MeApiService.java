package org.wso2.carbon.identity.rest.api.user.association.v1;

import org.wso2.carbon.identity.rest.api.user.association.v1.*;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.*;

import org.wso2.carbon.identity.rest.api.user.association.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.UserDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationUserRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationSwitchRequestDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import javax.ws.rs.core.Response;

public abstract class MeApiService {
    public abstract Response meAssociationsDelete();
    public abstract Response meAssociationsGet();
    public abstract Response meAssociationsPost(AssociationUserRequestDTO association);
    public abstract Response meAssociationsSwitchPut(AssociationSwitchRequestDTO switchUserReqeust);
}

