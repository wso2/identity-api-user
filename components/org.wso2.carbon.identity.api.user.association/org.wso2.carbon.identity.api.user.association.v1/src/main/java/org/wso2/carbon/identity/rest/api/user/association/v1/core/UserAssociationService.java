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
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationUserRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.FederatedAssociationDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.UserDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.util.UserAssociationServiceHolder;
import org.wso2.carbon.identity.user.account.association.dto.UserAccountAssociationDTO;
import org.wso2.carbon.identity.user.account.association.exception.UserAccountAssociationClientException;
import org.wso2.carbon.identity.user.account.association.exception.UserAccountAssociationException;
import org.wso2.carbon.identity.user.profile.mgt.association.federation.exception.FederatedAssociationManagerClientException;
import org.wso2.carbon.identity.user.profile.mgt.association.federation.exception.FederatedAssociationManagerException;
import org.wso2.carbon.identity.user.profile.mgt.association.federation.model.FederatedAssociation;
import org.wso2.carbon.user.core.util.UserCoreUtil;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.ASSOCIATION_ERROR_PREFIX;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.ERROR_MSG_DELIMITER;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.ErrorMessages.ERROR_CODE_PW_MANDATORY;
import static org.wso2.carbon.user.core.UserCoreConstants.DOMAIN_SEPARATOR;

/**
 * This service is used to execute the association related APIs through the UserAccountConnector OSGI service.
 */
public class UserAssociationService {

    private static final Log log = LogFactory.getLog(UserAssociationService.class);

    public List<UserDTO> getAssociationsOfUser(String userId) {

        try {
            UserAccountAssociationDTO[] accountAssociationsOfUser = UserAssociationServiceHolder
                    .getUserAccountConnector().getAccountAssociationsOfUser(userId);
            return getUserAssociationsDTOs(accountAssociationsOfUser);
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e, "Error while getting associations of user: " + userId);
        }
    }

    public List<FederatedAssociationDTO> getFederatedAssociationsOfUser(String userId) {

        try {
            FederatedAssociation[] federatedAccountAssociationsOfUser = UserAssociationServiceHolder
                    .getFederatedAssociationManager().getFederatedAssociationsOfUser(getUser(userId));
            return getFederatedAssociationDTOs(federatedAccountAssociationsOfUser);
        } catch (FederatedAssociationManagerException e) {
            throw handleFederatedAssociationManagerException(e, "Error while getting associations of user: " + userId);
        }
    }

    public void createUserAccountAssociation(AssociationUserRequestDTO associationUserRequestDTO) {

        try {
            if (associationUserRequestDTO.getPassword() == null) {
                throw new UserAccountAssociationClientException(ERROR_CODE_PW_MANDATORY.getCode(),
                        ERROR_CODE_PW_MANDATORY.getDescription());
            }
            UserAssociationServiceHolder.getUserAccountConnector()
                    .createUserAccountAssociation(associationUserRequestDTO.getUserId(),
                            associationUserRequestDTO.getPassword().toCharArray());
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e,
                    "Error while adding associations of user: " + associationUserRequestDTO.getUserId());
        }
    }

    public boolean switchLoggedInUser(String userName)  {

        try {
            return UserAssociationServiceHolder.getUserAccountConnector().switchLoggedInUser(userName);
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e, "Error while switching user: " + userName);
        }
    }

    public void deleteUserAccountAssociation(String userId) {

        try {
            UserAssociationServiceHolder.getUserAccountConnector().deleteUserAccountAssociation(userId);
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e, "Error while deleting user association: " + userId);
        }
    }

    public void deleteAssociatedUserAccount(String ownerUserId, String associatedUserId) {

        try {
            UserAssociationServiceHolder.getUserAccountConnector().deleteAssociatedUserAccount(ownerUserId,
                    associatedUserId);
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e, "Error while deleting user association of the user: "
                    + ownerUserId + ", with the user: " + associatedUserId);
        }
    }

    public void deleteFederatedUserAccountAssociation(String userId, String federatedAssociationId) {

        try {
            UserAssociationServiceHolder.getFederatedAssociationManager()
                    .deleteFederatedAssociation(getUser(userId), federatedAssociationId);
        } catch (FederatedAssociationManagerException e) {
            throw handleFederatedAssociationManagerException(e, "Error while deleting federated user association: "
                    + userId);
        }
    }

    public void deleteFederatedUserAccountAssociation(String userId) {

        try {
            UserAssociationServiceHolder.getFederatedAssociationManager().deleteFederatedAssociation(getUser(userId));
        } catch (FederatedAssociationManagerException e) {
            throw handleFederatedAssociationManagerException(e, "Error while deleting federated user association: "
                    + userId);
        }
    }

    private List<UserDTO> getUserAssociationsDTOs(UserAccountAssociationDTO[] accountAssociationsOfUser) {

        List<UserDTO> userDTOList = new ArrayList<>();

        for (UserAccountAssociationDTO userAccountAssociationDTO : accountAssociationsOfUser) {
            userDTOList.add(getUserDTO(userAccountAssociationDTO));
        }
        return userDTOList;
    }

    private List<FederatedAssociationDTO> getFederatedAssociationDTOs(FederatedAssociation[]
                                                                              federatedAssociations) {

        List<FederatedAssociationDTO> federatedAssociationDTOs = new ArrayList<>();
        for (FederatedAssociation federatedAssociation : federatedAssociations) {
            federatedAssociationDTOs.add(getFederatedAssociationDTO(federatedAssociation));
        }
        return federatedAssociationDTOs;
    }

    private UserDTO getUserDTO(UserAccountAssociationDTO userAccountAssociationDTO) {

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(getEncodedUserId(userAccountAssociationDTO));
        userDTO.setUsername(userAccountAssociationDTO.getUsername());
        userDTO.setUserStoreDomain(userAccountAssociationDTO.getDomain());
        userDTO.setTenantDomain(userAccountAssociationDTO.getTenantDomain());

        userDTO.setFirstName(userAccountAssociationDTO.getFirstName());
        userDTO.setLastName(userAccountAssociationDTO.getLastName());
        userDTO.setEmail(userAccountAssociationDTO.getEmail());
        return userDTO;
    }

    private FederatedAssociationDTO getFederatedAssociationDTO(FederatedAssociation federatedAssociation) {

        FederatedAssociationDTO federatedAssociationDTO = new FederatedAssociationDTO();
        federatedAssociationDTO.setId(federatedAssociation.getId());
        federatedAssociationDTO.setIdpId(federatedAssociation.getIdpId());
        federatedAssociationDTO.setFederatedUserId(federatedAssociation.getFederatedUserId());
        return federatedAssociationDTO;
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

    private APIError handleFederatedAssociationManagerException(FederatedAssociationManagerException e,
                                                                String message) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(e.getErrorCode())
                .withMessage(message)
                .build(log, e, e.getMessage());

        Response.Status status;

        if (e instanceof FederatedAssociationManagerClientException) {
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

        handleCommonErrorDescription(e, errorResponse);
    }

    private void handleErrorDescription(FederatedAssociationManagerException e, ErrorResponse errorResponse) {

        handleCommonErrorDescription(e, errorResponse);
    }

    private void handleCommonErrorDescription(Exception e, ErrorResponse errorResponse) {

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

    private String getEncodedUserId(UserAccountAssociationDTO userAccountAssociationDTO) {

        String domainAwareUserName = userAccountAssociationDTO.getDomain() + DOMAIN_SEPARATOR
                + userAccountAssociationDTO.getUsername();
        return Base64.getUrlEncoder().encodeToString(domainAwareUserName.getBytes(StandardCharsets.UTF_8));
    }

    private User getUser(String userId) {

        User user = new User();
        user.setTenantDomain(MultitenantUtils.getTenantDomain(userId));
        user.setUserStoreDomain(UserCoreUtil.extractDomainFromName(userId));
        user.setUserName(MultitenantUtils.getTenantAwareUsername(userId));
        return user;
    }
}
