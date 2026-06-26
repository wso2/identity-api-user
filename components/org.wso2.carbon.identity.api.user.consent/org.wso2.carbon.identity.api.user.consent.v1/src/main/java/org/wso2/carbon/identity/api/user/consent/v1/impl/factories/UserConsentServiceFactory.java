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

package org.wso2.carbon.identity.api.user.consent.v1.impl.factories;

import org.wso2.carbon.consent.mgt.core.PrivilegedConsentManager;
import org.wso2.carbon.identity.api.user.consent.common.util.ConsentManagementServiceHolder;
import org.wso2.carbon.identity.api.user.consent.v1.impl.core.UserConsentService;

/**
 * Factory class for UserConsentService.
 */
public class UserConsentServiceFactory {

    private static final UserConsentService SERVICE;

    static {
        PrivilegedConsentManager consentManager = ConsentManagementServiceHolder.getPrivilegedConsentManager();
        if (consentManager == null) {
            throw new IllegalStateException("PrivilegedConsentManager is not available in the OSGi context.");
        }
        SERVICE = new UserConsentService(consentManager);
    }

    /**
     * Returns the UserConsentService instance.
     *
     * @return UserConsentService
     */
    public static UserConsentService getUserConsentService() {

        return SERVICE;
    }
}
