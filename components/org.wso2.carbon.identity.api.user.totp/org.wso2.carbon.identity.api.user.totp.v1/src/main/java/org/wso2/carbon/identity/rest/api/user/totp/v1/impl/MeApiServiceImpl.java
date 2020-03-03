/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.identity.rest.api.user.totp.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.totp.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.totp.v1.core.TOTPService;
import org.wso2.carbon.identity.rest.api.user.totp.v1.dto.UserRequestDTO;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.totp.common.TOTPConstants.ErrorMessage.USER_ERROR_INVALID_ACTION_ID;
import static org.wso2.carbon.identity.api.user.totp.common.TOTPConstants.ErrorMessage
        .USER_ERROR_INVALID_VALIDATION_PARAMS;

/**
 * Implementation class of TOTP API.
 */
public class MeApiServiceImpl extends MeApiService {

    @Autowired
    TOTPService totpService;

    @Override
    public Response meTotpDelete() {

        totpService.resetTOTP();
        return Response.ok().build();
    }

    @Override
    public Response meTotpGet() {

        return Response.ok().entity(totpService.getQRUrlCode()).build();
    }

    @Override
    public Response meTotpPost(UserRequestDTO request) {

        switch (request.getAction()) {
            case INIT:
                return Response.ok().entity(totpService.initTOTP()).build();
            case REFRESH:
                return Response.ok().entity(totpService.refreshSecretKey()).build();
            case VALIDATE:
                if (request.getVerificationCode() == null) {
                    throw totpService.handleInvalidInput(USER_ERROR_INVALID_VALIDATION_PARAMS);
                }
                return Response.ok().entity(totpService.validateTOTP(request.getVerificationCode())).build();
            default:
                throw totpService.handleInvalidInput(USER_ERROR_INVALID_ACTION_ID);
        }
    }

    @Override
    public Response meTotpSecretGet() {

        return Response.ok().entity(totpService.getSecretKey()).build();
    }
}
