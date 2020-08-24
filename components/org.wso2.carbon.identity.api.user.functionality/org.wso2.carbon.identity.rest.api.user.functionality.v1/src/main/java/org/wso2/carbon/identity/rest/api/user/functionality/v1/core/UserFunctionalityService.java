/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.rest.api.user.functionality.v1.core;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.user.common.function.UserToUniqueId;
import org.wso2.carbon.identity.api.user.functionality.common.Constants;
import org.wso2.carbon.identity.api.user.functionality.common.UserFunctionalityServiceHolder;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.rest.api.user.functionality.v1.model.LockStatusResponse;
import org.wso2.carbon.identity.rest.api.user.functionality.v1.model.StatusChangeRequest;
import org.wso2.carbon.identity.rest.api.user.functionality.v1.model.UserStatusChangeRequest;
import org.wso2.carbon.identity.user.functionality.mgt.UserFunctionalityManager;
import org.wso2.carbon.identity.user.functionality.mgt.UserFunctionalityMgtConstants;
import org.wso2.carbon.identity.user.functionality.mgt.exception.UserFunctionalityManagementClientException;
import org.wso2.carbon.identity.user.functionality.mgt.exception.UserFunctionalityManagementException;
import org.wso2.carbon.identity.user.functionality.mgt.model.FunctionalityLockStatus;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.ContextLoader.getUserFromContext;

/**
 * This class performs the operations related to user functionality management by invoking
 * {@link org.wso2.carbon.identity.user.functionality.mgt.UserFunctionalityManager}
 * service.
 */
public class UserFunctionalityService {

    /**
     * Lock or Unlock a functionality of a user by a privileged user.
     *
     * @param userId                  User Id
     * @param functionalityIdentifier FunctionalityIdentifier
     * @param statusChangeRequest     StatusChangeRequest
     * @return Response
     */
    public Response changeStatus(String userId, String functionalityIdentifier,
                                 StatusChangeRequest statusChangeRequest) {

        if (statusChangeRequest.getAction() == StatusChangeRequest.ActionEnum.LOCK) {
            return lock(userId, functionalityIdentifier, statusChangeRequest);
        } else {
            if (statusChangeRequest.getFunctionalityLockReason() != null ||
                    statusChangeRequest.getFunctionalityLockReasonCode() != null
                    || statusChangeRequest.getTimeToLock() != null) {
                throw handleError(Response.Status.UNSUPPORTED_MEDIA_TYPE,
                        Constants.ErrorMessages.ERROR_CODE_UNSUPPORTED_PARAMETERS_FOR_UNLOCK);
            } else {
                return unlock(userId, functionalityIdentifier);
            }
        }
    }

    /**
     * Lock a functionality of a user by a privileged user.
     *
     * @param userId                  User Id
     * @param functionalityIdentifier FunctionalityIdentifier
     * @param statusChangeRequest     StatusChangeRequest
     * @return Response
     */
    public Response lock(String userId, String functionalityIdentifier, StatusChangeRequest statusChangeRequest) {

        try {
            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
            long unlockTime = 0;
            if (statusChangeRequest.getTimeToLock() != null) {
                unlockTime = Long.parseLong(statusChangeRequest.getTimeToLock());
            }
            UserFunctionalityManager userFunctionalityManager =
                    UserFunctionalityServiceHolder.getuserFunctionalityManager();
            userFunctionalityManager.lock(userId,
                    tenantId, functionalityIdentifier, unlockTime,
                    statusChangeRequest.getFunctionalityLockReasonCode(),
                    statusChangeRequest.getFunctionalityLockReason());
            return Response.ok().build();
        } catch (UserFunctionalityManagementException e) {
            if (e instanceof UserFunctionalityManagementClientException) {
                throw handleUserFunctionalityMgtClientException(e);
            }
            throw handleError(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessages.ERROR_CODE_LOCK_THE_FUNCTIONALITY_FAILED);

        }
    }

    /**
     * Unlock a functionality of a user by a privileged user.
     *
     * @param userId                  User Id
     * @param functionalityIdentifier FunctionalityIdentifier
     * @return Response
     */
    public Response unlock(String userId, String functionalityIdentifier) {

        try {
            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
            UserFunctionalityManager userFunctionalityManager =
                    UserFunctionalityServiceHolder.getuserFunctionalityManager();
            userFunctionalityManager.unlock(userId,
                    tenantId, functionalityIdentifier);
            userFunctionalityManager.deleteAllPropertiesForUser(userId, tenantId, functionalityIdentifier);

            return Response.ok().build();
        } catch (UserFunctionalityManagementException e) {
            if (e instanceof UserFunctionalityManagementClientException) {
                throw handleUserFunctionalityMgtClientException(e);
            }
            throw handleError(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessages.ERROR_CODE_UNLOCK_THE_FUNCTIONALITY_FAILED);
        }
    }

    /**
     * Get LockStatus of a functionality of a user by a privileged user.
     *
     * @param userId                  User Id
     * @param functionalityIdentifier FunctionalityIdentifier
     * @return LockStatusResponse
     */
    public LockStatusResponse getLockStatus(String userId, String functionalityIdentifier) {

        try {
            LockStatusResponse lockStatusResponse;
            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
            FunctionalityLockStatus functionalityLockStatus =
                    UserFunctionalityServiceHolder.getuserFunctionalityManager().getLockStatus(userId,
                            tenantId, functionalityIdentifier);
            lockStatusResponse = new FunctionalityLockStatusToExternal().apply(functionalityLockStatus);
            return lockStatusResponse;
        } catch (UserFunctionalityManagementException e) {
            if (e instanceof UserFunctionalityManagementClientException) {
                throw handleUserFunctionalityMgtClientException(e);
            }
            throw handleError(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessages.ERROR_CODE_GET_LOCK_STATUS_FAILED);
        }
    }

    /**
     * Lock or Unlock a functionality by a user.
     *
     * @param functionalityIdentifier FunctionalityIdentifier
     * @param userStatusChangeRequest UserStatusChangeRequest
     * @return Response
     */
    public Response changeStatusOfLoggedInUser(String functionalityIdentifier,
                                               UserStatusChangeRequest userStatusChangeRequest) {

        String userId = getUserIdFromUser(getUserFromContext());
        if (userStatusChangeRequest.getAction() == UserStatusChangeRequest.ActionEnum.LOCK) {
            return lockLoggedInUser(userId, functionalityIdentifier,
                    userStatusChangeRequest);
        } else {
            if (userStatusChangeRequest.getTimeToLock() != null) {
                throw handleError(Response.Status.UNSUPPORTED_MEDIA_TYPE,
                        Constants.ErrorMessages.ERROR_CODE_UNSUPPORTED_PARAMETERS_FOR_UNLOCK);
            }
            return unlockLoggedInUser(userId, functionalityIdentifier);
        }
    }

    /**
     * Unlock a functionality by a user.
     *
     * @param functionalityIdentifier FunctionalityIdentifier
     * @return Response
     */
    public Response unlockLoggedInUser(String userId, String functionalityIdentifier) {

        try {
            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
            UserFunctionalityManager userFunctionalityManager =
                    UserFunctionalityServiceHolder.getuserFunctionalityManager();

            FunctionalityLockStatus lockStatus = userFunctionalityManager.getLockStatus(userId, tenantId,
                    functionalityIdentifier);
            if (StringUtils.equals(lockStatus.getLockReasonCode(),
                    UserFunctionalityMgtConstants.FunctionalityLockReasons.USER_MANUALLY_LOCKED
                            .getFunctionalityLockCode())) {
                userFunctionalityManager.unlock(userId,
                        tenantId, functionalityIdentifier);
                return Response.ok().build();
            } else {
                throw handleError(Response.Status.FORBIDDEN,
                        Constants.ErrorMessages.ERROR_CODE_USER_NOT_PERMITTED_TO_UNLOCK);
            }
        } catch (UserFunctionalityManagementException e) {
            if (e instanceof UserFunctionalityManagementClientException) {
                throw handleUserFunctionalityMgtClientException(e);
            }
            throw handleError(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessages.ERROR_CODE_LOCK_THE_FUNCTIONALITY_FAILED);

        }
    }

    /**
     * Lock a functionality by a user.
     *
     * @param functionalityIdentifier FunctionalityIdentifier
     * @param userStatusChangeRequest UserStatusChangeRequest
     * @return Response
     */
    public Response lockLoggedInUser(String userId, String functionalityIdentifier,
                                     UserStatusChangeRequest userStatusChangeRequest) {

        try {
            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
            long unlockTime = 0;
            if (userStatusChangeRequest.getTimeToLock() != null) {
                unlockTime = Long.parseLong(userStatusChangeRequest.getTimeToLock());
            }
            UserFunctionalityManager userFunctionalityManager =
                    UserFunctionalityServiceHolder.getuserFunctionalityManager();
            FunctionalityLockStatus status = userFunctionalityManager.getLockStatus(userId, tenantId,
                    functionalityIdentifier);
            if (status.getLockStatus() && !StringUtils.equals(status.getLockReasonCode(),
                    UserFunctionalityMgtConstants.FunctionalityLockReasons.USER_MANUALLY_LOCKED
                            .getFunctionalityLockCode())) {
                throw handleError(Response.Status.FORBIDDEN,
                        Constants.ErrorMessages.ERROR_CODE_FUNCTIONALITY_ALREADY_LOCKED);
            }
            userFunctionalityManager.lock(userId,
                    tenantId, functionalityIdentifier, unlockTime,
                    UserFunctionalityMgtConstants.FunctionalityLockReasons.USER_MANUALLY_LOCKED
                            .getFunctionalityLockCode(),
                    UserFunctionalityMgtConstants.FunctionalityLockReasons.USER_MANUALLY_LOCKED
                            .getFunctionalityLockReason());
            return Response.ok().build();
        } catch (UserFunctionalityManagementException e) {
            if (e instanceof UserFunctionalityManagementClientException) {
                throw handleUserFunctionalityMgtClientException(e);
            }
            throw handleError(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessages.ERROR_CODE_LOCK_THE_FUNCTIONALITY_FAILED);

        }
    }

    /**
     * Get LockStatus of a functionality by a user.
     *
     * @param functionalityIdentifier FunctionalityIdentifier
     * @return LockStatusResponse
     */
    public LockStatusResponse getLockStatusOfLoggedInUser(String functionalityIdentifier) {

        try {
            String userId = getUserIdFromUser(getUserFromContext());
            LockStatusResponse lockStatusResponse;
            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
            UserFunctionalityManager userFunctionalityManager =
                    UserFunctionalityServiceHolder.getuserFunctionalityManager();
            FunctionalityLockStatus functionalityLockStatus = userFunctionalityManager
                    .getLockStatus(userId, tenantId, functionalityIdentifier);
            lockStatusResponse = new FunctionalityLockStatusToExternal().apply(functionalityLockStatus);
            return lockStatusResponse;
        } catch (UserFunctionalityManagementException e) {
            if (e instanceof UserFunctionalityManagementClientException) {
                throw handleUserFunctionalityMgtClientException(e);
            }
            throw handleError(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessages.ERROR_CODE_GET_LOCK_STATUS_FAILED);

        }
    }

    private APIError handleUserFunctionalityMgtClientException(UserFunctionalityManagementException e) {

        if (StringUtils.equals(UserFunctionalityMgtConstants.ErrorMessages.USER_NOT_FOUND.getCode(),
                e.getErrorCode())) {
            return handleError(Response.Status.NOT_FOUND, Constants.ErrorMessages.ERROR_CODE_INVALID_USERID);
        }
        return handleError(Response.Status.BAD_REQUEST, Constants.ErrorMessages.ERROR_CODE_BAD_REQUEST);
    }

    private static APIError handleError(Response.Status status, Constants.ErrorMessages error, String... data) {

        String description;
        if (data != null) {
            description = String.format(error.getDescription(), (Object[]) data);
        } else {
            description = error.getMessage();
        }
        return new APIError(status, new ErrorResponse.Builder().withCode(error.getCode())
                .withMessage(error.getMessage())
                .withDescription(description).build());
    }

    private String getUserIdFromUser(User user) {

        return new UserToUniqueId().apply(UserFunctionalityServiceHolder.getRealmService(), user);
    }
}
