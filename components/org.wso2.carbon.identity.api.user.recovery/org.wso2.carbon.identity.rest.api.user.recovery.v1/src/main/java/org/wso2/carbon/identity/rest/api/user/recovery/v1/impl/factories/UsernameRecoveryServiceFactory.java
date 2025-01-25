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

package org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.factories;

import org.wso2.carbon.identity.api.user.recovery.commons.UserAccountRecoveryServiceDataHolder;
import org.wso2.carbon.identity.recovery.services.username.UsernameRecoveryManager;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.UsernameRecoveryService;

/**
 * Factory class for UsernameRecoveryService.
 */
public class UsernameRecoveryServiceFactory {

    private UsernameRecoveryServiceFactory() {

    }

    private static class UsernameRecoveryServiceHolder {

        private static final UsernameRecoveryService SERVICE = createServiceInstance();
    }

    private static UsernameRecoveryService createServiceInstance() {

        UsernameRecoveryManager passwordRecoveryManager = getUsernameRecoveryManager();
        return new UsernameRecoveryService(passwordRecoveryManager);
    }

    /**
     * Get AuthorizedAppsService.
     *
     * @return AuthorizedAppsService.
     */
    public static UsernameRecoveryService getUsernameRecoveryService() {

        return UsernameRecoveryServiceHolder.SERVICE;
    }

    private static UsernameRecoveryManager getUsernameRecoveryManager() {

        UsernameRecoveryManager service = UserAccountRecoveryServiceDataHolder.getUsernameRecoveryManager();
        if (service == null) {
            throw new IllegalStateException("UsernameRecoveryManager is not available from OSGi context.");
        }
        return service;
    }
}
