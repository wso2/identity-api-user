/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com). All Rights Reserved.
 *
 * This software is the property of WSO2 LLC. and its suppliers, if any.
 * Dissemination of any information or reproduction of any material contained
 * herein in any form is strictly forbidden, unless permitted by WSO2 expressly.
 * You may not alter or remove any copyright or other notice from copies of this content.
 */

package org.wso2.carbon.identity.api.user.organization.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.organization.common.UserOrganizationServiceHolder;
import org.wso2.carbon.identity.api.user.organization.v1.exceptions.UserOrganizationManagementEndpointException;
import org.wso2.carbon.identity.api.user.organization.v1.model.BasicOrganizationObject;
import org.wso2.carbon.identity.api.user.organization.v1.model.RootOrganizationResponse;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;
import org.wso2.carbon.identity.organization.management.service.OrganizationUserResidentResolverService;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementException;
import org.wso2.carbon.identity.organization.management.service.model.BasicOrganization;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.organization.v1.util.Util.getError;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_USER_ROOT_ORGANIZATION_NOT_FOUND;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.SUPER_ORG_ID;

/**
 * Call internal osgi services to perform user organization management related operations.
 */
public class UserOrganizationService {

    private static final Log LOG = LogFactory.getLog(UserOrganizationService.class);

    /**
     * Retrieves the root organization visible to the user in the organization hierarchy.
     *
     * @return The root organization of the authenticated user.
     */
    public RootOrganizationResponse getUserOrganizationRoot() {

        String userId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUserId();
        String accessedOrgId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getOrganizationId();
        if (StringUtils.isBlank(accessedOrgId)) {
            accessedOrgId = SUPER_ORG_ID;
        }
        try {
            String rootOrgId = getOrganizationUserResidentResolverService()
                    .resolveResidentOrganization(userId, accessedOrgId)
                    .orElseThrow(() -> new UserOrganizationManagementEndpointException(Response.Status.NOT_FOUND,
                            getError(ERROR_CODE_USER_ROOT_ORGANIZATION_NOT_FOUND, userId)));
            return buildRootOrganizationResponse(rootOrgId);
        } catch (OrganizationManagementException e) {
            LOG.error(String.format("Server encountered an error while retrieving root organization of user with ID: " +
                    "%s.", userId), e);
            throw new UserOrganizationManagementEndpointException(Response.Status.INTERNAL_SERVER_ERROR, getError(e));
        }
    }

    /**
     * Retrieves the user organization hierarchy up to the resident organization.
     *
     * @return The root descendants organizations of the authenticated user.
     */
    public List<BasicOrganizationObject> getUserOrganizationHierarchyUptoResidentOrganization() {

        String userId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUserId();
        String accessedOrgId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getOrganizationId();
        if (StringUtils.isBlank(accessedOrgId)) {
            accessedOrgId = SUPER_ORG_ID;
        }
        try {
            List<BasicOrganizationObject> rootDescendantsOrganizationResponseList = new ArrayList<>();
            List<BasicOrganization> basicOrganizationList = getOrganizationUserResidentResolverService()
                    .getHierarchyUptoResidentOrganization(userId, accessedOrgId);
            for (BasicOrganization basicOrganization: basicOrganizationList) {
                BasicOrganizationObject basicOrganizationObject = new BasicOrganizationObject();
                basicOrganizationObject.setId(basicOrganization.getId());
                basicOrganizationObject.setName(basicOrganization.getName());
                rootDescendantsOrganizationResponseList.add(basicOrganizationObject);
            }
            return rootDescendantsOrganizationResponseList;
        } catch (OrganizationManagementException e) {
            LOG.error(String.format("Server encountered an error while retrieving the descendants of user resident" +
                    " organization until the accessed organization of user with ID: %s.", userId), e);
            throw new UserOrganizationManagementEndpointException(Response.Status.INTERNAL_SERVER_ERROR, getError(e));
        }
    }

    private RootOrganizationResponse buildRootOrganizationResponse(String rootOrgId)
            throws OrganizationManagementException {

        RootOrganizationResponse rootOrganizationResponse = new RootOrganizationResponse();
        rootOrganizationResponse.setId(rootOrgId);
        rootOrganizationResponse.setName(getOrganizationManagementService().getOrganizationNameById(rootOrgId));
        return rootOrganizationResponse;
    }

    private OrganizationUserResidentResolverService getOrganizationUserResidentResolverService() {

        return UserOrganizationServiceHolder.getOrganizationUserResidentResolverService();
    }

    private OrganizationManager getOrganizationManagementService() {

        return UserOrganizationServiceHolder.getOrganizationManagementService();
    }
}
