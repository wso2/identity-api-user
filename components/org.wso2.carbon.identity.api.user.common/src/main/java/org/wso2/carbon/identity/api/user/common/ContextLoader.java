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

import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.user.core.UserStoreConfigConstants;

import java.net.URI;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessage.ERROR_CODE_INVALID_USERNAME;
import static org.wso2.carbon.identity.api.user.common.Constants.TENANT_CONTEXT_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.user.common.Constants.TENANT_NAME_FROM_CONTEXT;
import static org.wso2.carbon.identity.api.user.common.Constants.USER_API_PATH_COMPONENT;

/**
 * Load information from context
 */
public class ContextLoader {

    /**
     * Retrieves loaded tenant domain from carbon context.
     * @return tenant domain of the request is being served.
     */
    public static String getTenantDomainFromContext() {

        String tenantDomain = MultitenantConstants.SUPER_TENANT_DOMAIN_NAME;
        if (IdentityUtil.threadLocalProperties.get().get(TENANT_NAME_FROM_CONTEXT) != null) {
            tenantDomain = (String) IdentityUtil.threadLocalProperties.get().get(TENANT_NAME_FROM_CONTEXT);
        }
        return tenantDomain;
    }

    /**
     * Retrieves authenticated username from carbon context.
     * @return username of the authenticated user.
     */
    public static String getUsernameFromContext() {

        return PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
    }

    /**
     * Retrieves authenticated username from carbon context.
     * @return username of the authenticated user.
     */
    public static User getUserFromContext() {

        return getUser(getTenantDomainFromContext(), getUsernameFromContext());
    }


    /**
     * Build user object from tenant domain and username
     * @param tenantDomain
     * @param decodedUsername
     * @return
     */
    public static User getUser(String tenantDomain, String decodedUsername) {
        String realm = UserStoreConfigConstants.PRIMARY;
        String username;
        String[] strComponent = decodedUsername.split("/");

        if (strComponent.length == 1) {
            username = strComponent[0];
        } else if (strComponent.length == 2) {
            realm = strComponent[0];
            username = strComponent[1];
        } else {
            throw new APIError(Response.Status.BAD_REQUEST, new ErrorResponse.Builder().withDescription("Provided " +
                    "Username is not in the correct format.")
                    .withCode(ERROR_CODE_INVALID_USERNAME.getCode())
                    .withMessage(ERROR_CODE_INVALID_USERNAME.getMessage()).build());
        }

        User user = new User();
        user.setUserName(username);
        user.setUserStoreDomain(realm);
        user.setTenantDomain(tenantDomain);
        return user;
    }

    /**
     * Build URI prepending the user API context with to the endpoint
     * https://<hostname>:<port>/t/<tenant-domain>/api/users/<endpoint>
     * @param endpoint relative endpoint path
     * @return
     */
    public static URI buildURI(String endpoint) {

        String tenantQualifiedRelativePath =
                String.format(TENANT_CONTEXT_PATH_COMPONENT, getTenantDomainFromContext()) + USER_API_PATH_COMPONENT;
        String url = IdentityUtil.getServerURL(tenantQualifiedRelativePath + endpoint, true, true);
        return URI.create(url);
    }
}
