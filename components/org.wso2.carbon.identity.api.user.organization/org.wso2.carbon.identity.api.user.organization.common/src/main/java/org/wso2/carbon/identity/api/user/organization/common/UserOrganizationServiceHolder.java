/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com). All Rights Reserved.
 *
 * This software is the property of WSO2 LLC. and its suppliers, if any.
 * Dissemination of any information or reproduction of any material contained
 * herein in any form is strictly forbidden, unless permitted by WSO2 expressly.
 * You may not alter or remove any copyright or other notice from copies of this content.
 */

package org.wso2.carbon.identity.api.user.organization.common;

import org.wso2.carbon.identity.organization.management.service.OrganizationManager;
import org.wso2.carbon.identity.organization.management.service.OrganizationUserResidentResolverService;

/**
 * Service holder class for user organization management services.
 */
public class UserOrganizationServiceHolder {

    private static OrganizationUserResidentResolverService organizationUserResidentResolverService;

    private static OrganizationManager organizationManagementService;

    /**
     * Method to get the organization user resident resolver osgi service.
     *
     * @return OrganizationUserResidentResolverService.
     */
    public static OrganizationUserResidentResolverService getOrganizationUserResidentResolverService() {

        return organizationUserResidentResolverService;
    }

    /**
     * Set OrganizationUserResidentResolverService osgi service.
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
}
