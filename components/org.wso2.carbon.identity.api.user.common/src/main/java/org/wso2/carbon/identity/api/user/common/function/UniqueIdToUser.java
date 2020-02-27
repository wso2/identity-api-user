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

package org.wso2.carbon.identity.api.user.common.function;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.UniqueIDUserStoreManager;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.constants.UserCoreErrorConstants;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.function.BiFunction;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessage.ERROR_CODE_INVALID_USERNAME;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessage.ERROR_CODE_SERVER_ERROR;
import static org.wso2.carbon.identity.api.user.common.ContextLoader.getUser;

/**
 * Build user object from the unique user id and tenant domain
 */
public class UniqueIdToUser implements BiFunction<RealmService, String[], User> {

    private static final Log log = LogFactory.getLog(UniqueIdToUser.class);

    @Override
    public User apply(RealmService realmService, String... args) {

        String userId = null;
        String tenantDomain;
        try {
            userId = args[0];
            tenantDomain = args[1];
            if (StringUtils.isEmpty(userId)) {
                if (log.isDebugEnabled()) {
                    log.debug("UserID is empty.");
                }
                throw buildUserNotFoundError();
            } else if (StringUtils.isEmpty(tenantDomain)) {
                if (log.isDebugEnabled()) {
                    log.debug("Tenant domain is empty.");
                }
                throw buildUserNotFoundError();
            }

            UniqueIDUserStoreManager uniqueIdEnabledUserStoreManager =
                    getUniqueIdEnabledUserStoreManager(realmService, tenantDomain);
            org.wso2.carbon.user.core.common.User user =
                    uniqueIdEnabledUserStoreManager.getUserWithID(userId, null, null);
            return getUser(user);
        } catch (org.wso2.carbon.user.api.UserStoreException e) {

            if (isUserNotExistingError(e)) {
                if (log.isDebugEnabled()) {
                    log.debug("Cannot retrieve user from userId: " + userId, e);
                }
                throw buildUserNotFoundError();
            }
            throw new APIError(Response.Status.INTERNAL_SERVER_ERROR, new ErrorResponse.Builder()
                    .withCode(ERROR_CODE_SERVER_ERROR.getCode())
                    .withMessage(ERROR_CODE_SERVER_ERROR.getMessage())
                    .withDescription(ERROR_CODE_SERVER_ERROR.getDescription())
                    .build(log, e, "Error occurred when retrieving user from userId: " + userId));
        }
    }

    private UniqueIDUserStoreManager getUniqueIdEnabledUserStoreManager(RealmService realmService, String tenantDomain)
            throws org.wso2.carbon.user.api.UserStoreException {

        UserStoreManager userStoreManager = realmService.getTenantUserRealm(
                IdentityTenantUtil.getTenantId(tenantDomain)).getUserStoreManager();
        if (!(userStoreManager instanceof UniqueIDUserStoreManager)) {
            if (log.isDebugEnabled()) {
                log.debug("Provided user store manager does not support unique user IDs.");
            }
            throw buildUserNotFoundError();
        }
        return (UniqueIDUserStoreManager) userStoreManager;
    }

    private boolean isUserNotExistingError(org.wso2.carbon.user.api.UserStoreException e) {

        return e instanceof UserStoreException &&
                UserCoreErrorConstants.ErrorMessages.ERROR_CODE_NON_EXISTING_USER.getCode().equals(
                        ((UserStoreException) e).getErrorCode());
    }

    private APIError buildUserNotFoundError() {

        return new APIError(Response.Status.NOT_FOUND, new ErrorResponse.Builder()
                .withCode(ERROR_CODE_INVALID_USERNAME.getCode())
                .withMessage(ERROR_CODE_INVALID_USERNAME.getMessage())
                .withDescription(ERROR_CODE_INVALID_USERNAME.getDescription())
                .build());
    }
}
