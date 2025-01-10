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

package org.wso2.carbon.identity.rest.api.user.association.v1.util;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.user.account.association.UserAccountConnector;
import org.wso2.carbon.identity.user.profile.mgt.association.federation.FederatedAssociationManager;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Service holder class for User Associations.
 */
public class UserAssociationServiceHolder {

    private UserAssociationServiceHolder() {

    }

    private static class FederatedAssociationManagerServiceHolder {

        static final FederatedAssociationManager SERVICE = (FederatedAssociationManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(FederatedAssociationManager.class, null);
    }

    private static class UserAccountConnectorServiceHolder {

        static final UserAccountConnector SERVICE = (UserAccountConnector) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(UserAccountConnector.class, null);
    }

    private static class RealmServiceHolder {

        static final RealmService SERVICE = (RealmService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(RealmService.class, null);
    }

    /**
     * Get FederatedAssociationManager OSGi service.
     *
     * @return FederatedAssociationManager
     */
    public static FederatedAssociationManager getFederatedAssociationManager() {

        return FederatedAssociationManagerServiceHolder.SERVICE;
    }

    /**
     * Get UserAccountConnector OSGi service.
     *
     * @return UserAccountConnector
     */
    public static UserAccountConnector getUserAccountConnector() {

        return UserAccountConnectorServiceHolder.SERVICE;
    }

    /**
     * Get RealmService OSGi service.
     *
     * @return RealmService
     */
    public static RealmService getRealmService() {

        return RealmServiceHolder.SERVICE;
    }
}
