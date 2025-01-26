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

package org.wso2.carbon.identity.rest.api.user.idv.v1.factories;

import org.wso2.carbon.extension.identity.verification.mgt.IdentityVerificationManager;
import org.wso2.carbon.identity.api.user.idv.common.IdentityVerificationServiceHolder;
import org.wso2.carbon.identity.rest.api.user.idv.v1.core.IdentityVerificationService;

/**
 * Factory class for IdentityVerificationService.
 */
public class IdentityVerificationServiceFactory {

    private IdentityVerificationServiceFactory() {

    }

    private static class IdentityVerificationServicesHolder {

        private static final IdentityVerificationService SERVICE = createServiceInstance();
    }

    private static IdentityVerificationService createServiceInstance() {

        IdentityVerificationManager identityVerificationManager = getIdentityVerificationManager();
        return new IdentityVerificationService(identityVerificationManager);
    }

    /**
     * Get IdentityVerificationService.
     *
     * @return IdentityVerificationService.
     */
    public static IdentityVerificationService getIdentityVerificationService() {

        return IdentityVerificationServicesHolder.SERVICE;
    }

    private static IdentityVerificationManager getIdentityVerificationManager() {

        IdentityVerificationManager service = IdentityVerificationServiceHolder.getIdentityVerificationManager();
        if (service == null) {
            throw new IllegalStateException("IdentityVerificationManager is not available from OSGi context.");
        }
        return service;
    }
}
