/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.organization.v1.factories;

import org.wso2.carbon.identity.api.user.organization.common.UserOrganizationServiceHolder;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;
import org.wso2.carbon.identity.organization.management.service.OrganizationUserResidentResolverService;
import org.wso2.carbon.identity.rest.api.user.organization.v1.core.UserOrganizationService;

/**
 * Factory class for UserOrganizationService.
 */
public class UserOrganizationServiceFactory {

    private UserOrganizationServiceFactory() {

    }

    private static class UserOrganizationServicesHolder {

        private static final UserOrganizationService SERVICE = createServiceInstance();
    }

    private static UserOrganizationService createServiceInstance() {

        OrganizationUserResidentResolverService organizationUserResidentResolverService =
                getOrganizationUserResidentResolverService();
        OrganizationManager organizationManagementService = getOrganizationManager();
        ApplicationManagementService applicationManagementService = getApplicationManagementService();

        return new UserOrganizationService(organizationUserResidentResolverService, organizationManagementService,
                applicationManagementService);
    }

    /**
     * Get UserOrganizationService.
     *
     * @return UserOrganizationService.
     */
    public static UserOrganizationService getUserOrganizationService() {

        return UserOrganizationServicesHolder.SERVICE;
    }

    private static OrganizationUserResidentResolverService getOrganizationUserResidentResolverService() {

        OrganizationUserResidentResolverService service = UserOrganizationServiceHolder
                .getOrganizationUserResidentResolverService();
        if (service == null) {
            throw new IllegalStateException("OrganizationUserResidentResolverService is not available " +
                    "from OSGi context.");
        }

        return service;
    }

    private static OrganizationManager getOrganizationManager() {

        OrganizationManager service = UserOrganizationServiceHolder.getOrganizationManagementService();
        if (service == null) {
            throw new IllegalStateException("OrganizationManager is not available from OSGi context.");
        }

        return service;
    }

    private static ApplicationManagementService getApplicationManagementService() {

        ApplicationManagementService service = UserOrganizationServiceHolder.getApplicationManagementService();
        if (service == null) {
            throw new IllegalStateException("ApplicationManagementService is not available from OSGi context.");
        }

        return service;
    }
}
