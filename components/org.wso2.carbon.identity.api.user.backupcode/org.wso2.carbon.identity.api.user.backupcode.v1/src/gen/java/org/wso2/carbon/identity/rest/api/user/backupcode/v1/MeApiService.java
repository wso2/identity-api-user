/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.backupcode.v1;

import javax.ws.rs.core.Response;

/**
 * Service class for me endpoint of backup code authenticator.
 */
public abstract class MeApiService {

    /**
     * Delete backup codes.
     *
     * @return API response.
     */
    public abstract Response meBackupCodesDelete();

    /**
     * Retrieve backup codes.
     *
     * @return API response.
     */
    public abstract Response meBackupCodesGet();

    /**
     * Generate/Refresh backup codes.
     *
     * @return API response.
     */
    public abstract Response meBackupCodesPost();

}
