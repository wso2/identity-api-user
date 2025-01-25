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

package org.wso2.carbon.identity.rest.api.user.application.v1.factories;

import org.wso2.carbon.identity.api.user.application.common.ApplicationServiceHolder;
import org.wso2.carbon.identity.application.mgt.DiscoverableApplicationManager;
import org.wso2.carbon.identity.organization.management.application.OrgApplicationManager;
import org.wso2.carbon.identity.rest.api.user.application.v1.core.ApplicationService;

/**
 * Factory to return ApplicationService.
 */
public class ApplicationServiceFactory {

    private static final ApplicationService SERVICE;

    static {
        OrgApplicationManager orgApplicationManager = ApplicationServiceHolder
                .getOrgApplicationManager();
        DiscoverableApplicationManager discoverableApplicationManager = ApplicationServiceHolder
                .getDiscoverableApplicationManager();

        if (orgApplicationManager == null) {
            throw new IllegalStateException("OrgApplicationManager not available in the OSGi context.");
        } else if (discoverableApplicationManager == null) {
            throw new IllegalStateException("DiscoverableApplicationManager not available in the OSGi context.");
        }
        SERVICE = new ApplicationService(orgApplicationManager, discoverableApplicationManager);
    }

    /**
     * Get ApplicationService instance.
     *
     * @return ApplicationService instance.
     */
    public static ApplicationService getApplicationService() {

        return SERVICE;
    }

}
