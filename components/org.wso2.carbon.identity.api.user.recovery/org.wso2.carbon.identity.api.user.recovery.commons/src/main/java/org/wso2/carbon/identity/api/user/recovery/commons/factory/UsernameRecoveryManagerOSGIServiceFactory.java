/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.identity.api.user.recovery.commons.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.recovery.services.username.UsernameRecoveryManager;

/**
 * This factory bean is used to instantiate UsernameRecoveryManager type object inside the container.
 */
public class UsernameRecoveryManagerOSGIServiceFactory extends AbstractFactoryBean<UsernameRecoveryManager> {

    private UsernameRecoveryManager usernameRecoveryManager;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected UsernameRecoveryManager createInstance() throws Exception {

        if (this.usernameRecoveryManager == null) {
            UsernameRecoveryManager usernameRecoveryManager = (UsernameRecoveryManager) PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiService(UsernameRecoveryManager.class, null);
            if (usernameRecoveryManager != null) {
                this.usernameRecoveryManager = usernameRecoveryManager;
            } else {
                throw new Exception("Unable to retrieve UsernameRecoveryManager service.");
            }
        }
        return this.usernameRecoveryManager;
    }
}
