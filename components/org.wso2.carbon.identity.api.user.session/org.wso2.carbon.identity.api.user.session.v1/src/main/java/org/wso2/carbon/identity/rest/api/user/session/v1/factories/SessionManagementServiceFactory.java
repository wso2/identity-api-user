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

package org.wso2.carbon.identity.rest.api.user.session.v1.factories;

import org.wso2.carbon.identity.api.user.session.common.util.SessionManagementServiceHolder;
import org.wso2.carbon.identity.application.authentication.framework.UserSessionManagementService;
import org.wso2.carbon.identity.rest.api.user.session.v1.core.SessionManagementService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Factory class for SessionManagementService.
 */
public class SessionManagementServiceFactory {

    private static final SessionManagementService SERVICE;

    static {
        UserSessionManagementService userSessionManagementService =
                SessionManagementServiceHolder.getUserSessionManagementService();
        RealmService realmService = SessionManagementServiceHolder.getRealmService();

        if (userSessionManagementService == null) {
            throw new IllegalStateException("UserSessionManagementService is not available in the OSGi context.");
        } else if (realmService == null) {
            throw new IllegalStateException("RealmService is not available in the OSGi context.");
        }
        SERVICE = new SessionManagementService(userSessionManagementService, realmService);
    }

    /**
     * Method to get the session management service.
     *
     * @return SessionManagementService.
     */
    public static SessionManagementService getSessionManagementService() {

        return SERVICE;
    }
}
