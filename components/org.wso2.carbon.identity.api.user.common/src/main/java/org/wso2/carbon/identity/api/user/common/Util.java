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

package org.wso2.carbon.identity.api.user.common;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.authentication.framework.exception.UserSessionException;
import org.wso2.carbon.identity.application.authentication.framework.store.UserSessionStore;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.UniqueIDUserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.UUID;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.CORRELATION_ID_MDC;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessage.ERROR_CODE_INVALID_USERNAME;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessage.ERROR_CODE_SERVER_ERROR;

/**
 * Common util class
 */
public class Util {

    private static final Log log = LogFactory.getLog(Util.class);

    /**
     * Get correlation id of current thread
     *
     * @return correlation-id
     */
    public static String getCorrelation() {
        String ref;
        if (isCorrelationIDPresent()) {
            ref = MDC.get(CORRELATION_ID_MDC).toString();
        } else {
            ref = UUID.randomUUID().toString();

        }
        return ref;
    }

    /**
     * Check whether correlation id present in the log MDC
     *
     * @return
     */
    public static boolean isCorrelationIDPresent() {
        return MDC.get(CORRELATION_ID_MDC) != null;
    }

    /**
     * Resolve the user id of the given user object.
     *
     * @param user user object
     * @return user-id
     */
    public static String resolveUserIdFromUser(User user) {
        int tenantId = (user.getTenantDomain() == null) ? org.wso2.carbon.utils.multitenancy.MultitenantConstants
                .INVALID_TENANT_ID : IdentityTenantUtil.getTenantId(user.getTenantDomain());
        try {
            return UserSessionStore.getInstance().getUserId(user.getUserName(), tenantId, user
                    .getUserStoreDomain(), -1);
        } catch (UserSessionException e) {
            throw new APIError(Response.Status.BAD_REQUEST, new ErrorResponse.Builder()
                    .withCode(ERROR_CODE_INVALID_USERNAME.getCode())
                    .withMessage(ERROR_CODE_INVALID_USERNAME.getMessage())
                    .withDescription(ERROR_CODE_INVALID_USERNAME.getDescription())
                    .build(log, e, "Invalid userId: " + user.getUserName()));
        }
    }

    /**
     * Validate whether the given id is valid user id in the user store or in the session management data stores.
     *
     * @param realmService  realm service
     * @param userId    unique user id of the user
     * @param tenantDomain  tenant domain of the user
     */
    public static void validateUserId(RealmService realmService, String userId, String tenantDomain) {

        if (StringUtils.isEmpty(userId)) {
            throw new WebApplicationException("UserID is empty.");
        }
        boolean isUserValid;

        try {
            isUserValid = UserSessionStore.getInstance().isExistingUser(userId);
            if (!isUserValid) {
                isUserValid = validateUserIdInUserstore(realmService, tenantDomain, userId);
            }
        } catch (UserSessionException | UserStoreException e) {
           throw new APIError(Response.Status.INTERNAL_SERVER_ERROR, new ErrorResponse.Builder()
                    .withCode(ERROR_CODE_SERVER_ERROR.getCode())
                    .withMessage(ERROR_CODE_SERVER_ERROR.getMessage())
                    .withDescription(ERROR_CODE_SERVER_ERROR.getDescription())
                    .build(log, e, "Error occurred when retrieving user from userId: " + userId));
        }
        if (!isUserValid) {
            throw new APIError(Response.Status.NOT_FOUND, new ErrorResponse.Builder()
                    .withCode(ERROR_CODE_INVALID_USERNAME.getCode())
                    .withMessage(ERROR_CODE_INVALID_USERNAME.getMessage())
                    .withDescription(ERROR_CODE_INVALID_USERNAME.getDescription())
                    .build());
        }
    }

    private static boolean validateUserIdInUserstore(RealmService realmService, String tenantDomain, String userId)
            throws UserStoreException {

        UserStoreManager userStoreManager = realmService.getTenantUserRealm(
                IdentityTenantUtil.getTenantId(tenantDomain)).getUserStoreManager();
        if (!(userStoreManager instanceof UniqueIDUserStoreManager)) {
            if (log.isDebugEnabled()) {
                log.debug("Provided user store manager does not support unique user IDs. Therefore the user id: "
                        + userId + " cannot be validated.");
            }
            return false;
        }
        return ((UniqueIDUserStoreManager) userStoreManager).isExistingUserWithID(userId);
    }
}
