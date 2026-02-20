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

package org.wso2.carbon.identity.api.user.password.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.event.services.IdentityEventService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Service holder class for password management services.
 */
public class PasswordServiceHolder {

    private PasswordServiceHolder() {
    }

    private static class RealmServiceHolder {

        static final RealmService SERVICE = (RealmService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(RealmService.class, null);
    }

    private static class IdentityEventServiceHolder {

        static final IdentityEventService SERVICE = (IdentityEventService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(IdentityEventService.class, null);
    }

    /**
     * Method to get the RealmService OSGi service.
     *
     * @return RealmService.
     */
    public static RealmService getRealmService() {

        RealmService service = RealmServiceHolder.SERVICE;
        if (service == null) {
            throw new IllegalStateException("RealmService is not available from OSGi context.");
        }
        return service;
    }

    /**
     * Method to get the IdentityEventService OSGi service.
     *
     * @return IdentityEventService.
     */
    public static IdentityEventService getIdentityEventService() {

        IdentityEventService service = IdentityEventServiceHolder.SERVICE;
        if (service == null) {
            throw new IllegalStateException("IdentityEventService is not available from OSGi context.");
        }
        return service;
    }
}

