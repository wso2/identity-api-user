/*
 * Copyright (c) 2019-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.user.application.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.mgt.DiscoverableApplicationManager;
import org.wso2.carbon.identity.organization.management.application.OrgApplicationManager;

/**
 * Application management service holder.
 */
public class ApplicationServiceHolder {

    private ApplicationServiceHolder () {}

    private static class OrgApplicationManagerServiceHolder {

        static final OrgApplicationManager SERVICE = (OrgApplicationManager)
                PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(OrgApplicationManager.class, null);
    }

    private static class DiscoverableApplicationManagerServiceHolder {

        static final DiscoverableApplicationManager SERVICE = (DiscoverableApplicationManager)
                PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(DiscoverableApplicationManager.class, null);
    }

    /**
     * Get application management service.
     *
     * @return ApplicationManagementService.
     */
    public static DiscoverableApplicationManager getDiscoverableApplicationManager() {

        return DiscoverableApplicationManagerServiceHolder.SERVICE;
    }

    /**
     * Get organization application management service.
     *
     * @return OrgApplicationManager.
     */
    public static OrgApplicationManager getOrgApplicationManager() {

        return OrgApplicationManagerServiceHolder.SERVICE;
    }

}
