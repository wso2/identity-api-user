/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.user.onboard.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.user.onboard.core.service.UserOnboardCoreService;

/**
 * Service holder class for User onboard service.
 */
public class UserOnboardServiceDataHolder {

    private UserOnboardServiceDataHolder() {

    }

    private static class UserOnboardCoreServiceHolder {

        static final UserOnboardCoreService SERVICE = (UserOnboardCoreService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(UserOnboardCoreService.class, null);
    }

    /**
     * Get UserOnboardCoreService OSGi service.
     *
     * @return UserOnboardCoreService.
     */
    public static UserOnboardCoreService getUserOnboardCoreService() {

        return UserOnboardCoreServiceHolder.SERVICE;
    }
}
