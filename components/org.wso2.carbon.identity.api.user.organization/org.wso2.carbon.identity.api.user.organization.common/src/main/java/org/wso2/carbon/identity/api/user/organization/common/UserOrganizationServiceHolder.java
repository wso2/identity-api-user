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

import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;
import org.wso2.carbon.identity.organization.management.service.OrganizationUserResidentResolverService;

/**
 * Service holder class for user organization management services.
 */
public class UserOrganizationServiceHolder {

    private static OrganizationUserResidentResolverService organizationUserResidentResolverService;

    private static OrganizationManager organizationManagementService;
    private static ApplicationManagementService applicationManagementService;

    /**
     * Method to get the organization user resident resolver OSGi service.
     *
     * @return OrganizationUserResidentResolverService.
     */
    public static OrganizationUserResidentResolverService getOrganizationUserResidentResolverService() {

        return organizationUserResidentResolverService;
    }

    /**
     * Set OrganizationUserResidentResolverService OSGi service.
     *
     * @param organizationUserResidentResolverService OrganizationUserResidentResolverService.
     */
    public static void setOrganizationUserResidentResolverService(
            OrganizationUserResidentResolverService organizationUserResidentResolverService) {

        UserOrganizationServiceHolder.organizationUserResidentResolverService = organizationUserResidentResolverService;
    }

    /**
     * Get organization management service.
     *
     * @return Organization management service.
     */
    public static OrganizationManager getOrganizationManagementService() {

        return organizationManagementService;
    }

    /**
     * Set Organization management OSGi service.
     *
     * @param organizationManagementService Organization management service.
     */
    public static void setOrganizationManagementService(
            OrganizationManager organizationManagementService) {

        UserOrganizationServiceHolder.organizationManagementService = organizationManagementService;
    }

    /**
     * Get application management service.
     *
     * @return Application management service.
     */
    public static ApplicationManagementService getApplicationManagementService() {

        return applicationManagementService;
    }

    /**
     * Set application management OSGi service.
     *
     * @param applicationManagementService Application management service.
     */
    public static void setApplicationManagementService(
            ApplicationManagementService applicationManagementService) {

        UserOrganizationServiceHolder.applicationManagementService = applicationManagementService;
    }
}
