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
import org.wso2.carbon.user.core.service.RealmService;

import java.util.function.BiFunction;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessage.ERROR_CODE_INVALID_USERNAME;
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
                throw new WebApplicationException("UserID is empty.");
            } else if (StringUtils.isEmpty(tenantDomain)) {
                throw new WebApplicationException("Tenant domain is empty.");
            }

            UniqueIDUserStoreManager uniqueIdEnabledUserStoreManager = getUniqueIdEnabledUserStoreManager(realmService,
                    tenantDomain);
            org.wso2.carbon.user.core.common.User user = getUserByUniqueId(userId, uniqueIdEnabledUserStoreManager);
            return getUser(user);
        } catch (Exception e) {
            throw new APIError(Response.Status.BAD_REQUEST, new ErrorResponse.Builder()
                    .withCode(ERROR_CODE_INVALID_USERNAME.getCode())
                    .withMessage(ERROR_CODE_INVALID_USERNAME.getMessage())
                    .withDescription(ERROR_CODE_INVALID_USERNAME.getDescription())
                    .build(log, e, "Invalid userId: " + userId));
        }
    }

    private org.wso2.carbon.user.core.common.User getUserByUniqueId(String userId, UniqueIDUserStoreManager
            uniqueIdEnabledUserStoreManager) {

        org.wso2.carbon.user.core.common.User user = null;
        try {
            user = uniqueIdEnabledUserStoreManager.getUserWithID(userId, null, null);
        } catch (UserStoreException e) {
            log.error("Unable to retrieve user for the given user id: " + userId, e);
        }
        if (user == null) {
            throw new WebApplicationException("Could not find a valid user for the UserId: " + userId + ". " +
                    "Provided Id is either empty or invalid.");
        }
        return user;
    }

    private UniqueIDUserStoreManager getUniqueIdEnabledUserStoreManager(RealmService realmService, String tenantDomain)
            throws org.wso2.carbon.user.api.UserStoreException {

        UserStoreManager userStoreManager = realmService.getTenantUserRealm(
                IdentityTenantUtil.getTenantId(tenantDomain)).getUserStoreManager();
        if (!(userStoreManager instanceof UniqueIDUserStoreManager)) {
            throw new WebApplicationException("Provided user store manager does not support unique user IDs.");
        }
        return (UniqueIDUserStoreManager) userStoreManager;
    }
}
