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

package org.wso2.carbon.identity.rest.api.user.recovery.v1;

import org.wso2.carbon.identity.rest.api.user.recovery.v1.*;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.*;

import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.ErrorResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.ResetCodeResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.ConfirmRequestDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.InitRequestDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.AccountRecoveryTypeDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.PasswordRecoveryInternalNotifyResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.RecoveryRequestDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.PasswordRecoveryExternalNotifyResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.UsernameRecoveryInternalNotifyResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.UsernameRecoveryExternalNotifyResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.ResendConfirmationRequestDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.ResendConfirmationCodeResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.RetryErrorResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.PasswordResetResponseDTO;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.ResetRequestDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import javax.ws.rs.core.Response;

public abstract class RecoveryApiService {

    public abstract Response confirmRecovery(ConfirmRequestDTO confirmRequest);

    public abstract Response initiatePasswordRecovery(InitRequestDTO initRequest);

    public abstract Response initiateUsernameRecovery(InitRequestDTO initRequest);

    public abstract Response recoverPassword(RecoveryRequestDTO recoveryRequest);

    public abstract Response recoverUsername(RecoveryRequestDTO recoveryRequest);

    public abstract Response resendConfirmation(ResendConfirmationRequestDTO resendConfirmationRequest);

    public abstract Response resetPassword(ResetRequestDTO resetRequest);

}
