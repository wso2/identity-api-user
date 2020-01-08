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
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.AccountRecoveryType;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.ConfirmRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.ErrorResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.InitRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.PasswordRecoveryExternalNotifyResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.PasswordRecoveryInternalNotifyResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.PasswordResetResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.RecoveryRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.ResendConfirmationCodeResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.ResendConfirmationRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.ResetCodeResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.ResetRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.RetryErrorResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.UsernameRecoveryExternalNotifyResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.model.UsernameRecoveryInternalNotifyResponse;
import javax.ws.rs.core.Response;


public interface RecoveryApiService {

      public Response confirmRecovery(ConfirmRequest confirmRequest);

      public Response initiatePasswordRecovery(InitRequest initRequest);

      public Response initiateUsernameRecovery(InitRequest initRequest);

      public Response recoverPassword(RecoveryRequest recoveryRequest);

      public Response recoverUsername(RecoveryRequest recoveryRequest);

      public Response resendConfirmation(ResendConfirmationRequest resendConfirmationRequest);

      public Response resetPassword(ResetRequest resetRequest);
}
