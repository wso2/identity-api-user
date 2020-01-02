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

package org.wso2.carbon.identity.rest.api.user.recovery.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.dto.*;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.RecoveryApiService;
import org.wso2.carbon.identity.rest.api.user.recovery.v1.factories.RecoveryApiServiceFactory;

import io.swagger.annotations.ApiParam;

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
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/recovery")
@Consumes({ "application/json" })
@Produces({ "application/json" })
@io.swagger.annotations.Api(value = "/recovery", description = "the recovery API")
public class RecoveryApi  {

    @Autowired
    private RecoveryApiService delegate;

    @Valid
    @POST
    @Path("/password/confirm")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Confirm password recovery\n",
            notes = "- This API is used to validate the __confirmationCode__ given at password recovery.\n- Use the returned __reset code__ with the __password reset API__ to reset the password.\n- The API will return the next API call.\n",
            response = ResetCodeResponseDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "- Password reset confirmed.\n- `resetCode` is returned.\n"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request. Request cannot be processed by the server."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Confirmation code is not found."),
        
        @io.swagger.annotations.ApiResponse(code = 406, message = "Confirmation code given in the request is not valid or expired.\n"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error.") })

    public Response confirmRecovery(@ApiParam(value = "- Request to confirm the password recovery.\n- `confirmationCode` is `REQUIRED`.\n" ,required=true ) @Valid ConfirmRequestDTO confirmRequest) {

        return delegate.confirmRecovery(confirmRequest);
    }

    @Valid
    @POST
    @Path("/password/init")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Initiate password recovery\n",
            notes = "- This API is used to initiate password recovery by user. The API will return recovery information for password recovery with challenge questions and password recovery with notifications.\n- `mode` __recoverWithNotifications__ contains available communication channels and a recovery code for password recovery with notifications. The next API call will be returned in the response.\n- If `password recovery with notifications` is not enabled, the response will not contain __recoverWithNotifications__ mode.\n- __recoverWithChallengeQuestions__ contains the next API call to begin the recovery process via challenge questions.\n- If `password recovery with challenge questions` is not enabled, the response will not contain __recoverWithChallengeQuestions__ mode.\n",
            response = AccountRecoveryTypeDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "User is successfully identified for the given claims and returning available notification channels for the user."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request. The request cannot be processed by the server."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "No user found for the given set of claims or no recovery channels are availabe for the user."),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Mutiple users found for the given claims."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error.") })

    public Response initiatePasswordRecovery(@ApiParam(value = "Request to initate password recovery process. The request should contain the claims to identify the user. User claims are `REQUIRED`." ,required=true ) @Valid InitRequestDTO initRequest) {

        return delegate.initiatePasswordRecovery(initRequest);
    }

    @Valid
    @POST
    @Path("/username/init")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Initiate Username Recovery",
            notes = "- This API is used to initiate username recovery by user. The API will return available recovery information for username recovery with notifications.\n- Use the returned __recoveryCode__ and the __channelId__ with __username recover api__ to confirm the the username recovery and to recieve recovery notifications via the channel specified by the user.\n",
            response = AccountRecoveryTypeDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "User is successfully identified for the given claims and returning available notification channels for the user."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request. The request cannot be processed by the server."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Username recovery is not enabled."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "No user found for the given set of claims or no recovery channels are availabe for the user."),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Mutiple users found for the given claims."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error.") })

    public Response initiateUsernameRecovery(@ApiParam(value = "Request to initate username recovery process. The request should contain the claims to identify the user. User claims are `REQUIRED`." ,required=true ) @Valid InitRequestDTO initRequest) {

        return delegate.initiateUsernameRecovery(initRequest);
    }

    @Valid
    @POST
    @Path("/password/recover")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get Recovery Information\n",
            notes = "- This API is used to send recovery information to the user who matches the recovery code via the channel specified by the channel Id.\n- NOTE: If the notification channel is EXTERNAL, the API will return a confirmationCode.\n- Use the returned confimation code with __password recpvery confirm API__ to verify the password recovery process.\n- The API will return the next API call.\n",
            response = PasswordRecoveryExternalNotifyResponseDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "- Password recovery initiated via `EXTERNAL` channel. API will return a  `confirmationCode` to confirm password recovery.\n- Use the confirmation code with __password recpvery confirm API__ to confirm the password recovery.\n"),
        
        @io.swagger.annotations.ApiResponse(code = 202, message = "- Password recovery initiated via internal channels. API will send a `confirmationCode` to the channel specified by the user.\n- Use the confirmation code with __password recovery confirm API__ to confirm the password recovery.\n"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request. Request cannot be processed by the server."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Password recovery is not enabled."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Recovery code is not found."),
        
        @io.swagger.annotations.ApiResponse(code = 406, message = "- Recovery code given in the request is not valid or expired.\n- Channel id is not valid\n"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error.") })

    public Response recoverPassword(@ApiParam(value = "- Request to receive recovery notifications.\n- `recoveryCode` and `channelId` are `REQUIRED`.\n- __NOTE__ `channelId` should always be __larger than 0__.\n" ,required=true ) @Valid RecoveryRequestDTO recoveryRequest) {

        return delegate.recoverPassword(recoveryRequest);
    }

    @Valid
    @POST
    @Path("/username/recover")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get Recovery Information",
            notes = "- This API is used to confirm username recovery and send username recovery information to the user who matches the recovery code via the channel specified by the channel Id.\n - __NOTE__: If the notification channel is __EXTERNAL__, the API will return the username of the user.\n",
            response = UsernameRecoveryExternalNotifyResponseDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Username is successfully recovered and the __notification channel__ is `EXTERNAL`."),
        
        @io.swagger.annotations.ApiResponse(code = 202, message = "Username is successfully recovered."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request. Request cannot be processed by the server."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Recovery code is not found."),
        
        @io.swagger.annotations.ApiResponse(code = 406, message = "- Recovery code given in the request is not valid or expired.\n- Channel id is not valid\n"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error.") })

    public Response recoverUsername(@ApiParam(value = "- Request to confirm username recovery and receive recovery notifications.\n- `recoveryCode` and `channelId` are `REQUIRED`.\n- __NOTE__ `channelId` should always be __larger than 0__.\n" ,required=true ) @Valid RecoveryRequestDTO recoveryRequest) {

        return delegate.recoverUsername(recoveryRequest);
    }

    @Valid
    @POST
    @Path("/password/resend")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Resend password recovery confirmation details\n",
            notes = "- This API is used to resend a confirmation code to the user via a user preferred channel defined at password recovery.\n- NOTE: The API cannot be used when the notification channel is external.\n- The API will return the next API calls.\n",
            response = ResendConfirmationCodeResponseDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 202, message = "- API will send a `confirmationCode` to the channel specified by the user.\n- Use the confirmation code with __password recpvery confirm API__ to confirm the password recovery.\n"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request. Request cannot be processed by the server."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Resend code is not found."),
        
        @io.swagger.annotations.ApiResponse(code = 406, message = "Resend code given in the request is not valid or expired."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error.") })

    public Response resendConfirmation(@ApiParam(value = "- Request to resend the `confirmationCode` to the user via a user preferred channel.\n- Can send additional properties.\n" ,required=true ) @Valid ResendConfirmationRequestDTO resendConfirmationRequest) {

        return delegate.resendConfirmation(resendConfirmationRequest);
    }

    @Valid
    @POST
    @Path("/password/reset")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Reset password\n",
            notes = "This API is used to reset the password of the user who matches the resetCode given by the confirmation API.\n",
            response = PasswordResetResponseDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful password reset."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request. Request cannot be processed by the server."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Reset code is not found."),
        
        @io.swagger.annotations.ApiResponse(code = 406, message = "Reset code given in the request is not valid or expired."),
        
        @io.swagger.annotations.ApiResponse(code = 412, message = "Password policy violation."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error.") })

    public Response resetPassword(@ApiParam(value = "- Request to reset the password.\n- `resetCode` and `password` are required.\n" ,required=true ) @Valid ResetRequestDTO resetRequest) {

        return delegate.resetPassword(resetRequest);
    }

}
