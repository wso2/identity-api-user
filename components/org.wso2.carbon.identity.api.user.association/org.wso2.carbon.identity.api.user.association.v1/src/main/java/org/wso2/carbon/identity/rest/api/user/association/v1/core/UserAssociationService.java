/*
 * Copyright (c) 2019-2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.rest.api.user.association.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.user.common.function.UniqueIdToUser;
import org.wso2.carbon.identity.api.user.common.function.UserToUniqueId;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.AssociationUserRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.BulkAssociationOperationResponseDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.BulkAssociationOperationResponseStatusDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.BulkFederatedAssociationOperationDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.BulkFederatedAssociationRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.BulkFederatedAssociationResponseDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.FederatedAssociationDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.FederatedAssociationRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.IdpDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.UserDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.model.BulkAssociationPathObject;
import org.wso2.carbon.identity.rest.api.user.association.v1.util.UserAssociationServiceHolder;
import org.wso2.carbon.identity.user.account.association.UserAccountConnector;
import org.wso2.carbon.identity.user.account.association.dto.UserAccountAssociationDTO;
import org.wso2.carbon.identity.user.account.association.exception.UserAccountAssociationClientException;
import org.wso2.carbon.identity.user.account.association.exception.UserAccountAssociationException;
import org.wso2.carbon.identity.user.profile.mgt.association.federation.FederatedAssociationManager;
import org.wso2.carbon.identity.user.profile.mgt.association.federation.exception.FederatedAssociationManagerClientException;
import org.wso2.carbon.identity.user.profile.mgt.association.federation.exception.FederatedAssociationManagerException;
import org.wso2.carbon.identity.user.profile.mgt.association.federation.model.FederatedAssociation;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.user.core.util.UserCoreUtil;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.ASSOCIATION_ERROR_PREFIX;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.ERROR_MSG_DELIMITER;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.ErrorMessages.ERROR_CODE_PW_MANDATORY;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.HTTP_DELETE;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.HTTP_POST;

/**
 * This service is used to execute the association related APIs through the UserAccountConnector OSGI service.
 */
public class UserAssociationService {

    private static final Log log = LogFactory.getLog(UserAssociationService.class);

    private final UserAccountConnector userAccountConnector;
    private final FederatedAssociationManager federatedAssociationManager;
    private final RealmService realmService;

    public UserAssociationService(UserAccountConnector userAccountConnector, FederatedAssociationManager
            federatedAssociationManager, RealmService realmService) {

        this.userAccountConnector = userAccountConnector;
        this.federatedAssociationManager = federatedAssociationManager;
        this.realmService = realmService;
    }

    public List<UserDTO> getAssociationsOfUser(String userId) {

        try {
            UserAccountAssociationDTO[] accountAssociationsOfUser = userAccountConnector
                    .getAccountAssociationsOfUser(userId);

            return getUserAssociationsDTOs(accountAssociationsOfUser);
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e, "Error while getting associations of user: " + userId);
        }
    }

    public List<FederatedAssociationDTO> getFederatedAssociationsOfUser(String userId) {

        try {
            FederatedAssociation[] federatedAccountAssociationsOfUser = federatedAssociationManager
                    .getFederatedAssociationsOfUser(getUser(userId));
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
            userAccountConnector.createUserAccountAssociation(associationUserRequestDTO.getUserId(),
                    associationUserRequestDTO.getPassword().toCharArray());
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e,
                    "Error while adding associations of user: " + associationUserRequestDTO.getUserId());
        }
    }

    public boolean switchLoggedInUser(String userName) {

        try {
            return userAccountConnector.switchLoggedInUser(userName);
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e, "Error while switching user: " + userName);
        }
    }

    public void deleteUserAccountAssociation(String userId) {

        try {
            userAccountConnector.deleteUserAccountAssociation(userId);
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e, "Error while deleting user association: " + userId);
        }
    }

    public void deleteAssociatedUserAccount(String ownerUserId, String associatedUserId) {

        try {
            userAccountConnector.deleteAssociatedUserAccount(ownerUserId, associatedUserId);
        } catch (UserAccountAssociationException e) {
            throw handleUserAccountAssociationException(e, "Error while deleting user association of the user: "
                    + ownerUserId + ", with the user: " + associatedUserId);
        }
    }

    public void deleteFederatedUserAccountAssociation(String userId, String federatedAssociationId) {

        try {
            federatedAssociationManager.deleteFederatedAssociation(getUser(userId), federatedAssociationId);
        } catch (FederatedAssociationManagerException e) {
            throw handleFederatedAssociationManagerException(e, "Error while deleting federated user association: "
                    + userId);
        }
    }

    public void deleteFederatedUserAccountAssociation(String userId) {

        try {
            federatedAssociationManager.deleteFederatedAssociation(getUser(userId));
        } catch (FederatedAssociationManagerException e) {
            throw handleFederatedAssociationManagerException(e, "Error while deleting federated user association: "
                    + userId);
        }
    }

    public BulkFederatedAssociationResponseDTO handleBulkFederatedAssociations(BulkFederatedAssociationRequestDTO
                                                                                       bulkAssociationRequest) {

        BulkFederatedAssociationResponseDTO bulkAssociationResponse = new BulkFederatedAssociationResponseDTO();
        List<BulkAssociationOperationResponseDTO> bulkAssociationOperationResponses = new ArrayList<>();
        bulkAssociationResponse.setOperations(bulkAssociationOperationResponses);

        int failOnErrorsCount = 0;
        boolean failOnErrors = false;
        int errorCount = 0;
        if (bulkAssociationRequest.getFailOnErrors() != null) {
            failOnErrorsCount = bulkAssociationRequest.getFailOnErrors();
            failOnErrors = true;
        }
        List<BulkFederatedAssociationOperationDTO> bulkOperations = bulkAssociationRequest.getOperations();

        for (BulkFederatedAssociationOperationDTO bulkOperation : bulkOperations) {
            if (failOnErrors && errorCount >= failOnErrorsCount) {
                break;
            }

            BulkAssociationOperationResponseDTO bulkAssociationOperationResponse =
                    new BulkAssociationOperationResponseDTO();
            BulkAssociationOperationResponseStatusDTO bulkAssociationOperationResponseStatus =
                    new BulkAssociationOperationResponseStatusDTO();
            bulkAssociationOperationResponse.setStatus(bulkAssociationOperationResponseStatus);
            bulkAssociationOperationResponse.setBulkId(bulkOperation.getBulkId());

            try {
                if (HTTP_POST.equals(bulkOperation.getMethod())) {
                    handleBulkFedAssociationPostOperation(bulkOperation);
                    bulkAssociationOperationResponseStatus.setStatusCode(Response.Status.CREATED.getStatusCode());
                } else if (HTTP_DELETE.equals(bulkOperation.getMethod())) {
                    handleBulkFedAssociationDeleteOperation(bulkOperation);
                    bulkAssociationOperationResponseStatus.setStatusCode(Response.Status.NO_CONTENT.getStatusCode());
                } else {
                    bulkAssociationOperationResponseStatus.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
                    bulkAssociationOperationResponseStatus.setErrorCode("BULK_OPERATION_INVALID_METHOD");
                    bulkAssociationOperationResponseStatus.setErrorMessage(
                            "Invalid method: " + bulkOperation.getMethod());
                    bulkAssociationOperationResponseStatus.setErrorDescription("The method " + bulkOperation.getMethod()
                            + " is not supported for bulk operations.");
                    errorCount++;
                    bulkAssociationOperationResponses.add(bulkAssociationOperationResponse);
                    continue;
                }
            } catch (APIError e) {
                bulkAssociationOperationResponseStatus.setStatusCode(e.getStatus().getStatusCode());
                bulkAssociationOperationResponseStatus.setErrorCode(e.getCode());
                bulkAssociationOperationResponseStatus.setErrorMessage(e.getMessage());
                bulkAssociationOperationResponseStatus.setErrorDescription(e.getResponseEntity().getDescription());
                errorCount++;
            }
            bulkAssociationOperationResponses.add(bulkAssociationOperationResponse);
        }
        return bulkAssociationResponse;
    }

    private void handleBulkFedAssociationPostOperation(
            BulkFederatedAssociationOperationDTO bulkFederatedAssociationOperationDTO) {

        BulkAssociationPathObject bulkAssociationPathObject =
                BulkAssociationPathObject.parseBulkAssociationPathObject(
                        bulkFederatedAssociationOperationDTO.getMethod(),
                        bulkFederatedAssociationOperationDTO.getPath());

        addFederatedUserAccountAssociation(
                bulkAssociationPathObject.getUserId(), bulkFederatedAssociationOperationDTO.getData().getIdp(),
                bulkFederatedAssociationOperationDTO.getData().getFederatedUserId());
    }

    private void handleBulkFedAssociationDeleteOperation(
            BulkFederatedAssociationOperationDTO bulkFederatedAssociationOperationDTO) {

        BulkAssociationPathObject bulkAssociationPathObject =
                BulkAssociationPathObject.parseBulkAssociationPathObject(
                        bulkFederatedAssociationOperationDTO.getMethod(),
                        bulkFederatedAssociationOperationDTO.getPath());
        deleteFederatedUserAccountAssociation(
                bulkAssociationPathObject.getUserId(), bulkAssociationPathObject.getAssociationId());
    }

    public void addFederatedUserAccountAssociation(String userId,
                                                   FederatedAssociationRequestDTO federatedAssociationDTO) {

        try {
            UserAssociationServiceHolder.getFederatedAssociationManager()
                    .createFederatedAssociation(getUserFromUserId(userId), federatedAssociationDTO.getIdp(),
                            federatedAssociationDTO.getFederatedUserId());
        } catch (FederatedAssociationManagerException e) {
            throw handleFederatedAssociationManagerException(e, "Error while adding federated user association: "
                    + userId);
        }
    }

    public void addFederatedUserAccountAssociation(String userId, String idp, String federatedUserId) {

        try {
            UserAssociationServiceHolder.getFederatedAssociationManager()
                    .createFederatedAssociation(getUserFromUserId(userId), idp, federatedUserId);
        } catch (FederatedAssociationManagerException e) {
            throw handleFederatedAssociationManagerException(e, "Error while adding federated user association: "
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
        userDTO.setUserId(getUniqueUserId(userAccountAssociationDTO));
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
        federatedAssociationDTO.setFederatedUserId(federatedAssociation.getFederatedUserId());

        IdpDTO idpDTO = new IdpDTO();
        idpDTO.setId(federatedAssociation.getIdp().getId());
        idpDTO.setName(federatedAssociation.getIdp().getName());
        if (federatedAssociation.getIdp().getDisplayName() == null) {
            idpDTO.setDisplayName(StringUtils.EMPTY);
        } else {
            idpDTO.setDisplayName(federatedAssociation.getIdp().getDisplayName());
        }
        if (federatedAssociation.getIdp().getImageUrl() == null) {
            idpDTO.setImageUrl(StringUtils.EMPTY);
        } else {
            idpDTO.setImageUrl(federatedAssociation.getIdp().getImageUrl());
        }

        federatedAssociationDTO.setIdp(idpDTO);
        return federatedAssociationDTO;
    }

    private APIError handleUserAccountAssociationException(UserAccountAssociationException e, String message) {

        Response.Status status;
        ErrorResponse errorResponse;

        if (e instanceof UserAccountAssociationClientException) {
            errorResponse = new ErrorResponse.Builder()
                    .withCode(e.getErrorCode())
                    .withMessage(message)
                    .build(log, e.getMessage());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode : ASSOCIATION_ERROR_PREFIX
                        + errorCode;
                errorResponse.setCode(errorCode);
            }
            handleErrorDescription(e, errorResponse);
            status = Response.Status.BAD_REQUEST;
        } else {
            errorResponse = new ErrorResponse.Builder()
                    .withCode(e.getErrorCode())
                    .withMessage(message)
                    .build(log, e, e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    private APIError handleFederatedAssociationManagerException(FederatedAssociationManagerException e,
                                                                String message) {
        ErrorResponse errorResponse;
        Response.Status status;
        if (e instanceof FederatedAssociationManagerClientException) {
            errorResponse = new ErrorResponse.Builder()
                    .withCode(e.getErrorCode())
                    .withMessage(message)
                    .build();
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode : ASSOCIATION_ERROR_PREFIX
                        + errorCode;
                errorResponse.setCode(errorCode);
            }
            handleErrorDescription(e, errorResponse);
            status = Response.Status.BAD_REQUEST;
        } else {
            errorResponse = new ErrorResponse.Builder()
                    .withCode(e.getErrorCode())
                    .withMessage(message)
                    .build(log, e, e.getMessage());
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

    private String getUniqueUserId(UserAccountAssociationDTO userAccountAssociationDTO) {

        User user = new User();
        user.setUserName(userAccountAssociationDTO.getUsername());
        user.setUserStoreDomain(userAccountAssociationDTO.getDomain());
        user.setTenantDomain(userAccountAssociationDTO.getTenantDomain());
        return new UserToUniqueId().apply(realmService, user);
    }

    private User getUserFromUserId(String userId) {

        User user = new UniqueIdToUser().apply(UserAssociationServiceHolder.getRealmService(), userId,
                IdentityTenantUtil.resolveTenantDomain());
        return getUser(user.toFullQualifiedUsername());
    }

    private User getUser(String userId) {

        User user = new User();
        user.setTenantDomain(MultitenantUtils.getTenantDomain(userId));
        user.setUserStoreDomain(UserCoreUtil.extractDomainFromName(userId));
        user.setUserName(MultitenantUtils.getTenantAwareUsername(UserCoreUtil.removeDomainFromName(userId)));
        return user;
    }
}
