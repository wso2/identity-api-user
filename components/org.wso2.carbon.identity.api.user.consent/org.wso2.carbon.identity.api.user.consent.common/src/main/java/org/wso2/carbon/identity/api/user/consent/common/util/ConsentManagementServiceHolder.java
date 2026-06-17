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

package org.wso2.carbon.identity.api.user.consent.common.util;

import org.wso2.carbon.consent.mgt.core.PrivilegedConsentManager;
import org.wso2.carbon.context.PrivilegedCarbonContext;

/**
 * Service holder class for consent management OSGi services.
 */
public class ConsentManagementServiceHolder {

    private ConsentManagementServiceHolder() {}

    private static class PrivilegedConsentManagerHolder {

        static final PrivilegedConsentManager SERVICE = (PrivilegedConsentManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(PrivilegedConsentManager.class, null);
    }

    /**
     * Returns the PrivilegedConsentManager OSGi service.
     *
     * @return PrivilegedConsentManager
     */
    public static PrivilegedConsentManager getPrivilegedConsentManager() {

        return PrivilegedConsentManagerHolder.SERVICE;
    }
}
