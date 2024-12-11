/*
 * Copyright (c) 2020-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.user.recovery.commons;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.recovery.services.password.PasswordRecoveryManager;
import org.wso2.carbon.identity.recovery.services.username.UsernameRecoveryManager;

/**
 * Service holder class for user account recovery.
 */
public class UserAccountRecoveryServiceDataHolder {

    private UserAccountRecoveryServiceDataHolder () {

    }

    private static class UsernameRecoveryManagerHolder {

        static final UsernameRecoveryManager SERVICE = (UsernameRecoveryManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(UsernameRecoveryManager.class, null);
    }

    private static class PasswordRecoveryManagerHolder {

        static final PasswordRecoveryManager SERVICE = (PasswordRecoveryManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(PasswordRecoveryManager.class, null);
    }

    /**
     * Get UsernameRecoveryManager instance.
     *
     * @return UsernameRecoveryManager
     */
    public static UsernameRecoveryManager getUsernameRecoveryManager() {

        return UsernameRecoveryManagerHolder.SERVICE;
    }

    /**
     * Get PasswordRecoveryManager instance.
     *
     * @return PasswordRecoveryManager
     */
    public static PasswordRecoveryManager getPasswordRecoveryManager() {

        return PasswordRecoveryManagerHolder.SERVICE;
    }
}
