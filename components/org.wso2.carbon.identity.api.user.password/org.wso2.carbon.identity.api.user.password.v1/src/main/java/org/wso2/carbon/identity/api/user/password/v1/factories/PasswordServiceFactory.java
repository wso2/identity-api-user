/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.user.password.v1.factories;

import org.wso2.carbon.identity.api.user.password.common.PasswordServiceHolder;
import org.wso2.carbon.identity.api.user.password.v1.core.PasswordService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Factory class for PasswordService.
 */
public class PasswordServiceFactory {

    private PasswordServiceFactory() {

    }

    private static class PasswordServiceInstanceHolder {

        private static final PasswordService SERVICE = createServiceInstance();
    }

    private static PasswordService createServiceInstance() {

        RealmService realmService = getRealmService();
        return new PasswordService(realmService);
    }

    /**
     * Get PasswordService instance.
     *
     * @return PasswordService.
     */
    public static PasswordService getPasswordService() {

        return PasswordServiceInstanceHolder.SERVICE;
    }

    private static RealmService getRealmService() {

        RealmService service = PasswordServiceHolder.getRealmService();
        if (service == null) {
            throw new IllegalStateException("RealmService is not available from OSGi context.");
        }
        return service;
    }
}
