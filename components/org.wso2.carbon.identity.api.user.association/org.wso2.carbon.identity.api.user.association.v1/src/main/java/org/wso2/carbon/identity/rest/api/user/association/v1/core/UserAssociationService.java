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

public class UserAssociationService {

    private static final Log log = LogFactory.getLog(UserAssociationService.class);

    public List<UserDTO> getAssociationsOfUser(String userId) {

        try {
            UserAccountAssociationDTO[] accountAssociationsOfUser = Utils.getUserAccountConnector()
                    .getAccountAssociationsOfUser(userId);
            return getUserAssociationsDTO(accountAssociationsOfUser);
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e);
        }
    }

    public void createUserAccountAssociation(AssociationUserRequestDTO associationUserRequestDTO){

        try {
            //TODO handle password null
            Utils.getUserAccountConnector().createUserAccountAssociation(associationUserRequestDTO.getUserId(),
                    associationUserRequestDTO.getPassword().toCharArray());
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e);
        }
    }

    public void createUserAccountAssociation(AssociationRequestDTO association, String userId) {

        try {
            Utils.getUserAccountConnector().createUserAccountAssociation(userId, association.getAssociateUserId());
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e);
        }
    }


    public boolean switchLoggedInUser(String userName)  {

        try {
            return Utils.getUserAccountConnector().switchLoggedInUser(userName);
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e);
        }
    }

    public void deleteUserAccountAssociation(String userId) {

        try {
            Utils.getUserAccountConnector().deleteUserAccountAssociation(userId);
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e);
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
        return userDTO;
    }

    private APIError handleUserAccountAssociationException(UserAccountAssociationException e) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(e.getErrorCode())
                .withMessage(e.getMessage())
                .build(log, e, e.getMessage());

        Response.Status status;

        if (e instanceof UserAccountAssociationClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode : ASSOCIATION_ERROR_PREFIX
                        + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }
}
