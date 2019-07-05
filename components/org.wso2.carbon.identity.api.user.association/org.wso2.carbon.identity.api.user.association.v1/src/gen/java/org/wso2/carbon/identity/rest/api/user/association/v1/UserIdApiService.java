package org.wso2.carbon.identity.rest.api.user.association.v1;

import org.wso2.carbon.identity.rest.api.user.association.v1.*;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.*;

import org.wso2.carbon.identity.rest.api.user.association.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.UserDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationRequestDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import javax.ws.rs.core.Response;

public abstract class UserIdApiService {
    public abstract Response userIdAssociationsDelete(String userId);
    public abstract Response userIdAssociationsGet(String userId);
    public abstract Response userIdAssociationsPost(AssociationRequestDTO association,String userId);
}

