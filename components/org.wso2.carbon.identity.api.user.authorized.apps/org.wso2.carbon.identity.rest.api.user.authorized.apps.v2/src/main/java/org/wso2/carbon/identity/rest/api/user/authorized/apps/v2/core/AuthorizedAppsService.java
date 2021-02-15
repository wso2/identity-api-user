/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.core;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.user.common.function.UserToUniqueId;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;
import org.wso2.carbon.identity.oauth.OAuthAdminServiceImpl;
import org.wso2.carbon.identity.oauth.common.OAuthConstants;
import org.wso2.carbon.identity.oauth.dto.OAuthConsumerAppDTO;
import org.wso2.carbon.identity.oauth.dto.OAuthRevocationRequestDTO;
import org.wso2.carbon.identity.oauth.dto.OAuthRevocationResponseDTO;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2ScopeConsentException;
import org.wso2.carbon.identity.oauth2.OAuth2ScopeService;
import org.wso2.carbon.identity.oauth2.internal.OAuth2ServiceComponentHolder;
import org.wso2.carbon.identity.oauth2.model.OAuth2ScopeConsentResponse;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v2.dto.AuthorizedAppDTO;
import org.wso2.carbon.user.core.UserCoreConstants;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Class with the core implementation of authorized application services.
 */
public class AuthorizedAppsService {

    private static final Log log = LogFactory.getLog(AuthorizedAppsService.class);
    private static final ApplicationManagementService applicationManagementService;
    private static final OAuthAdminServiceImpl oAuthAdminService;
    private static final OAuth2ScopeService oAuth2ScopeService;
    private static final RealmService realmService;


    static {
        applicationManagementService = (ApplicationManagementService) PrivilegedCarbonContext.
                getThreadLocalCarbonContext().getOSGiService(ApplicationManagementService.class, null);
        oAuthAdminService = (OAuthAdminServiceImpl) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(OAuthAdminServiceImpl.class, null);
        oAuth2ScopeService = (OAuth2ScopeService) PrivilegedCarbonContext.
                getThreadLocalCarbonContext().getOSGiService(OAuth2ScopeService.class, null);
        realmService = (RealmService) PrivilegedCarbonContext.
                getThreadLocalCarbonContext().getOSGiService(RealmService.class, null);
    }

    /**
     * Delete the authorized application for the user by the given application id.
     *
     * @param user          User.
     * @param applicationId Application Id.
     */
    public void deleteUserAuthorizedApps(User user, String applicationId) {

        String applicationName = getApplicationName(user, applicationId);
        OAuthRevocationRequestDTO oAuthRevocationRequestDTO = new OAuthRevocationRequestDTO();
        oAuthRevocationRequestDTO.setApps(new String[]{applicationName});
        try {
            startTenantFlowWithUser(getUsernameWithUserStoreDomain(user), user.getTenantDomain());
            OAuthConsumerAppDTO[] appsAuthorizedByUser = oAuthAdminService.getAppsAuthorizedByUser();
            Optional<OAuthConsumerAppDTO> first = Arrays.stream(appsAuthorizedByUser)
                    .filter(oAuthConsumerAppDTO -> oAuthConsumerAppDTO.getApplicationName().equals(applicationName))
                    .findFirst();
            String userId = getUserIdFromUser(user);
            oAuth2ScopeService.revokeUserConsentForApplication(userId, applicationId,
                    IdentityTenantUtil.getTenantId(user.getTenantDomain()));
            if (!first.isPresent()) {
                throw handleError(NOT_FOUND, Constants.ErrorMessages.ERROR_CODE_INVALID_APPLICATION_ID, applicationId,
                        user.toFullQualifiedUsername());
            }
            OAuthRevocationResponseDTO oAuthRevocationResponseDTO = oAuthAdminService
                    .revokeAuthzForAppsByResourceOwner(oAuthRevocationRequestDTO);
            if (!oAuthRevocationResponseDTO.isError()) {
                //TODO: Handle
                log.warn("Given application: " + applicationId + " has been deleted by a PreRevokeListener.");
            }
        } catch (IdentityOAuthAdminException | IdentityOAuth2ScopeConsentException e) {
            throw handleError(INTERNAL_SERVER_ERROR, Constants.ErrorMessages.ERROR_CODE_REVOKE_APP_BY_ID_BY_USER,
                    applicationId, user.toFullQualifiedUsername());
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
    }

    /**
     * Delete all the authorized application for the user.
     *
     * @param user User.
     */
    public void deleteUserAuthorizedApps(User user) {

        OAuthRevocationRequestDTO oAuthRevocationRequestDTO = new OAuthRevocationRequestDTO();
        try {
            startTenantFlowWithUser(getUsernameWithUserStoreDomain(user), user.getTenantDomain());
            List<AuthorizedAppDTO> authorizedAppDTOS = listUserAuthorizedApps(user);
            List<String> allAuthorizedApps = authorizedAppDTOS.stream().map(AuthorizedAppDTO::getName)
                    .collect(Collectors.toList());
            oAuthRevocationRequestDTO.setApps(allAuthorizedApps.toArray(new String[0]));
            OAuthRevocationResponseDTO oAuthRevocationResponseDTO = oAuthAdminService
                    .revokeAuthzForAppsByResourceOwner(oAuthRevocationRequestDTO);
            String userId = getUserIdFromUser(user);
            oAuth2ScopeService.revokeUserConsents(userId, IdentityTenantUtil.getTenantId(user.getTenantDomain()));
            if (!oAuthRevocationResponseDTO.isError()) {
                //TODO: Handle
                log.warn("No applications can be found for the user: " + user.getUserName());

            }
        } catch (IdentityOAuthAdminException |  IdentityOAuth2ScopeConsentException e) {
            throw handleError(INTERNAL_SERVER_ERROR, Constants.ErrorMessages.ERROR_CODE_REVOKE_APP_BY_USER,
                    user.toFullQualifiedUsername());
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
    }

    /**
     * List the authorized application for the user by the given application id.
     *
     * @param user          User.
     * @param applicationId Application Id.
     * @return AuthorizedAppDTO.
     */
    public AuthorizedAppDTO listUserAuthorizedAppsByAppId(User user, String applicationId) {

        String applicationName = getApplicationName(user, applicationId);
        AuthorizedAppDTO authorizedAppDTO;
        try {
            startTenantFlowWithUser(getUsernameWithUserStoreDomain(user), user.getTenantDomain());
            OAuthConsumerAppDTO[] appsAuthorizedByUser = oAuthAdminService.getAppsAuthorizedByUser();
            Optional<OAuthConsumerAppDTO> authConsumerAppDTO = Arrays.stream(appsAuthorizedByUser)
                    .filter(oAuthConsumerAppDTO -> oAuthConsumerAppDTO.getApplicationName().equals(applicationName))
                    .findFirst();
            if (authConsumerAppDTO.isPresent()) {
                String clientKey = authConsumerAppDTO.get().getOauthConsumerKey();
                String resourceId = getApplicationResourceIdByClientId(clientKey, user.getTenantDomain());
                String userId = getUserIdFromUser(user);
                OAuth2ScopeConsentResponse oAuth2ScopeConsentResponse =
                        oAuth2ScopeService.getUserConsentForApp(userId, resourceId,
                                IdentityTenantUtil.getTenantId(user.getTenantDomain()));
                authorizedAppDTO = buildAuthorizedAppDTO(resourceId, authConsumerAppDTO.get(),
                        oAuth2ScopeConsentResponse);
            } else {
                throw handleError(NOT_FOUND, Constants.ErrorMessages.ERROR_CODE_INVALID_APPLICATION_ID, applicationId,
                        user.toFullQualifiedUsername());
            }
        } catch (IdentityOAuthAdminException | IdentityOAuth2ScopeConsentException e) {
            throw handleError(INTERNAL_SERVER_ERROR, Constants.ErrorMessages.ERROR_CODE_GET_APP_BY_ID_BY_USER,
                    applicationId, user.toFullQualifiedUsername());
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
        return authorizedAppDTO;
    }

    /**
     * Get the authorized applications list for the user.
     *
     * @param user User.
     * @return List of authorized applications list for the user.
     */
    public List<AuthorizedAppDTO> listUserAuthorizedApps(User user) {

        List<AuthorizedAppDTO> authorizedAppDTOS = new ArrayList<>();
        try {
            startTenantFlowWithUser(getUsernameWithUserStoreDomain(user), user.getTenantDomain());
            OAuthConsumerAppDTO[] appsAuthorizedByUser = oAuthAdminService.getAppsAuthorizedByUser();
            if (ArrayUtils.isEmpty(appsAuthorizedByUser)) {
                return Collections.emptyList();
            }
            for (OAuthConsumerAppDTO authConsumerAppDTO : appsAuthorizedByUser) {
                String clientKey = authConsumerAppDTO.getOauthConsumerKey();
                String resourceId = getApplicationResourceIdByClientId(clientKey, user.getTenantDomain());
                String userId = getUserIdFromUser(user);
                OAuth2ScopeConsentResponse oAuth2ScopeConsentResponse =
                        oAuth2ScopeService.getUserConsentForApp(userId, resourceId,
                        IdentityTenantUtil.getTenantId(user.getTenantDomain()));
                authorizedAppDTOS.add(buildAuthorizedAppDTO(resourceId, authConsumerAppDTO,
                        oAuth2ScopeConsentResponse));
            }
        } catch (IdentityOAuthAdminException | IdentityOAuth2ScopeConsentException e) {
            throw handleError(Response.Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessages.ERROR_CODE_GET_APP_BY_USER,
                    user.toFullQualifiedUsername());
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
        return authorizedAppDTOS;
    }

    private static APIError handleError(Response.Status status, Constants.ErrorMessages error, String... data) {

        String description;
        if (data != null) {
            description = String.format(error.getDescription(), (Object[]) data);
        } else {
            description = error.getMessage();
        }
        return new APIError(status, new ErrorResponse.Builder().withCode(error.getCode())
                .withMessage(error.getMessage())
                .withDescription(description).build());
    }

    private void startTenantFlowWithUser(String subject, String subjectTenantDomain) {

        startTenantFlow(subjectTenantDomain);
        PrivilegedCarbonContext.getThreadLocalCarbonContext().setUsername(subject);
    }

    private void startTenantFlow(String tenantDomain) {

        PrivilegedCarbonContext.startTenantFlow();
        PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain(tenantDomain, true);
    }

    private String getUsernameWithUserStoreDomain(User user) {

        return user.getUserStoreDomain() + UserCoreConstants.DOMAIN_SEPARATOR + user.getUserName();
    }

    private String getApplicationName(User user, String resourceId) {

        try {
            ServiceProvider serviceProvider = applicationManagementService.getApplicationByResourceId(resourceId,
                    user.getTenantDomain());
            if (serviceProvider == null) {
                throw handleError(Response.Status.NOT_FOUND,
                        Constants.ErrorMessages.ERROR_CODE_INVALID_APPLICATION_ID,
                        resourceId, user.toFullQualifiedUsername());
            }
            return serviceProvider.getApplicationName();
        } catch (IdentityApplicationManagementException e) {
            throw handleError(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessages.ERROR_CODE_GETTING_APPLICATION_INFORMATION, resourceId);
        }
    }

    private String getApplicationResourceIdByClientId(String clientId, String spTenantDomain)
            throws IdentityOAuthAdminException {

        try {
            ServiceProvider serviceProvider = OAuth2ServiceComponentHolder.getApplicationMgtService().
                    getServiceProviderByClientId(clientId, OAuthConstants.Scope.OAUTH2, spTenantDomain);
            return serviceProvider.getApplicationResourceId();
        } catch (IdentityApplicationManagementException e) {
            throw new IdentityOAuthAdminException("Error while retrieving the app information", e);
        }
    }

    private AuthorizedAppDTO buildAuthorizedAppDTO(String resourceId, OAuthConsumerAppDTO consumerAppDTO,
                                                   OAuth2ScopeConsentResponse oAuth2ScopeConsentResponse) {

        AuthorizedAppDTO authorizedAppDTO = new AuthorizedAppDTO();
        authorizedAppDTO.setId(resourceId);
        authorizedAppDTO.setName(consumerAppDTO.getApplicationName());
        authorizedAppDTO.setClientId(consumerAppDTO.getOauthConsumerKey());
        authorizedAppDTO.approvedScopes(oAuth2ScopeConsentResponse.getApprovedScopes());
        return authorizedAppDTO;
    }

    private String getUserIdFromUser(User user) {

        return new UserToUniqueId().apply(realmService, user);
    }
}
