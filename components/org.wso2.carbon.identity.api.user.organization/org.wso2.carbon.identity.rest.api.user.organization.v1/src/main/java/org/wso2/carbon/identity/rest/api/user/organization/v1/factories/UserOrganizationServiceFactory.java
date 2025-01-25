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

    private static final UserOrganizationService SERVICE;

    static {
        OrganizationUserResidentResolverService organizationUserResidentResolverService =
                UserOrganizationServiceHolder.getOrganizationUserResidentResolverService();
        OrganizationManager organizationManagementService =
                UserOrganizationServiceHolder.getOrganizationManagementService();
        ApplicationManagementService applicationManagementService =
                UserOrganizationServiceHolder.getApplicationManagementService();

        if (organizationUserResidentResolverService == null) {
            throw new IllegalStateException(
                    "OrganizationUserResidentResolverService is not available from OSGi context.");
        } else if (organizationManagementService == null) {
            throw new IllegalStateException("OrganizationManagementService is not available from OSGi context.");
        } else if (applicationManagementService == null) {
            throw new IllegalStateException("ApplicationManagementService is not available from OSGi context.");
        }
        SERVICE = new UserOrganizationService(organizationUserResidentResolverService, organizationManagementService,
                applicationManagementService);
    }

    /**
     * Get UserOrganizationService.
     *
     * @return UserOrganizationService.
     */
    public static UserOrganizationService getUserOrganizationService() {

        return SERVICE;
    }
}
