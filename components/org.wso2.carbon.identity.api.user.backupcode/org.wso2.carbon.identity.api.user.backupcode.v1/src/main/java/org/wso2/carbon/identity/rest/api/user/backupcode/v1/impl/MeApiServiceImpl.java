/*
 * Copyright (c) 2022-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.backupcode.v1.impl;

import org.wso2.carbon.identity.rest.api.user.backupcode.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.backupcode.v1.core.BackupCodeService;
import org.wso2.carbon.identity.rest.api.user.backupcode.v1.factories.BackupCodeServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Implementation of MeApi Service.
 */
public class MeApiServiceImpl extends MeApiService {

    private final BackupCodeService backupCodeService;

    public MeApiServiceImpl() {

        backupCodeService = BackupCodeServiceFactory.getBackupCodeService();
    }

    @Override
    public Response meBackupCodesDelete() {

        backupCodeService.deleteBackupCodes();
        return Response.ok().build();
    }

    @Override
    public Response meBackupCodesGet() {

        return Response.ok().entity(backupCodeService.getBackupCodes()).build();
    }

    @Override
    public Response meBackupCodesPost() {

        return Response.ok().entity(backupCodeService.initBackupCodes()).build();
    }
}
