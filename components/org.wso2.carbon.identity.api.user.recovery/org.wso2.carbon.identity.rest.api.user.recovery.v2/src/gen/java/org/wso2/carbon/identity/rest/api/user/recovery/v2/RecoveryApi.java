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

package org.wso2.carbon.identity.rest.api.user.recovery.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;

import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.AccountRecoveryType;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.ConfirmRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.ErrorResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.InitRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.PasswordRecoveryExternalNotifyResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.PasswordRecoveryInternalNotifyResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.PasswordResetResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.RecoveryRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.ResendConfirmationCodeExternalResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.ResendConfirmationCodeInternalResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.ResendConfirmationRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.ResetCodeResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.ResetRequest;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.RetryErrorResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.model.UsernameRecoveryNotifyResponse;
import org.wso2.carbon.identity.rest.api.user.recovery.v2.RecoveryApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/recovery")
@Api(description = "The recovery API")

public class RecoveryApi  {

    @Autowired
    private RecoveryApiService delegate;

    @Valid
    @POST
    @Path("/password/confirm")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Confirm password recovery ", notes = "- This API is used to validate the __confirmationCode__ given at password recovery. - Use the returned __reset code__ with the __password reset API__ to reset the password. - The API will return the next API call. ", response = ResetCodeResponse.class, tags={ "Password Recovery", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "- Password reset confirmed. - `resetCode` is returned. ", response = ResetCodeResponse.class),
        @ApiResponse(code = 400, message = "Bad Request. Request cannot be processed by the server.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Confirmation code is not found.", response = ErrorResponse.class),
        @ApiResponse(code = 406, message = "Confirmation code given in the request is not valid or expired. ", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Server Error.", response = ErrorResponse.class)
    })
    public Response confirmRecovery(@ApiParam(value = "- Request to confirm the password recovery. - `confirmationCode` is `REQUIRED`. " ,required=true) @Valid ConfirmRequest confirmRequest) {

        return delegate.confirmRecovery(confirmRequest );
    }

    @Valid
    @POST
    @Path("/password/init")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Initiate password recovery ", notes = "- This API is used to initiate password recovery by user. The API will return recovery information for password recovery with challenge questions and password recovery with notifications. - `mode` __recoverWithNotifications__ contains available communication channels and a recovery code for password recovery with notifications. The next API call will be returned in the response. - If `password recovery with notifications` is not enabled, the response will not contain __recoverWithNotifications__ mode. - __recoverWithChallengeQuestions__ contains the next API call to begin the recovery process via challenge questions. - If `password recovery with challenge questions` is not enabled, the response will not contain __recoverWithChallengeQuestions__ mode. ", response = AccountRecoveryType.class, responseContainer = "List", tags={ "Password Recovery", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "User is successfully identified for the given claims and returning available notification channels for the user.", response = AccountRecoveryType.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request. The request cannot be processed by the server.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "No user found for the given set of claims or no recovery channels are availabe for the user.", response = ErrorResponse.class),
        @ApiResponse(code = 409, message = "Mutiple users found for the given claims.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Server Error.", response = ErrorResponse.class)
    })
    public Response initiatePasswordRecovery(@ApiParam(value = "Request to initate password recovery process. The request should contain the claims to identify the user. User claims are `REQUIRED`." ,required=true) @Valid InitRequest initRequest) {

        return delegate.initiatePasswordRecovery(initRequest );
    }

    @Valid
    @POST
    @Path("/username/init")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Initiate Username Recovery", notes = "- This API is used to initiate username recovery by user. The API will return available recovery information for username recovery with notifications. - Use the returned __recoveryCode__ and the __channelId__ with __username recover api__ to confirm the the username recovery and to recieve recovery notifications via the channel specified by the user. ", response = AccountRecoveryType.class, responseContainer = "List", tags={ "Username Recovery", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "User is successfully identified for the given claims and returning available notification channels for the user.", response = AccountRecoveryType.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request. The request cannot be processed by the server.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Username recovery is not enabled.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "No user found for the given set of claims or no recovery channels are availabe for the user.", response = ErrorResponse.class),
        @ApiResponse(code = 409, message = "Mutiple users found for the given claims.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Server Error.", response = ErrorResponse.class)
    })
    public Response initiateUsernameRecovery(@ApiParam(value = "Request to initate username recovery process. The request should contain the claims to identify the user. User claims are `REQUIRED`." ,required=true) @Valid InitRequest initRequest) {

        return delegate.initiateUsernameRecovery(initRequest );
    }

    @Valid
    @POST
    @Path("/password/recover")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Get Recovery Information ", notes = "- This API is used to send recovery information to the user who matches the recovery code via the channel specified by the channel Id. - NOTE: If the notification channel is EXTERNAL, the API will return a confirmationCode. - Use the returned confimation code with __password recpvery confirm API__ to verify the password recovery process. - The API will return the next API call. ", response = PasswordRecoveryExternalNotifyResponse.class, tags={ "Password Recovery", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "- Password recovery initiated via `EXTERNAL` channel. API will return a  `confirmationCode` to confirm password recovery. - Use the confirmation code with __password recpvery confirm API__ to confirm the password recovery. ", response = PasswordRecoveryExternalNotifyResponse.class),
        @ApiResponse(code = 202, message = "- Password recovery initiated via internal channels. API will send a `confirmationCode` to the channel specified by the user. - Use the confirmation code with __password recovery confirm API__ to confirm the password recovery. ", response = PasswordRecoveryInternalNotifyResponse.class),
        @ApiResponse(code = 400, message = "Bad Request. Request cannot be processed by the server.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Password recovery is not enabled.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Recovery code is not found.", response = ErrorResponse.class),
        @ApiResponse(code = 406, message = "- Recovery code given in the request is not valid or expired. - Channel id is not valid ", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Server Error.", response = ErrorResponse.class)
    })
    public Response recoverPassword(@ApiParam(value = "- Request to receive recovery notifications. - `recoveryCode` and `channelId` are `REQUIRED`. - __NOTE__ `channelId` should always be __larger than 0__. " ,required=true) @Valid RecoveryRequest recoveryRequest) {

        return delegate.recoverPassword(recoveryRequest );
    }

    @Valid
    @POST
    @Path("/username/recover")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Get Recovery Information", notes = "- This API is used to confirm username recovery and send username recovery information to the user who matches the recovery code via the channel specified by the channel Id.  - __NOTE__: If the notification channel is __EXTERNAL__, the API will return the username of the user. ", response = UsernameRecoveryNotifyResponse.class, tags={ "Username Recovery", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Username is successfully recovered and the __notification channel__ is `EXTERNAL`.", response = UsernameRecoveryNotifyResponse.class),
        @ApiResponse(code = 202, message = "Username is successfully recovered.", response = UsernameRecoveryNotifyResponse.class),
        @ApiResponse(code = 400, message = "Bad Request. Request cannot be processed by the server.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Recovery code is not found.", response = ErrorResponse.class),
        @ApiResponse(code = 406, message = "- Recovery code given in the request is not valid or expired. - Channel id is not valid ", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Server Error.", response = ErrorResponse.class)
    })
    public Response recoverUsername(@ApiParam(value = "- Request to confirm username recovery and receive recovery notifications. - `recoveryCode` and `channelId` are `REQUIRED`. - __NOTE__ `channelId` should always be __larger than 0__. " ,required=true) @Valid RecoveryRequest recoveryRequest) {

        return delegate.recoverUsername(recoveryRequest );
    }

    @Valid
    @POST
    @Path("/password/resend")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Resend password recovery confirmation details ", notes = "- This API is used to resend a confirmation code to the user via a user preferred channel defined at password recovery. - NOTE: The API cannot be used when the notification channel is external. - The API will return the next API calls. ", response = ResendConfirmationCodeExternalResponse.class, tags={ "Password Recovery", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "- API will return a new `confirmationCode`. - Use the confirmation code with __password recovery confirm API__ to confirm the password recovery. ", response = ResendConfirmationCodeExternalResponse.class),
        @ApiResponse(code = 202, message = "- API will send a `confirmationCode` to the channel specified by the user. - Use the confirmation code with __password recovery confirm API__ to confirm the password recovery. ", response = ResendConfirmationCodeInternalResponse.class),
        @ApiResponse(code = 400, message = "Bad Request. Request cannot be processed by the server.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Resend code is not found.", response = ErrorResponse.class),
        @ApiResponse(code = 406, message = "Resend code given in the request is not valid or expired.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Server Error.", response = ErrorResponse.class)
    })
    public Response resendConfirmation(@ApiParam(value = "- Request to resend the `confirmationCode` to the user via a user preferred channel. - Can send additional properties. " ,required=true) @Valid ResendConfirmationRequest resendConfirmationRequest) {

        return delegate.resendConfirmation(resendConfirmationRequest );
    }

    @Valid
    @POST
    @Path("/password/reset")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Reset password ", notes = "This API is used to reset the password of the user who matches the resetCode given by the confirmation API. ", response = PasswordResetResponse.class, tags={ "Password Recovery" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful password reset.", response = PasswordResetResponse.class),
        @ApiResponse(code = 400, message = "Bad Request. Request cannot be processed by the server.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Reset code is not found.", response = ErrorResponse.class),
        @ApiResponse(code = 406, message = "Reset code given in the request is not valid or expired.", response = ErrorResponse.class),
        @ApiResponse(code = 412, message = "Password policy violation.", response = RetryErrorResponse.class),
        @ApiResponse(code = 500, message = "Server Error.", response = ErrorResponse.class)
    })
    public Response resetPassword(@ApiParam(value = "- Request to reset the password. - `resetCode` and `password` are required. " ,required=true) @Valid ResetRequest resetRequest) {

        return delegate.resetPassword(resetRequest );
    }

}
