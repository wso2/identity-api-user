/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.identity.rest.api.user.recovery.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.RecoveryApiService;

import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.ConfirmRequestDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.InitRequestDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.RecoveryRequestDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.ResendConfirmationRequestDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.ResetRequestDTO;

import org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.PasswordRecoveryService;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.impl.core.UsernameRecoveryService;

import javax.ws.rs.core.Response;

/**
 * Implementation of RecoveryApi Service.
 */
public class RecoveryApiServiceImpl extends RecoveryApiService {

    @Autowired
    UsernameRecoveryService usernameRecoveryService;

    @Autowired
    PasswordRecoveryService passwordRecoveryService;

    @Override
    public Response confirmRecovery(ConfirmRequestDTO confirmRequest) {

        return passwordRecoveryService.confirmRecovery(confirmRequest);
    }

    @Override
    public Response initiatePasswordRecovery(InitRequestDTO initRequest) {

        return passwordRecoveryService.initiatePasswordRecovery(initRequest);
    }

    @Override
    public Response initiateUsernameRecovery(InitRequestDTO initRequest) {

        return usernameRecoveryService.initiateUsernameRecovery(initRequest);
    }

    @Override
    public Response recoverPassword(RecoveryRequestDTO recoveryRequest) {

        return passwordRecoveryService.recoverPassword(recoveryRequest);
    }

    @Override
    public Response recoverUsername(RecoveryRequestDTO recoveryRequest) {

        return usernameRecoveryService.recoverUsername(recoveryRequest);
    }

    @Override
    public Response resendConfirmation(ResendConfirmationRequestDTO resendConfirmationRequest) {

        return passwordRecoveryService.resendConfirmation(resendConfirmationRequest);
    }

    @Override
    public Response resetPassword(ResetRequestDTO resetRequest) {

        return passwordRecoveryService.resetPassword(resetRequest);
    }

}
