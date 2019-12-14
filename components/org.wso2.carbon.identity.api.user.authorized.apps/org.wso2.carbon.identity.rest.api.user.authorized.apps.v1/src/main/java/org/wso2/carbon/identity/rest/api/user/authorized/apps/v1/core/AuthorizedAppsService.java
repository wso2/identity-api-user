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

package org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;
import org.wso2.carbon.identity.oauth.OAuthAdminServiceImpl;
import org.wso2.carbon.identity.oauth.dto.OAuthConsumerAppDTO;
import org.wso2.carbon.identity.oauth.dto.OAuthRevocationRequestDTO;
import org.wso2.carbon.identity.oauth.dto.OAuthRevocationResponseDTO;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.core.functions.OAuthConsumerAppToExternal;
import org.wso2.carbon.identity.rest.api.user.authorized.apps.v1.dto.AuthorizedAppDTO;
import org.wso2.carbon.user.core.UserCoreConstants;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response.Status;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * This class performs the operations related to user authorized OAuth apps by invoking {@link OAuthAdminServiceImpl}
 * service.
 */
public class AuthorizedAppsService {

    private static final Log log = LogFactory.getLog(AuthorizedAppsService.class);
    private static OAuthAdminServiceImpl oAuthAdminService = null;
    private static RealmService realmService = null;

    static {
        oAuthAdminService = (OAuthAdminServiceImpl) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(OAuthAdminServiceImpl.class, null);
        realmService = (RealmService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(RealmService.class, null);
    }

    public void deleteUserAuthorizedApps(User user) {

        OAuthRevocationRequestDTO oAuthRevocationRequestDTO = new OAuthRevocationRequestDTO();

        try {
            startTenantFlowWithUser(getUsernameWithUserStoreDomain(user), user.getTenantDomain());
            List<AuthorizedAppDTO> authorizedAppDTOS = listUserAuthorizedApps(user);
            List<String> allAuthorizedApps = authorizedAppDTOS.stream().map(AuthorizedAppDTO::getAppId)
                    .collect(Collectors.toList());
            oAuthRevocationRequestDTO.setApps(allAuthorizedApps.toArray(new String[0]));
            OAuthRevocationResponseDTO oAuthRevocationResponseDTO = oAuthAdminService
                    .revokeAuthzForAppsByResourceOwner(oAuthRevocationRequestDTO);

            if (!oAuthRevocationResponseDTO.isError()) {
                //TODO: Handle
                log.warn("No applications can be found for the user: " + user.getUserName());

            }
        } catch (IdentityOAuthAdminException e) {
            throw handleError(Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessages.ERROR_CODE_REVOKE_APP_BY_USER,
                    user.toFullQualifiedUsername());
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
    }

    public void deleteUserAuthorizedApps(User user, String applicationId) {

        OAuthRevocationRequestDTO oAuthRevocationRequestDTO = new OAuthRevocationRequestDTO();
        oAuthRevocationRequestDTO.setApps(new String[] { applicationId });
        try {
            startTenantFlowWithUser(getUsernameWithUserStoreDomain(user), user.getTenantDomain());

            OAuthConsumerAppDTO[] appsAuthorizedByUser = oAuthAdminService.getAppsAuthorizedByUser();
            Optional<OAuthConsumerAppDTO> first = Arrays.stream(appsAuthorizedByUser)
                    .filter(oAuthConsumerAppDTO -> oAuthConsumerAppDTO.getApplicationName().equals(applicationId))
                    .findFirst();
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
        } catch (IdentityOAuthAdminException e) {
            throw handleError(Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessages.ERROR_CODE_REVOKE_APP_BY_ID_BY_USER,
                    applicationId, user.toFullQualifiedUsername());
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
    }

    private String getUsernameWithUserStoreDomain(User user) {
        return user.getUserStoreDomain() + UserCoreConstants.DOMAIN_SEPARATOR + user.getUserName();
    }

    public List<AuthorizedAppDTO> listUserAuthorizedApps(User user) {

        List<AuthorizedAppDTO> authorizedAppDTOS;
        try {
            startTenantFlowWithUser(getUsernameWithUserStoreDomain(user), user.getTenantDomain());
            OAuthConsumerAppDTO[] appsAuthorizedByUser = oAuthAdminService.getAppsAuthorizedByUser();
            authorizedAppDTOS = Arrays.stream(appsAuthorizedByUser).map(new OAuthConsumerAppToExternal())
                    .collect(Collectors.toList());

        } catch (IdentityOAuthAdminException e) {
            throw handleError(Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessages.ERROR_CODE_GET_APP_BY_USER,
                    user.toFullQualifiedUsername());
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
        return authorizedAppDTOS;
    }

    public AuthorizedAppDTO listUserAuthorizedAppsByAppId(User user, String applicationId) {

        AuthorizedAppDTO authorizedAppDTO;
        try {
            startTenantFlowWithUser(getUsernameWithUserStoreDomain(user), user.getTenantDomain());
            OAuthConsumerAppDTO[] appsAuthorizedByUser = oAuthAdminService.getAppsAuthorizedByUser();
            Optional<OAuthConsumerAppDTO> first = Arrays.stream(appsAuthorizedByUser)
                    .filter(oAuthConsumerAppDTO -> oAuthConsumerAppDTO.getApplicationName().equals(applicationId))
                    .findFirst();
            if (first.isPresent()) {
                authorizedAppDTO = new OAuthConsumerAppToExternal().apply(first.get());
            } else {
                throw handleError(NOT_FOUND, Constants.ErrorMessages.ERROR_CODE_INVALID_APPLICATION_ID, applicationId,
                        user.toFullQualifiedUsername());
            }
        } catch (IdentityOAuthAdminException e) {
            throw handleError(Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessages.ERROR_CODE_GET_APP_BY_ID_BY_USER,
                    applicationId, user.toFullQualifiedUsername());
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
        return authorizedAppDTO;
    }

    private void startTenantFlowWithUser(String subject, String subjectTenantDomain) {

        startTenantFlow(subjectTenantDomain);
        PrivilegedCarbonContext.getThreadLocalCarbonContext().setUsername(subject);
    }

    private void startTenantFlow(String tenantDomain) {

        PrivilegedCarbonContext.startTenantFlow();
        PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain(tenantDomain, true);
    }

    private static APIError handleError(Status status, Constants.ErrorMessages error, String... data) {

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
}
