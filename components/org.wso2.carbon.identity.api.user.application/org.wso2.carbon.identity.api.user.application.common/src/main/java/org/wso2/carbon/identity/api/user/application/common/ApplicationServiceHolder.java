/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

import org.wso2.carbon.identity.application.mgt.DiscoverableApplicationManager;
import org.wso2.carbon.identity.organization.management.application.OrgApplicationManager;

/**
 * Application management service holder.
 */
public class ApplicationServiceHolder {

    private static OrgApplicationManager orgApplicationManager;
    private static DiscoverableApplicationManager discoverableApplicationManager;

    /**
     * Get application management service.
     *
     * @return ApplicationManagementService
     */
    public static DiscoverableApplicationManager getDiscoverableApplicationManager() {

        return discoverableApplicationManager;
    }

    /**
     * Set application management service.
     *
     * @param discoverableApplicationManager
     */
    public static void setDiscoverableApplicationManager(DiscoverableApplicationManager
                                                                 discoverableApplicationManager) {

        ApplicationServiceHolder.discoverableApplicationManager = discoverableApplicationManager;
    }

    /**
     * Get organization application management service.
     *
     * @return OrgApplicationManager
     */
    public static OrgApplicationManager getOrgApplicationManager() {

        return orgApplicationManager;
    }

    /**
     * Set organization application management service.
     *
     * @param orgApplicationManager
     */
    public static void setOrgApplicationManager(OrgApplicationManager orgApplicationManager) {

        ApplicationServiceHolder.orgApplicationManager = orgApplicationManager;
    }
}
