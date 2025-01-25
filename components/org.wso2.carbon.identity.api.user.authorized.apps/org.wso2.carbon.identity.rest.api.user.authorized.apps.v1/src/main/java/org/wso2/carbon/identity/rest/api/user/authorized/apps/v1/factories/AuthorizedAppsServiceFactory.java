/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.factories;

import org.wso2.carbon.identity.oauth.OAuthAdminServiceImpl;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.core.AuthorizedAppsService;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.utils.AuthorizedAppsServicesHolder;

/**
 * This class is responsible for initializing AuthorizedAppsService.
 */
public class AuthorizedAppsServiceFactory {

    private AuthorizedAppsServiceFactory() {

    }

    private static class AuthorizedAppsServiceHolder {

        private static final AuthorizedAppsService SERVICE = createServiceInstance();
    }

    private static AuthorizedAppsService createServiceInstance() {

        OAuthAdminServiceImpl oAuthAdminServiceImpl = getOAuthAdminService();
        return new AuthorizedAppsService(oAuthAdminServiceImpl);
    }

    /**
     * Get AuthorizedAppsService.
     *
     * @return AuthorizedAppsService.
     */
    public static AuthorizedAppsService getAuthorizedAppsService() {

        return AuthorizedAppsServiceHolder.SERVICE;
    }

    private static OAuthAdminServiceImpl getOAuthAdminService() {

        OAuthAdminServiceImpl service = AuthorizedAppsServicesHolder.getOAuthAdminService();
        if (service == null) {
            throw new IllegalStateException("OAuthAdminServiceImpl is not available from OSGi context.");
        }
        return service;
    }
}
