/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.api.user.common;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.ServiceURLBuilder;
import org.wso2.carbon.identity.core.URLBuilderException;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.UserStoreConfigConstants;
import org.wso2.carbon.user.core.common.AbstractUserStoreManager;

import java.net.URI;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessage.ERROR_CODE_INVALID_USERNAME;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessage.ERROR_CODE_SERVER_ERROR;
import static org.wso2.carbon.identity.api.user.common.Constants.TENANT_CONTEXT_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.user.common.Constants.TENANT_NAME_FROM_CONTEXT;
import static org.wso2.carbon.identity.api.user.common.Constants.USER_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.Error.UNEXPECTED_SERVER_ERROR;

/**
 * Load information from context
 */
public class ContextLoader {

    private static final Log LOG = LogFactory.getLog(ContextLoader.class);

    /**
     * Retrieves loaded tenant domain from carbon context.
     * @return tenant domain of the request is being served.
     */
    public static String getTenantDomainFromContext() {

        String tenantDomain = MultitenantConstants.SUPER_TENANT_DOMAIN_NAME;
        if (IdentityUtil.threadLocalProperties.get().get(TENANT_NAME_FROM_CONTEXT) != null) {
            tenantDomain = (String) IdentityUtil.threadLocalProperties.get().get(TENANT_NAME_FROM_CONTEXT);
        }
        return tenantDomain;
    }

    /**
     * Retrieves authenticated username from carbon context.
     * @return username of the authenticated user.
     */
    public static String getUsernameFromContext() {

        return PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
    }

    /**
     * Retrieves authenticated username from carbon context.
     * @return username of the authenticated user.
     */
    public static User getUserFromContext() {

        return getUser(getTenantDomainFromContext(), getUsernameFromContext());
    }


    /**
     * Build user object from tenant domain and username
     * @param tenantDomain
     * @param decodedUsername
     * @return
     */
    public static User getUser(String tenantDomain, String decodedUsername) {
        String realm = UserStoreConfigConstants.PRIMARY;
        String username;
        String[] strComponent = decodedUsername.split("/");

        if (strComponent.length == 1) {
            username = strComponent[0];
        } else if (strComponent.length == 2) {
            realm = strComponent[0];
            username = strComponent[1];
        } else {
            throw new APIError(Response.Status.BAD_REQUEST, new ErrorResponse.Builder().withDescription("Provided " +
                    "Username is not in the correct format.")
                    .withCode(ERROR_CODE_INVALID_USERNAME.getCode())
                    .withMessage(ERROR_CODE_INVALID_USERNAME.getMessage()).build());
        }

        User user = new User();
        user.setUserName(username);
        user.setUserStoreDomain(realm);
        user.setTenantDomain(tenantDomain);
        return user;
    }

    /**
     * Build {@link org.wso2.carbon.identity.application.common.model} object from the
     * {@link org.wso2.carbon.user.core.common.User}.
     *
     * @param user Common user {@link org.wso2.carbon.user.core.common.User}.
     * @return Application user {@link org.wso2.carbon.identity.application.common.model}.
     */
    public static org.wso2.carbon.identity.application.common.model.User getUser(org.wso2.carbon.user.core.common.User
                                                                                         user) {

        String realm = UserStoreConfigConstants.PRIMARY;
        String username;
        String tenantDomain;

        if (StringUtils.isNotEmpty(user.getUsername()) && StringUtils.isNotEmpty(user.getTenantDomain())) {
            username = user.getUsername();
            tenantDomain = user.getTenantDomain();
        } else {
            throw new APIError(Response.Status.BAD_REQUEST, new ErrorResponse.Builder().withDescription("Provided " +
                    "user cannot be empty.")
                    .withCode(ERROR_CODE_INVALID_USERNAME.getCode())
                    .withMessage(ERROR_CODE_INVALID_USERNAME.getMessage()).build());
        }

        if (StringUtils.isNotEmpty(user.getUserStoreDomain())) {
            realm = user.getUserStoreDomain();
        }

        org.wso2.carbon.identity.application.common.model.User constructedUser = new org.wso2.carbon.identity.
                application.common.model.User();
        constructedUser.setUserName(username);
        constructedUser.setUserStoreDomain(realm);
        constructedUser.setTenantDomain(tenantDomain);
        return constructedUser;
    }

    /**
     * @deprecated This was deprecated because the requirement is to get the absolute and relative URIs by using the
     * {@link ServiceURLBuilder} methods.
     * @since 1.1.4
     *
     * Please use {@link #buildURIForBody(String)} method to build URIs for body and
     * {@link #buildURIForHeader(String)} method to build URI for headers.
     *
     * Build URI prepending the user API context to the endpoint.
     * /t/<tenant-domain>/api/users/<endpoint>
     * @param endpoint relative endpoint path
     * @return
     */
    @Deprecated
    public static URI buildURI(String endpoint) {

        String tenantQualifiedRelativePath =
                String.format(TENANT_CONTEXT_PATH_COMPONENT, getTenantDomainFromContext()) + USER_API_PATH_COMPONENT;
        String url = tenantQualifiedRelativePath + endpoint;
        return URI.create(url);
    }

    /**
     * Builds URI prepending the user API context with the proxy context path to the endpoint.
     * Ex: /t/<tenant-domain>/api/users/<endpoint>
     *
     * @param endpoint Relative endpoint path.
     * @return Relative URI.
     */
    public static URI buildURIForBody(String endpoint) {

        String url;
        String context = getContext(endpoint);

        try {
            url = ServiceURLBuilder.create().addPath(context).build().getRelativePublicURL();
        } catch (URLBuilderException e) {
            String errorDescription = "Server encountered an error while building URL for response body.";
            throw buildInternalServerError(e, errorDescription);
        }
        return URI.create(url);
    }

    /**
     * Builds the complete URI prepending the user API context without the proxy context path, to the endpoint.
     * Ex: https://localhost:9443/t/<tenant-domain>/api/users/<endpoint>
     *
     * @param endpoint Relative endpoint path.
     * @return Fully qualified and complete URI.
     */
    public static URI buildURIForHeader(String endpoint) {

        URI loc;
        String context = getContext(endpoint);

        try {
            String url = ServiceURLBuilder.create().addPath(context).build().getAbsolutePublicURL();
            loc = URI.create(url);
        } catch (URLBuilderException e) {
            String errorDescription = "Server encountered an error while building URL for response header.";
            throw buildInternalServerError(e, errorDescription);
        }
        return loc;
    }

    /**
     * Builds the API context on whether the tenant qualified url is enabled or not. In tenant qualified mode the
     * ServiceURLBuilder appends the tenant domain to the URI as a path param automatically. But
     * in non tenant qualified mode we need to append the tenant domain to the path manually.
     *
     * @param endpoint Relative endpoint path.
     * @return Context of the API.
     */
    private static String getContext(String endpoint) {

        String context;
        if (IdentityTenantUtil.isTenantQualifiedUrlsEnabled()) {
            context = USER_API_PATH_COMPONENT + endpoint;
        } else {
            context = String.format(TENANT_CONTEXT_PATH_COMPONENT, getTenantDomainFromContext()) +
                    USER_API_PATH_COMPONENT + endpoint;
        }
        return context;
    }

    /**
     * Builds APIError to be thrown if the URL building fails.
     *
     * @param e Exception occurred while building the URL.
     * @param errorDescription Description of the error.
     * @return APIError object which contains the error description.
     */
    private static APIError buildInternalServerError(Exception e, String errorDescription) {

        String errorCode = UNEXPECTED_SERVER_ERROR.getCode();
        String errorMessage = "Error while building response.";

        ErrorResponse errorResponse = new ErrorResponse.Builder().
                withCode(errorCode)
                .withMessage(errorMessage)
                .withDescription(errorDescription)
                .build(LOG, e, errorDescription);

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        return new APIError(status, errorResponse);
    }


    /**
     * Retrieves authenticated username from carbon context.
     *
     * @return username of the authenticated user.
     */
    public static String getUserIdFromContext() {

        try {
            UserRealm userRealm = CarbonContext.getThreadLocalCarbonContext().getUserRealm();
            AbstractUserStoreManager userStoreManager = (AbstractUserStoreManager) userRealm.getUserStoreManager();

            if (userStoreManager == null) {
                throw new APIError(Response.Status.INTERNAL_SERVER_ERROR,
                        new ErrorResponse.Builder().withDescription("Error occured while resolving the user.")
                                .withCode(ERROR_CODE_SERVER_ERROR.getCode())
                                .withMessage(ERROR_CODE_SERVER_ERROR.getMessage()).build());
            }
            return userStoreManager.getUserIDFromUserName(getUsernameFromContext());
        } catch (UserStoreException e) {
            throw new APIError(Response.Status.INTERNAL_SERVER_ERROR,
                    new ErrorResponse.Builder().withDescription("Error occured while resolving the user.")
                            .withCode(ERROR_CODE_SERVER_ERROR.getCode())
                            .withMessage(ERROR_CODE_SERVER_ERROR.getMessage()).build());
        }
    }
}
