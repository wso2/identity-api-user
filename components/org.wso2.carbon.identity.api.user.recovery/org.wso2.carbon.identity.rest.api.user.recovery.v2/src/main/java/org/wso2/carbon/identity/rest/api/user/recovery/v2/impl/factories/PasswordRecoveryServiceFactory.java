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

package org.wso2.carbon.identity.rest.api.user.recovery.v2.impl.factories;

import org.wso2.carbon.identity.api.user.recovery.commons.UserAccountRecoveryServiceDataHolder;
import org.wso2.carbon.identity.recovery.services.password.PasswordRecoveryManager;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.impl.core.PasswordRecoveryService;

/**
 * Factory class for PasswordRecoveryService.
 */
public class PasswordRecoveryServiceFactory {

    private PasswordRecoveryServiceFactory() {

    }

    private static class PasswordRecoveryServiceHolder {

        private static final PasswordRecoveryService SERVICE = createServiceInstance();
    }

    private static PasswordRecoveryService createServiceInstance() {

        PasswordRecoveryManager passwordRecoveryManager = getPasswordRecoveryManagerService();
        return new PasswordRecoveryService(passwordRecoveryManager);
    }

    /**
     * Get AuthorizedAppsService.
     *
     * @return AuthorizedAppsService.
     */
    public static PasswordRecoveryService getPasswordRecoveryService() {

        return PasswordRecoveryServiceHolder.SERVICE;
    }

    private static PasswordRecoveryManager getPasswordRecoveryManagerService() {

        PasswordRecoveryManager service = UserAccountRecoveryServiceDataHolder.getPasswordRecoveryManager();
        if (service == null) {
            throw new IllegalStateException("PasswordRecoveryManager is not available from OSGi context.");
        }
        return service;
    }
}
