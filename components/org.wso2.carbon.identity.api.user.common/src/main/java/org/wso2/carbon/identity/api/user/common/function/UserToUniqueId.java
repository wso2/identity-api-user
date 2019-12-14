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
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.common.AbstractUserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.user.core.util.UserCoreUtil;

import java.util.function.BiFunction;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessage.ERROR_CODE_INVALID_USERNAME;

/**
 * Build user object from the unique user id and tenant domain
 */
public class UserToUniqueId implements BiFunction<RealmService, User, String> {

    private static final Log log = LogFactory.getLog(UserToUniqueId.class);
    private static final String USERNAME_CLAIM_URI = "http://wso2.org/claims/username";

    @Override
    public String apply(RealmService realmService, User user) {

        if (realmService == null) {
            throw new WebApplicationException("Realm service cannot be null");
        }
        if (user == null) {
            throw new WebApplicationException("User cannot be null.");
        }
        if (StringUtils.isEmpty(user.getTenantDomain())) {
            throw new WebApplicationException("User tenant domain cannot be empty");
        }
        String tenantDomain = user.getTenantDomain();
        try {
            AbstractUserStoreManager abstractUserStoreManager = getAbstractUserStoreManager(realmService, tenantDomain);
            return getUniqueIdForUser(user, abstractUserStoreManager);
        } catch (Exception e) {
            throw new APIError(Response.Status.BAD_REQUEST, new ErrorResponse.Builder()
                    .withCode(ERROR_CODE_INVALID_USERNAME.getCode())
                    .withMessage(ERROR_CODE_INVALID_USERNAME.getMessage())
                    .withDescription(ERROR_CODE_INVALID_USERNAME.getDescription())
                    .build(log, e, "Invalid user: " + user.toFullQualifiedUsername()));
        }
    }

    private String getUniqueIdForUser(User user, AbstractUserStoreManager abstractUserStoreManager) {

        try {
            return abstractUserStoreManager.getUserIDFromUserName(UserCoreUtil.addDomainToName(user.getUserName(),
                    user.getUserStoreDomain()));
        } catch (UserStoreException e) {
            if (log.isDebugEnabled()) {
                log.debug("Error occurred while retrieving Id for the user: " + user.toFullQualifiedUsername(), e);
            }
            throw new WebApplicationException("Unable to retrieve Id for the user: " + user.toFullQualifiedUsername());
        }
    }

    private AbstractUserStoreManager getAbstractUserStoreManager(RealmService realmService, String tenantDomain)
            throws org.wso2.carbon.user.api.UserStoreException {

        UserStoreManager userStoreManager = realmService.getTenantUserRealm(
                IdentityTenantUtil.getTenantId(tenantDomain)).getUserStoreManager();
        if (!(userStoreManager instanceof AbstractUserStoreManager)) {
            if (log.isDebugEnabled()) {
                log.debug("Provided user store manager: " + userStoreManager.getClass() + ", is not an instance of "
                        + "the AbstractUserStore manager");
            }
            throw new WebApplicationException("Unsupported user store manager.");
        }
        return (AbstractUserStoreManager) userStoreManager;
    }
}
