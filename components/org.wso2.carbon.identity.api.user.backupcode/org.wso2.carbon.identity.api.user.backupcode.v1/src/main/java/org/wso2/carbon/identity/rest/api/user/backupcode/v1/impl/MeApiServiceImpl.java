/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.com) All Rights Reserved.
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
package org.wso2.carbon.identity.rest.api.user.backupcode.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.backupcode.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.backupcode.v1.core.BackupCodeService;

import org.wso2.carbon.identity.rest.api.user.backupcode.v1.dto.UserRequestDTO;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.backupcode.common.BackupCodeConstants.ErrorMessage.USER_ERROR_INVALID_ACTION_ID;

/**
 * Implementation of MeApi Service.
 */
public class MeApiServiceImpl extends MeApiService {

    @Autowired
    BackupCodeService backupCodeService;

    @Override
    public Response meBackupCodeDelete() {

        backupCodeService.deleteBackupCodes();
        return Response.ok().build();
    }

    @Override
    public Response meBackupCodeGet() {

        return Response.ok().entity(backupCodeService.getBackupCodes()).build();
    }

    @Override
    public Response meBackupCodePost(UserRequestDTO request) {

        switch (request.getAction()) {
            case INIT:
                return Response.ok().entity(backupCodeService.initBackupCodes()).build();
            case REFRESH:
                return Response.ok().entity(backupCodeService.refreshBackupCodes()).build();
            default:
                throw backupCodeService.handleInvalidInput(USER_ERROR_INVALID_ACTION_ID);
        }
    }
}
