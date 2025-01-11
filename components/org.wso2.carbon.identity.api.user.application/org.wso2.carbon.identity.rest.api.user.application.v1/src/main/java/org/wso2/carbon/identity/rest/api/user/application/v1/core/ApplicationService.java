/*
 * Copyright (c) 2019-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.application.v1.core;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.application.common.ApplicationServiceConstants;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.ApplicationBasicInfo;
import org.wso2.carbon.identity.application.mgt.DiscoverableApplicationManager;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.model.ExpressionNode;
import org.wso2.carbon.identity.core.model.FilterTreeBuilder;
import org.wso2.carbon.identity.core.model.Node;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.organization.management.application.OrgApplicationManager;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementException;
import org.wso2.carbon.identity.organization.management.service.util.OrganizationManagementUtil;
import org.wso2.carbon.identity.rest.api.user.application.v1.core.function.ApplicationBasicInfoToApiModel;
import org.wso2.carbon.identity.rest.api.user.application.v1.model.ApplicationListResponse;
import org.wso2.carbon.identity.rest.api.user.application.v1.model.ApplicationResponse;
import org.wso2.carbon.identity.rest.api.user.application.v1.model.Link;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.application.common.ApplicationServiceConstants.ErrorMessage.ERROR_CODE_ATTRIBUTE_FILTERING_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.user.application.common.ApplicationServiceConstants.ErrorMessage.ERROR_CODE_INVALID_FILTER_QUERY;
import static org.wso2.carbon.identity.api.user.application.common.ApplicationServiceConstants.ErrorMessage.ERROR_CODE_SORTING_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.user.application.common.ApplicationServiceConstants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE;
import static org.wso2.carbon.identity.api.user.application.common.ApplicationServiceConstants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_OPERATION;

/**
 * Call internal osgi services to perform user application related operations.
 */
public class ApplicationService {

    private final OrgApplicationManager orgApplicationManager;
    private final DiscoverableApplicationManager discoverableApplicationManager;

    private static final String APPLICATIONS_PAGINATION_LINK_FORMAT = "/v1/me/applications?offset=%d&limit=%d";
    private static final Log LOG = LogFactory.getLog(ApplicationService.class);
    private static final String PAGE_LINK_REL_NEXT = "next";
    private static final String PAGE_LINK_REL_PREVIOUS = "previous";

    public ApplicationService (OrgApplicationManager orgApplicationManager, DiscoverableApplicationManager
            discoverableApplicationManager) {

        this.orgApplicationManager = orgApplicationManager;
        this.discoverableApplicationManager = discoverableApplicationManager;
    }

    /**
     * Get application from application ID.
     *
     * @param applicationId unique identifier of the application
     * @return an Application instance.
     */
    public ApplicationResponse getApplication(String applicationId) {

        try {

            String tenantDomain = IdentityTenantUtil.resolveTenantDomain();
            ApplicationBasicInfo applicationBasicInfo = discoverableApplicationManager
                    .getDiscoverableApplicationBasicInfoByResourceId(applicationId, tenantDomain);
            if (applicationBasicInfo == null) {
                throw handleNotFoundError(applicationId, ApplicationServiceConstants.ErrorMessage
                        .ERROR_CODE_APPLICATION_NOT_FOUND);

            }
            return buildApplicationResponse(applicationBasicInfo);
        } catch (IdentityApplicationManagementException e) {
            ApplicationServiceConstants.ErrorMessage errorEnum =
                    ApplicationServiceConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_APPLICATION;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    /**
     * Get applications visible for the user matching the given criteria.
     *
     * @param attributes attributes of the application to be returned (optional).
     * @param limit      maximum no of applications to be returned in the result set (optional).
     * @param offset     zero based index of the first application to be returned in the result set (optional).
     * @param filter     filter to search for applications (optional).
     * @param sortOrder  sort order, ascending or descending (optional).
     * @param sortBy     attribute to sort from (optional).
     * @return List of applications matching the given criteria.
     */
    public ApplicationListResponse getApplications(String attributes, Integer limit, Integer offset, String filter,
                                                   String sortOrder, String sortBy) {

        handleNotImplementedCapabilities(attributes, sortOrder, sortBy);

        String tenantDomain = IdentityTenantUtil.resolveTenantDomain();
        String filterFormatted = buildFilter(filter);
        try {
            List<ApplicationBasicInfo> applicationBasicInfos = getDiscoverableApplicationBasicInfo(limit, offset,
                    filterFormatted, sortOrder, sortBy, tenantDomain);
            int totalApps = getCountOfDiscoverableApplications(filterFormatted, tenantDomain);
            return buildApplicationListResponse(limit, offset, totalApps, applicationBasicInfos);

        } catch (IdentityApplicationManagementException e) {
            ApplicationServiceConstants.ErrorMessage errorEnum =
                    ApplicationServiceConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_APPLICATIONS;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    private List<ApplicationBasicInfo> getDiscoverableApplicationBasicInfo(int limit, int offset, String filter,
                                                                           String sortOrder, String sortBy,
                                                                           String tenantDomain)
            throws IdentityApplicationManagementException {

        try {
            if (OrganizationManagementUtil.isOrganization(tenantDomain)) {
                return orgApplicationManager.getDiscoverableSharedApplicationBasicInfo(
                        limit, offset, filter, sortOrder, sortBy, tenantDomain);
            }
            return discoverableApplicationManager.getDiscoverableApplicationBasicInfo(
                    limit, offset, filter, sortOrder, sortBy, tenantDomain);
        } catch (OrganizationManagementException e) {
            throw new IdentityApplicationManagementException(e.getMessage(), e);
        }
    }

    private int getCountOfDiscoverableApplications(String filter, String tenantDomain)
            throws IdentityApplicationManagementException {

        try {
            if (OrganizationManagementUtil.isOrganization(tenantDomain)) {
                return orgApplicationManager.getCountOfDiscoverableSharedApplications(filter, tenantDomain);
            }
            return discoverableApplicationManager.getCountOfDiscoverableApplications(filter, tenantDomain);
        } catch (OrganizationManagementException e) {
            throw new IdentityApplicationManagementException(e.getMessage(), e);
        }
    }

    private ApplicationResponse buildApplicationResponse(ApplicationBasicInfo applicationBasicInfo) {

        return new ApplicationBasicInfoToApiModel().apply(applicationBasicInfo);
    }

    private ApplicationListResponse buildApplicationListResponse(int limit, int offset, int total,
                                                                 List<ApplicationBasicInfo> applicationBasicInfos) {

        List<ApplicationResponse> applicationResponseList = buildApplicationResponses(applicationBasicInfos);
        List<Link> applicationResponseLinks = buildPaginationLinks(limit, offset, total);
        ApplicationListResponse applicationListResponse = new ApplicationListResponse().applications
                (applicationResponseList).count(applicationResponseList.size()).startIndex(offset + 1).totalResults
                (total).links(applicationResponseLinks);

        return applicationListResponse;
    }

    private List<ApplicationResponse> buildApplicationResponses(List<ApplicationBasicInfo> applicationBasicInfos) {

        return applicationBasicInfos.stream().map(new ApplicationBasicInfoToApiModel()).collect(Collectors.toList());
    }

    private List<Link> buildPaginationLinks(int limit, int offset, int total) {

        List<Link> links = new ArrayList<>();

        // Next Link
        if ((offset + limit) < total) {
            links.add(buildPageLink(PAGE_LINK_REL_NEXT, (offset + limit), limit));
        }

        // Previous Link
        // Previous link matters only if offset is greater than 0.
        if (offset > 0) {
            if ((offset - limit) >= 0) { // A previous page of size 'limit' exists
                links.add(buildPageLink(PAGE_LINK_REL_PREVIOUS, calculateOffsetForPreviousLink(offset, limit, total),
                        limit));
            } else { // A previous page exists but it's size is less than the specified limit
                links.add(buildPageLink(PAGE_LINK_REL_PREVIOUS, 0, offset));
            }
        }

        return links;
    }

    private int calculateOffsetForPreviousLink(int offset, int limit, int total) {

        int newOffset = (offset - limit);
        if (newOffset < total) {
            return newOffset;
        }

        return calculateOffsetForPreviousLink(newOffset, limit, total);
    }

    private Link buildPageLink(String rel, int offset, int limit) {

        return new Link().rel(rel).href(ContextLoader.buildURIForBody
                (String.format(APPLICATIONS_PAGINATION_LINK_FORMAT, offset, limit)));
    }

    private String buildFilter(String filter) {

        if (StringUtils.isNotBlank(filter)) {
            try {
                FilterTreeBuilder filterTreeBuilder = new FilterTreeBuilder(filter);
                Node rootNode = filterTreeBuilder.buildTree();

                if (!(rootNode instanceof ExpressionNode)) {
                    throw buildError(ERROR_CODE_INVALID_FILTER_QUERY, Response.Status.BAD_REQUEST);
                }

                ExpressionNode expressionNode = (ExpressionNode) rootNode;
                if (!isFilterableAttribute(expressionNode.getAttributeValue())) {
                    throw buildError(ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE, Response.Status.BAD_REQUEST,
                            expressionNode.getAttributeValue());
                }

                return generateFilterStringForBackend(expressionNode.getOperation(), expressionNode.getValue());
            } catch (IOException | IdentityException e) {
                throw buildError(ERROR_CODE_INVALID_FILTER_QUERY, Response.Status.BAD_REQUEST);
            }
        } else {
            return null;
        }
    }

    private String generateFilterStringForBackend(String operation, String attributeValue) {

        String formattedFilter = null;
        try {
            switch (AttributeOperators.valueOf(operation)) {
                case sw:
                    formattedFilter = attributeValue + "*";
                    break;
                case ew:
                    formattedFilter = "*" + attributeValue;
                    break;
                case eq:
                    formattedFilter = attributeValue;
                    break;
                case co:
                    formattedFilter = "*" + attributeValue + "*";
                    break;
            }
        } catch (IllegalArgumentException e) {
            throw handleException(e, ERROR_CODE_UNSUPPORTED_FILTER_OPERATION, Response.Status.BAD_REQUEST, operation);

        }

        return formattedFilter;
    }

    private boolean isFilterableAttribute(String attribute) {

        return Arrays.stream(FilterableAttributes.values()).anyMatch(filterableAttribute -> filterableAttribute.name()
                .equals(attribute));
    }

    private APIError handleException(Exception e, ApplicationServiceConstants.ErrorMessage
            errorEnum, Response.Status status, String... data) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum, data).build(LOG, e, errorEnum.getDescription());
        return new APIError(status, errorResponse);
    }

    private APIError buildError(ApplicationServiceConstants.ErrorMessage
                                        errorEnum, Response.Status status, String... data) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum, data).build(LOG, null, errorEnum.getDescription());
        return new APIError(status, errorResponse);
    }

    private ErrorResponse.Builder getErrorBuilder(ApplicationServiceConstants.ErrorMessage errorMsg, String... data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(buildErrorDescription(errorMsg, data));
    }

    private String buildErrorDescription(ApplicationServiceConstants.ErrorMessage errorEnum, String... data) {

        String errorDescription;

        if (ArrayUtils.isNotEmpty(data)) {
            errorDescription = String.format(errorEnum.getDescription(), data);
        } else {
            errorDescription = errorEnum.getDescription();
        }

        return errorDescription;
    }

    private APIError handleNotFoundError(String resourceId, ApplicationServiceConstants.ErrorMessage errorMessage) {

        Response.Status status = Response.Status.NOT_FOUND;
        ErrorResponse errorResponse =
                getErrorBuilder(errorMessage, resourceId).build(LOG, errorMessage.getDescription());

        return new APIError(status, errorResponse);
    }

    private void handleNotImplementedCapabilities(String attributes, String sortBy, String sortOrder) {

        ApplicationServiceConstants.ErrorMessage errorEnum = null;

        if (attributes != null) {
            errorEnum = ERROR_CODE_ATTRIBUTE_FILTERING_NOT_IMPLEMENTED;
        } else if (sortBy != null || sortOrder != null) {
            errorEnum = ERROR_CODE_SORTING_NOT_IMPLEMENTED;
        }

        if (errorEnum != null) {
            ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(LOG, errorEnum.getDescription());
            Response.Status status = Response.Status.NOT_IMPLEMENTED;

            throw new APIError(status, errorResponse);
        }
    }

    private enum AttributeOperators {
        eq, sw, co, ew;
    }

    private enum FilterableAttributes {
        name;
    }
}
