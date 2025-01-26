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

package org.wso2.carbon.identity.api.user.onboard.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.onboard.v1.OfflineInviteLinkApiService;
import org.wso2.carbon.identity.api.user.onboard.v1.factories.OfflineInviteLinkServiceFactory;
import org.wso2.carbon.identity.api.user.onboard.v1.model.InvitationRequest;
import org.wso2.carbon.identity.api.user.onboard.v1.service.OfflineInviteLinkService;

import java.net.URI;

import javax.ws.rs.core.Response;

/**
 * This API is used to generate a random link that can be used to set a new password.
 */
public class OfflineInviteLinkApiServiceImpl implements OfflineInviteLinkApiService {

    private static final Log LOG = LogFactory.getLog(OfflineInviteLinkApiServiceImpl.class);
    private final OfflineInviteLinkService offlineInviteLinkService;

    public OfflineInviteLinkApiServiceImpl() {

        try {
            this.offlineInviteLinkService = OfflineInviteLinkServiceFactory.getOfflineInviteLinkService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating required services for " +
                    "OfflineInviteLinkService.", e);
        }
    }

    @Override
    public Response generateLink(InvitationRequest invitationRequest) {

        String generatedLink = offlineInviteLinkService.generatePasswordURL(invitationRequest);
        return Response.created(URI.create(generatedLink)).entity(generatedLink).build();
    }
}
