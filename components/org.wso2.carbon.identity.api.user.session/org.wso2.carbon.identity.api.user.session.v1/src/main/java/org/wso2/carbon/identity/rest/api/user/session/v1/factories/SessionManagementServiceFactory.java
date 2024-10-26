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

package org.wso2.carbon.identity.rest.api.user.session.v1.factories;

import org.wso2.carbon.identity.api.user.session.common.util.SessionManagementServiceHolder;
import org.wso2.carbon.identity.application.authentication.framework.UserSessionManagementService;
import org.wso2.carbon.identity.rest.api.user.session.v1.core.SessionManagementService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Factory class for SessionManagementService.
 */
public class SessionManagementServiceFactory {

    private SessionManagementServiceFactory() {

    }

    private static class SessionManagementServicesHolder {

        private static final SessionManagementService SERVICE = createServiceInstance();
    }

    private static SessionManagementService createServiceInstance() {

        UserSessionManagementService userSessionManagementService = getUserSessionManagementService();
        RealmService realmService = getRealmService();

        return new SessionManagementService(userSessionManagementService, realmService);
    }

    /**
     * Method to get SessionManagementService instance.
     *
     * @return SessionManagementService
     */
    public static SessionManagementService getSessionManagementService() {

        return SessionManagementServicesHolder.SERVICE;
    }

    private static UserSessionManagementService getUserSessionManagementService() {

        UserSessionManagementService service = SessionManagementServiceHolder.getUserSessionManagementService();
        if (service == null) {
            throw new IllegalStateException("UserSessionManagementService is not available from OSGi context.");
        }

        return service;
    }

    private static RealmService getRealmService() {

        RealmService service = SessionManagementServiceHolder.getRealmService();
        if (service == null) {
            throw new IllegalStateException("RealmService is not available from OSGi context.");
        }

        return service;
    }
}
