/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.recovery.v2.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.RecoveryApiService;

import org.wso2.carbon.identity.rest.api.user.recovery.v2.impl.core.PasswordRecoveryService;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.impl.core.UsernameRecoveryService;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.ConfirmRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.InitRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.RecoveryRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.ResendConfirmationRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.ResetRequest;

import javax.ws.rs.core.Response;

/**
 * Implementation of RecoveryApi Service.
 */
public class RecoveryApiServiceImpl implements RecoveryApiService {

    @Autowired
    private UsernameRecoveryService usernameRecoveryService;

    @Autowired
    private PasswordRecoveryService passwordRecoveryService;

    @Override
    public Response confirmRecovery(ConfirmRequest confirmRequest) {

        return passwordRecoveryService.confirmRecovery(confirmRequest);
    }

    @Override
    public Response initiatePasswordRecovery(InitRequest initRequest) {

        return passwordRecoveryService.initiatePasswordRecovery(initRequest);
    }

    @Override
    public Response initiateUsernameRecovery(InitRequest initRequest) {

        return usernameRecoveryService.initiateUsernameRecovery(initRequest);
    }

    @Override
    public Response recoverPassword(RecoveryRequest recoveryRequest) {

        return passwordRecoveryService.recoverPassword(recoveryRequest);
    }

    @Override
    public Response recoverUsername(RecoveryRequest recoveryRequest) {

        return usernameRecoveryService.recoverUsername(recoveryRequest);
    }

    @Override
    public Response resendConfirmation(ResendConfirmationRequest resendConfirmationRequest) {

        return passwordRecoveryService.resendConfirmation(resendConfirmationRequest);
    }

    @Override
    public Response resetPassword(ResetRequest resetRequest) {

        return passwordRecoveryService.resetPassword(resetRequest);
    }
}
