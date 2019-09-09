/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.identity.rest.api.user.association.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationUserRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.UserDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.util.Utils;
import org.wso2.carbon.identity.user.account.association.dto.UserAccountAssociationDTO;
import org.wso2.carbon.identity.user.account.association.exception.UserAccountAssociationClientException;
import org.wso2.carbon.identity.user.account.association.exception.UserAccountAssociationException;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.ASSOCIATION_ERROR_PREFIX;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.ERROR_MSG_DELIMITER;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.ErrorMessages.ERROR_CODE_PW_MANDATORY;

/**
 * This service is used to execute the association related APIs through the UserAccountConnector OSGI service.
 */
public class UserAssociationService {

    private static final Log log = LogFactory.getLog(UserAssociationService.class);

    public List<UserDTO> getAssociationsOfUser(String userId) {

        try {
            UserAccountAssociationDTO[] accountAssociationsOfUser = Utils.getUserAccountConnector()
                    .getAccountAssociationsOfUser(userId);
            return getUserAssociationsDTO(accountAssociationsOfUser);
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e, "Error while getting associations of user: " + userId);
        }
    }

    public void createUserAccountAssociation(AssociationUserRequestDTO associationUserRequestDTO) {

        try {
            if (associationUserRequestDTO.getPassword() == null) {
                throw new UserAccountAssociationClientException(ERROR_CODE_PW_MANDATORY.getCode(),
                        ERROR_CODE_PW_MANDATORY.getDescription());
            }
            Utils.getUserAccountConnector().createUserAccountAssociation(associationUserRequestDTO.getUserId(),
                    associationUserRequestDTO.getPassword().toCharArray());
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e,
                    "Error while adding associations of user: " + associationUserRequestDTO.getUserId());
        }
    }

    public void createUserAccountAssociation(AssociationRequestDTO association, String userId) {

        try {
            Utils.getUserAccountConnector().createUserAccountAssociation(userId, association.getAssociateUserId());
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e, "Error while associating user: " + userId);
        }
    }


    public boolean switchLoggedInUser(String userName)  {

        try {
            return Utils.getUserAccountConnector().switchLoggedInUser(userName);
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e, "Error while switching user: " + userName);
        }
    }

    public void deleteUserAccountAssociation(String userId) {

        try {
            Utils.getUserAccountConnector().deleteUserAccountAssociation(userId);
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e, "Error while deleting user association: " + userId);
        }
    }

    public void deleteAllUserAssociations(String userId) {

        try {
            Utils.getUserAccountConnector().deleteAllUserAssociations(userId);
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e, "Error while deleting user associations: " + userId);
        }
    }

    private List<UserDTO> getUserAssociationsDTO(UserAccountAssociationDTO[] accountAssociationsOfUser) {

        List<UserDTO> userDTOList = new ArrayList<>();

        for (UserAccountAssociationDTO userAccountAssociationDTO : accountAssociationsOfUser) {
            userDTOList.add(getUserDTO(userAccountAssociationDTO));
        }
        return userDTOList;
    }

    private UserDTO getUserDTO(UserAccountAssociationDTO userAccountAssociationDTO) {

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userAccountAssociationDTO.getUsername());
        userDTO.setUsername(userAccountAssociationDTO.getUsername());
        userDTO.setUserStoreDomain(userAccountAssociationDTO.getDomain());
        userDTO.setTenantDomain(userAccountAssociationDTO.getTenantDomain());
        return userDTO;
    }

    private APIError handleUserAccountAssociationException(UserAccountAssociationException e, String message) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(e.getErrorCode())
                .withMessage(message)
                .build(log, e, e.getMessage());

        Response.Status status;

        if (e instanceof UserAccountAssociationClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode : ASSOCIATION_ERROR_PREFIX
                        + errorCode;
                errorResponse.setCode(errorCode);
            }
            handleErrorDescription(e, errorResponse);
            status = Response.Status.BAD_REQUEST;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    private void handleErrorDescription(UserAccountAssociationException e, ErrorResponse errorResponse) {

        if (e.getMessage() != null && e.getMessage().contains(ERROR_MSG_DELIMITER)) {
            String[] splittedMessage = e.getMessage().split(ERROR_MSG_DELIMITER);
            if (splittedMessage.length == 2) {
                errorResponse.setDescription(splittedMessage[1].trim());
            } else {
                errorResponse.setDescription(e.getMessage());
            }
        } else if (!e.getMessage().contains(ERROR_MSG_DELIMITER)) {
            errorResponse.setDescription(e.getMessage());
        }
    }
}
