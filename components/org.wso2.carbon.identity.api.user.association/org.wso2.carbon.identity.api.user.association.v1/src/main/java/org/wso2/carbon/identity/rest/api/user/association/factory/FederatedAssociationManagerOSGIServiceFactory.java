/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.association.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.user.profile.mgt.association.federation.FederatedAssociationManager;

/**
 * Factory Beans serves as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the FederatedAssociationManager type of object inside the container.
 */
public class FederatedAssociationManagerOSGIServiceFactory extends AbstractFactoryBean<FederatedAssociationManager> {

    private FederatedAssociationManager federatedAssociationManager;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected FederatedAssociationManager createInstance() throws Exception {

        if (this.federatedAssociationManager == null) {
            FederatedAssociationManager federatedAssociationManager
                    = (FederatedAssociationManager) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                    .getOSGiService(FederatedAssociationManager.class, null);
            if (federatedAssociationManager != null) {
                this.federatedAssociationManager = federatedAssociationManager;
            } else {
                throw new Exception("Unable to retrieve UserAccountConnector service.");
            }
        }
        return this.federatedAssociationManager;
    }
}
