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

package org.wso2.carbon.identity.api.user.organization.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;
import org.wso2.carbon.identity.organization.management.service.OrganizationUserResidentResolverService;

/**
 * Service holder class for user organization management services.
 */
public class UserOrganizationServiceHolder {

    private UserOrganizationServiceHolder () {}

    private static class OrganizationUserResidentResolverServiceHolder {

        static final OrganizationUserResidentResolverService SERVICE = (OrganizationUserResidentResolverService)
                PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(OrganizationUserResidentResolverService.class, null);
    }

    private static class OrganizationManagementServiceHolder {

        static final OrganizationManager SERVICE = (OrganizationManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(OrganizationManager.class, null);
    }

    private static class ApplicationManagementServiceHolder {

        static final ApplicationManagementService SERVICE = (ApplicationManagementService)
                PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(ApplicationManagementService.class, null);
    }

    /**
     * Method to get the organization user resident resolver OSGi service.
     *
     * @return OrganizationUserResidentResolverService.
     */
    public static OrganizationUserResidentResolverService getOrganizationUserResidentResolverService() {

        return OrganizationUserResidentResolverServiceHolder.SERVICE;
    }

    /**
     * Get organization management service.
     *
     * @return Organization management service.
     */
    public static OrganizationManager getOrganizationManagementService() {

        return OrganizationManagementServiceHolder.SERVICE;
    }

    /**
     * Get application management service.
     *
     * @return Application management service.
     */
    public static ApplicationManagementService getApplicationManagementService() {

        return ApplicationManagementServiceHolder.SERVICE;
    }

}
