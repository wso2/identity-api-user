/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.association.v1.factories;

import org.wso2.carbon.identity.rest.api.user.association.v1.core.UserAssociationService;
import org.wso2.carbon.identity.rest.api.user.association.v1.util.UserAssociationServiceHolder;
import org.wso2.carbon.identity.user.account.association.UserAccountConnector;
import org.wso2.carbon.identity.user.profile.mgt.association.federation.FederatedAssociationManager;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Factory class for User Association Service.
 */
public class UserAssociationServiceFactory {

    private UserAssociationServiceFactory() {

    }

    private static class UserAssociationServiceManagerHolder {

        private static final UserAssociationService SERVICE = createServiceInstance();
    }

    private static UserAssociationService createServiceInstance() {

        UserAccountConnector userAccountConnector = getUserAccountConnector();
        FederatedAssociationManager federatedAssociationManager = getFederatedAssociationManager();
        RealmService realmService = getRealmService();

        return new UserAssociationService(userAccountConnector, federatedAssociationManager, realmService);
    }

    /**
     * Get UserAssociationService instance.
     *
     * @return UserAssociationService instance.
     */
    public static UserAssociationService getUserAssociationService() {

        return UserAssociationServiceManagerHolder.SERVICE;
    }

    private static UserAccountConnector getUserAccountConnector() {

        UserAccountConnector service = UserAssociationServiceHolder.getUserAccountConnector();
        if (service == null) {
            throw new IllegalStateException("UserAccountConnector is not available from OSGi context.");
        }

        return service;
    }

    private static FederatedAssociationManager getFederatedAssociationManager() {

        FederatedAssociationManager service = UserAssociationServiceHolder.getFederatedAssociationManager();
        if (service == null) {
            throw new IllegalStateException("FederatedAssociationManager is not available from OSGi context.");
        }

        return service;
    }

    private static RealmService getRealmService() {

        RealmService service = UserAssociationServiceHolder.getRealmService();
        if (service == null) {
            throw new IllegalStateException("RealmService is not available from OSGi context.");
        }

        return service;
    }
}
