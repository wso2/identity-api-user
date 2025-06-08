/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.association.v1.impl;

import org.wso2.carbon.identity.rest.api.user.association.v1.FederatedAssociationsApiService;
import org.wso2.carbon.identity.rest.api.user.association.v1.core.UserAssociationService;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.BulkFederatedAssociationRequestDTO;
import org.wso2.carbon.identity.rest.api.user.association.v1.factories.UserAssociationServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Implementation of FederatedAssociationsApi Service.
 */
public class FederatedAssociationsApiServiceImpl extends FederatedAssociationsApiService {

    private final UserAssociationService userAssociationService;

    public FederatedAssociationsApiServiceImpl() {

        try {
            this.userAssociationService = UserAssociationServiceFactory.getUserAssociationService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating UserAssociationService.", e);
        }
    }

    @Override
    public Response federatedAssociationsBulkPost(BulkFederatedAssociationRequestDTO bulkFederatedAssociationRequest) {

        return Response.ok()
                .entity(userAssociationService.handleBulkFederatedAssociations(bulkFederatedAssociationRequest))
                .build();
    }
}
