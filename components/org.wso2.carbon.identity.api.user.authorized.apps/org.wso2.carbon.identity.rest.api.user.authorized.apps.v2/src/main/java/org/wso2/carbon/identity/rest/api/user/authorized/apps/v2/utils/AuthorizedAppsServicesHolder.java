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

package org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.utils;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.oauth.OAuthAdminServiceImpl;
import org.wso2.carbon.identity.oauth2.OAuth2ScopeService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * This class holds the service instances required for authorized apps management.
 */
public class AuthorizedAppsServicesHolder {

    private AuthorizedAppsServicesHolder() {

    }

    private static class ApplicationManagementServiceHolder {

        static final ApplicationManagementService SERVICE = (ApplicationManagementService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(ApplicationManagementService.class, null);
    }

    private static class OAuthAdminServiceImplServiceHolder {

        static final OAuthAdminServiceImpl SERVICE = (OAuthAdminServiceImpl) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(OAuthAdminServiceImpl.class, null);
    }

    private static class OAuth2ScopeServiceHolder {

        static final OAuth2ScopeService SERVICE = (OAuth2ScopeService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(OAuth2ScopeService.class, null);
    }

    private static class RealmServiceHolder {

        static final RealmService SERVICE;

        static {
            SERVICE = (RealmService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                    .getOSGiService(RealmService.class, null);

            if (SERVICE == null) {
                throw new IllegalStateException("RealmService is not available from OSGi context.");
            }
        }
    }

    /**
     * Get OAuth2ScopeService
     *
     * @return OAuth2ScopeService
     */
    public static ApplicationManagementService getApplicationManagementService() {

        return ApplicationManagementServiceHolder.SERVICE;
    }

    /**
     * Get OAuthAdminService
     *
     * @return OAuthAdminServiceImpl
     */
    public static OAuthAdminServiceImpl getOAuthAdminService() {

        return OAuthAdminServiceImplServiceHolder.SERVICE;
    }

    /**
     * Get OAuth2ScopeService
     *
     * @return OAuth2ScopeService
     */
    public static OAuth2ScopeService getOAuth2ScopeService() {

        return OAuth2ScopeServiceHolder.SERVICE;
    }

    /**
     * Get RealmService
     *
     * @return RealmService
     */
    public static RealmService getRealmService() {

        return RealmServiceHolder.SERVICE;
    }
}
