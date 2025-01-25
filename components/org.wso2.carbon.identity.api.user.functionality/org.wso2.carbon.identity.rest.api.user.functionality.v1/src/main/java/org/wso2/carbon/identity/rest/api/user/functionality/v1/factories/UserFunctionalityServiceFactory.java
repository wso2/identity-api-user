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

package org.wso2.carbon.identity.rest.api.user.functionality.v1.factories;

import org.wso2.carbon.identity.api.user.functionality.common.UserFunctionalityServiceHolder;
import org.wso2.carbon.identity.rest.api.user.functionality.v1.core.UserFunctionalityService;
import org.wso2.carbon.identity.user.functionality.mgt.UserFunctionalityManager;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Factory class for UserFunctionalityService.
 */
public class UserFunctionalityServiceFactory {

    private UserFunctionalityServiceFactory() {

    }

    private static class UserFunctionalityServicesHolder {

        private static final UserFunctionalityService SERVICE = createServiceInstance();
    }

    private static UserFunctionalityService createServiceInstance() {

        UserFunctionalityManager userFunctionalityManager = getUserFunctionalityManager();
        RealmService realmService = getRealmService();
        return new UserFunctionalityService(userFunctionalityManager, realmService);
    }

    /**
     * Get UserFunctionalityService service.
     *
     * @return UserFunctionalityService.
     */
    public static UserFunctionalityService getUserFunctionalityService() {

        return UserFunctionalityServicesHolder.SERVICE;
    }

    private static UserFunctionalityManager getUserFunctionalityManager() {

        UserFunctionalityManager service = UserFunctionalityServiceHolder.getuserFunctionalityManager();
        if (service == null) {
            throw new IllegalStateException("UserFunctionalityManager is not available from OSGi context.");
        }
        return service;
    }

    private static RealmService getRealmService() {

        RealmService service = UserFunctionalityServiceHolder.getRealmService();
        if (service == null) {
            throw new IllegalStateException("RealmService is not available from OSGi context.");
        }
        return service;
    }
}
