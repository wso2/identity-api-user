/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.factories;

import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.oauth.OAuthAdminServiceImpl;
import org.wso2.carbon.identity.oauth2.OAuth2ScopeService;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.core.AuthorizedAppsService;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.utils.AuthorizedAppsServicesHolder;
import org.wso2.carbon.user.core.service.RealmService;

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

        ApplicationManagementService applicationManagementService = getApplicationManagementService();
        OAuthAdminServiceImpl oAuthAdminServiceImpl = getOAuthAdminService();
        OAuth2ScopeService oAuth2ScopeService = getOAuth2ScopeService();
        RealmService realmService = AuthorizedAppsServicesHolder.getRealmService();

        return new AuthorizedAppsService(applicationManagementService, oAuthAdminServiceImpl,
                oAuth2ScopeService, realmService);
    }

    /**
     * Get AuthorizedAppsService
     *
     * @return AuthorizedAppsService
     */
    public static AuthorizedAppsService getAuthorizedAppsService() {

        return AuthorizedAppsServiceHolder.SERVICE;
    }

    private static ApplicationManagementService getApplicationManagementService() {

        ApplicationManagementService service = AuthorizedAppsServicesHolder.getApplicationManagementService();
        if (service == null) {
            throw new IllegalStateException("ApplicationManagementService is not available from OSGi context.");
        }

        return service;
    }

    private static OAuthAdminServiceImpl getOAuthAdminService() {

        OAuthAdminServiceImpl service = AuthorizedAppsServicesHolder.getOAuthAdminService();
        if (service == null) {
            throw new IllegalStateException("OAuthAdminServiceImpl is not available from OSGi context.");
        }

        return service;
    }

    private static OAuth2ScopeService getOAuth2ScopeService() {

        OAuth2ScopeService service = AuthorizedAppsServicesHolder.getOAuth2ScopeService();
        if (service == null) {
            throw new IllegalStateException("OAuth2ScopeService is not available from OSGi context.");
        }

        return service;
    }
}
