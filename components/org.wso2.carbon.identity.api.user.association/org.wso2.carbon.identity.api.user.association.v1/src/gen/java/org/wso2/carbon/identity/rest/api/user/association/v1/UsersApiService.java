package org.wso2.carbon.identity.rest.api.user.association.v1;

import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationUserRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationSwitchRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationRequestDTO;

import javax.ws.rs.core.Response;

public abstract class UsersApiService {
    public abstract Response usersMeAssociationsAssociateUserIdDelete(String associateUserId);
    public abstract Response usersMeAssociationsDelete();
    public abstract Response usersMeAssociationsGet();
    public abstract Response usersMeAssociationsPost(AssociationUserRequestDTO association);
    public abstract Response usersMeAssociationsSwitchPut(AssociationSwitchRequestDTO switchUserReqeust);
    public abstract Response usersUserIdAssociationsAssociateUserIdDelete(String userId,String associateUserId);
    public abstract Response usersUserIdAssociationsDelete(String userId);
    public abstract Response usersUserIdAssociationsGet(String userId);
    public abstract Response usersUserIdAssociationsPost(AssociationRequestDTO association,String userId);
}

