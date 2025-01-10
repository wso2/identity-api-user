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

package org.wso2.carbon.identity.rest.api.user.mfa.v1.factories;

import org.wso2.carbon.identity.rest.api.user.mfa.v1.core.MFAService;
import org.wso2.carbon.identity.rest.api.user.mfa.v1.util.UserMFAServiceHolder;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Factory class for MFAService.
 */
public class MFAServiceFactory {

    private MFAServiceFactory() {

    }

    private static class MFAServicesHolder {

        private static final MFAService SERVICE = createServiceInstance();
    }

    private static MFAService createServiceInstance() {

        RealmService realmService = getRealmService();

        return new MFAService(realmService);
    }

    /**
     * Get MFAService service.
     *
     * @return MFAService.
     */
    public static MFAService getMFAService() {

        return MFAServicesHolder.SERVICE;
    }

    private static RealmService getRealmService() {

        RealmService service = UserMFAServiceHolder.getRealmService();
        if (service == null) {
            throw new IllegalStateException("RealmService is not available from OSGi context.");
        }

        return service;
    }
}
