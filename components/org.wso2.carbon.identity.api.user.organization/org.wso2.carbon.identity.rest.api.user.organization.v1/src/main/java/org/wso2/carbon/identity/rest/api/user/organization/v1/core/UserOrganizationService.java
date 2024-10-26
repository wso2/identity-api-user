/*
 * Copyright (c) 2023-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.organization.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.ApplicationBasicInfo;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.core.ServiceURLBuilder;
import org.wso2.carbon.identity.core.URLBuilderException;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;
import org.wso2.carbon.identity.organization.management.service.OrganizationUserResidentResolverService;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementClientException;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementException;
import org.wso2.carbon.identity.organization.management.service.model.BasicOrganization;
import org.wso2.carbon.identity.rest.api.user.organization.v1.exceptions.UserOrganizationManagementEndpointException;
import org.wso2.carbon.identity.rest.api.user.organization.v1.model.BasicOrganizationObject;
import org.wso2.carbon.identity.rest.api.user.organization.v1.model.Link;
import org.wso2.carbon.identity.rest.api.user.organization.v1.model.Organization;
import org.wso2.carbon.identity.rest.api.user.organization.v1.model.OrganizationsResponse;
import org.wso2.carbon.identity.rest.api.user.organization.v1.model.RootOrganizationResponse;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.USER_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ERROR_BUILDING_PAGINATED_RESPONSE_URL;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_INVALID_APPLICATION;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_USER_ROOT_ORGANIZATION_NOT_FOUND;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ROOT_TENANT_DOMAIN;
import static org.wso2.carbon.identity.organization.management.service.util.Utils.getOrganizationId;
import static org.wso2.carbon.identity.rest.api.user.organization.v1.Constants.ASC_SORT_ORDER;
import static org.wso2.carbon.identity.rest.api.user.organization.v1.Constants.DESC_SORT_ORDER;
import static org.wso2.carbon.identity.rest.api.user.organization.v1.Constants.ORGANIZATIONS_ME_ENDPOINT;
import static org.wso2.carbon.identity.rest.api.user.organization.v1.util.Util.buildOrganizationURL;
import static org.wso2.carbon.identity.rest.api.user.organization.v1.util.Util.getError;
import static org.wso2.carbon.identity.rest.api.user.organization.v1.util.Util.handleError;
import static org.wso2.carbon.identity.rest.api.user.organization.v1.util.Util.handleOrganizationManagementException;

/**
 * Call internal OSGi services to perform user organization management related operations.
 */
public class UserOrganizationService {

    private final OrganizationUserResidentResolverService organizationUserResidentResolverService;
    private final OrganizationManager organizationManagementService;
    private final ApplicationManagementService applicationManagementService;

    private static final Log LOG = LogFactory.getLog(UserOrganizationService.class);

    public UserOrganizationService(OrganizationUserResidentResolverService organizationUserResidentResolverService,
                                   OrganizationManager organizationManagementService,
                                   ApplicationManagementService applicationManagementService) {

        this.organizationUserResidentResolverService = organizationUserResidentResolverService;
        this.organizationManagementService = organizationManagementService;
        this.applicationManagementService = applicationManagementService;
    }

    /**
     * Retrieves the root organization visible to the user in the organization hierarchy.
     *
     * @return The root organization of the authenticated user.
     */
    public RootOrganizationResponse getUserOrganizationRoot() {

        String userId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUserId();
        String accessedOrgId = getOrganizationId();
        try {
            String rootOrgId = organizationUserResidentResolverService
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
        String accessedOrgId = getOrganizationId();
        try {
            List<BasicOrganizationObject> rootDescendantsOrganizationResponseList = new ArrayList<>();
            List<BasicOrganization> basicOrganizationList = organizationUserResidentResolverService
                    .getHierarchyUptoResidentOrganization(userId, accessedOrgId);
            for (BasicOrganization basicOrganization : basicOrganizationList) {
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

    /**
     * Retrieves the authorized organizations of the user in the organization hierarchy.
     *
     * @param filter    Filter string for filtering the organizations.
     * @param limit     Maximum number of organizations to return.
     * @param after     The pointer to next page.
     * @param before    The pointer to previous page.
     * @param recursive Determines whether recursive search is required.
     * @return The root descendants organizations of the authenticated user.
     */
    public OrganizationsResponse getAuthorizedOrganizations(String filter, Integer limit, String after, String before,
                                                            Boolean recursive, String applicationName) {

        try {
            limit = validateLimit(limit);
            String sortOrder = StringUtils.isNotBlank(before) ? ASC_SORT_ORDER : DESC_SORT_ORDER;
            String applicationAudience = null;
            if (applicationName != null) {
                applicationAudience = getApplicationAudience(applicationName);
            }
            List<BasicOrganization> authorizedOrganizations = organizationManagementService
                    .getUserAuthorizedOrganizations(limit + 1, after, before, sortOrder, filter,
                            Boolean.TRUE.equals(recursive), applicationAudience);
            return getAuthorizedOrganizationsResponse(limit, after, before, filter,
                    authorizedOrganizations, Boolean.TRUE.equals(recursive));
        } catch (OrganizationManagementException e) {
            throw handleOrganizationManagementException(e);
        }
    }

    private RootOrganizationResponse buildRootOrganizationResponse(String rootOrgId)
            throws OrganizationManagementException {

        RootOrganizationResponse rootOrganizationResponse = new RootOrganizationResponse();
        rootOrganizationResponse.setId(rootOrgId);
        rootOrganizationResponse.setName(organizationManagementService.getOrganizationNameById(rootOrgId));
        return rootOrganizationResponse;
    }

    private int validateLimit(Integer limit) {

        if (limit == null) {
            int defaultItemsPerPage = IdentityUtil.getDefaultItemsPerPage();
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Given limit is null. Therefore the default limit is set to %s.",
                        defaultItemsPerPage));
            }
            return defaultItemsPerPage;
        }

        int maximumItemsPerPage = IdentityUtil.getMaximumItemPerPage();
        if (limit > maximumItemsPerPage) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Given limit exceeds the maximum limit. Therefore the limit is set to %s.",
                        maximumItemsPerPage));
            }
            return maximumItemsPerPage;
        }
        return limit;
    }

    private OrganizationsResponse getAuthorizedOrganizationsResponse(Integer limit, String after, String before,
                                                                     String filter,
                                                                     List<BasicOrganization> organizations,
                                                                     boolean recursive) {

        OrganizationsResponse organizationsResponse = new OrganizationsResponse();

        if (limit != 0 && CollectionUtils.isNotEmpty(organizations)) {
            boolean hasMoreItems = organizations.size() > limit;
            boolean needsReverse = StringUtils.isNotBlank(before);
            boolean isFirstPage = (StringUtils.isBlank(before) && StringUtils.isBlank(after)) ||
                    (StringUtils.isNotBlank(before) && !hasMoreItems);
            boolean isLastPage = !hasMoreItems && (StringUtils.isNotBlank(after) || StringUtils.isBlank(before));

            String url = "?limit=" + limit + "&recursive=" + recursive;
            if (StringUtils.isNotBlank(filter)) {
                try {
                    url += "&filter=" + URLEncoder.encode(filter, StandardCharsets.UTF_8.name());
                } catch (UnsupportedEncodingException e) {
                    LOG.error("Server encountered an error while building paginated URL for the response.", e);
                    throw handleError(Response.Status.INTERNAL_SERVER_ERROR,
                            ERROR_CODE_ERROR_BUILDING_PAGINATED_RESPONSE_URL);
                }
            }

            if (hasMoreItems) {
                organizations.remove(organizations.size() - 1);
            }
            if (needsReverse) {
                Collections.reverse(organizations);
            }
            if (!isFirstPage) {
                String encodedString = Base64.getEncoder().encodeToString(organizations.get(0).getCreated()
                        .getBytes(StandardCharsets.UTF_8));
                Link link = new Link();
                link.setHref(URI.create(buildURIForPagination(url) + "&before=" + encodedString));
                link.setRel("previous");
                organizationsResponse.addLinksItem(link);
            }
            if (!isLastPage) {
                String encodedString = Base64.getEncoder().encodeToString(organizations.get(organizations.size() - 1)
                        .getCreated().getBytes(StandardCharsets.UTF_8));
                Link link = new Link();
                link.setHref(URI.create(buildURIForPagination(url) + "&after=" + encodedString));
                link.setRel("next");
                organizationsResponse.addLinksItem(link);
            }

            List<Organization> organizationDTOs = new ArrayList<>();
            for (BasicOrganization organization : organizations) {
                Organization organizationDTO = new Organization();
                organizationDTO.setId(organization.getId());
                organizationDTO.setName(organization.getName());
                organizationDTO.setStatus(Organization.StatusEnum.valueOf(organization.getStatus()));
                organizationDTO.setRef(buildOrganizationURL(organization.getId()).toString());
                organizationDTOs.add(organizationDTO);
            }
            organizationsResponse.setOrganizations(organizationDTOs);
        }
        return organizationsResponse;
    }

    private String buildURIForPagination(String paginationURL) {

        try {
            return ServiceURLBuilder.create().addPath(getContext(paginationURL)).build().getRelativePublicURL();
        } catch (URLBuilderException e) {
            LOG.error("Server encountered an error while building paginated URL for the response.", e);
            throw handleError(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_ERROR_BUILDING_PAGINATED_RESPONSE_URL);
        }
    }

    private String getContext(String url) {

        String organizationId = getOrganizationId();
        String endpoint = ORGANIZATIONS_ME_ENDPOINT + url;
        return StringUtils.isNotBlank(organizationId) ?
                String.format("/o/%s", organizationId) + USER_API_PATH_COMPONENT + endpoint :
                USER_API_PATH_COMPONENT + endpoint;
    }

    private String getApplicationAudience(String applicationName) throws OrganizationManagementException {

        try {
            String tenantDomain = (String) IdentityUtil.threadLocalProperties.get().get(ROOT_TENANT_DOMAIN);
            if (StringUtils.isBlank(tenantDomain)) {
                tenantDomain = IdentityTenantUtil.resolveTenantDomain();
            }
            ApplicationBasicInfo applicationBasicInfo = applicationManagementService
                    .getApplicationBasicInfoByName(applicationName, tenantDomain);
            if (applicationBasicInfo != null) {
                return applicationBasicInfo.getApplicationResourceId();
            }
            throw new OrganizationManagementClientException(ERROR_CODE_INVALID_APPLICATION.getMessage(),
                    String.format(ERROR_CODE_INVALID_APPLICATION.getDescription(), applicationName),
                    ERROR_CODE_INVALID_APPLICATION.getCode());
        } catch (IdentityApplicationManagementException e) {
            throw new OrganizationManagementException(e.getMessage(), e.getErrorCode(), e);
        }
    }
}
