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
package org.wso2.carbon.identity.api.user.recovery.commons;

import org.wso2.carbon.identity.recovery.services.password.PasswordRecoveryManager;
import org.wso2.carbon.identity.recovery.services.username.UsernameRecoveryManager;

/**
 * Service holder class for user account recovery.
 */
public class UserAccountRecoveryServiceDataHolder {

    private static UsernameRecoveryManager usernameRecoveryManager;
    private static PasswordRecoveryManager passwordRecoveryManager;

    /**
     * Get UsernameRecoveryManager instance.
     *
     * @return UsernameRecoveryManager
     */
    public static UsernameRecoveryManager getUsernameRecoveryManager() {

        return UserAccountRecoveryServiceDataHolder.usernameRecoveryManager;
    }

    /**
     * Set UsernameRecoveryManager instance.
     *
     * @param usernameRecoveryManager UsernameRecoveryManager
     */
    public static void setUsernameRecoveryManager(UsernameRecoveryManager usernameRecoveryManager) {

        UserAccountRecoveryServiceDataHolder.usernameRecoveryManager = usernameRecoveryManager;
    }

    /**
     * Get PasswordRecoveryManager instance.
     *
     * @return PasswordRecoveryManager
     */
    public static PasswordRecoveryManager getPasswordRecoveryManager() {

        return UserAccountRecoveryServiceDataHolder.passwordRecoveryManager;
    }

    /**
     * Get PasswordRecoveryManager instance.
     *
     * @param passwordRecoveryManager PasswordRecoveryManager
     */
    public static void setPasswordRecoveryManager(
            PasswordRecoveryManager passwordRecoveryManager) {

        UserAccountRecoveryServiceDataHolder.passwordRecoveryManager = passwordRecoveryManager;
    }
}
