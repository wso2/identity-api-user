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

package org.wso2.carbon.identity.rest.api.user.totp.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.core.util.CryptoException;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.user.totp.common.TOTPConstants;
import org.wso2.carbon.identity.application.authentication.framework.exception.AuthenticationFailedException;
import org.wso2.carbon.identity.application.authenticator.totp.TOTPAuthenticatorConstants;
import org.wso2.carbon.identity.application.authenticator.totp.TOTPKeyGenerator;
import org.wso2.carbon.identity.application.authenticator.totp.exception.TOTPException;
import org.wso2.carbon.identity.application.authenticator.totp.util.TOTPAuthenticatorConfig;
import org.wso2.carbon.identity.application.authenticator.totp.util.TOTPAuthenticatorCredentials;
import org.wso2.carbon.identity.application.authenticator.totp.util.TOTPAuthenticatorKey;
import org.wso2.carbon.identity.application.authenticator.totp.util.TOTPKeyRepresentation;
import org.wso2.carbon.identity.application.authenticator.totp.util.TOTPUtil;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.rest.api.user.totp.v1.dto.TOTPResponseDTO;
import org.wso2.carbon.identity.rest.api.user.totp.v1.dto.TOTPSecretResponseDTO;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.totp.common.TOTPConstants.ErrorMessage.SERVER_ERROR_DECRYPTING_SECRET;
import static org.wso2.carbon.identity.api.user.totp.common.TOTPConstants.ErrorMessage.SERVER_ERROR_GENERAL;
import static org.wso2.carbon.identity.api.user.totp.common.TOTPConstants.ErrorMessage
        .SERVER_ERROR_RETRIEVING_REALM_FOR_USER;
import static org.wso2.carbon.identity.api.user.totp.common.TOTPConstants.ErrorMessage
        .SERVER_ERROR_RETRIEVING_USERSTORE_MANAGER;
import static org.wso2.carbon.identity.api.user.totp.common.TOTPConstants.ErrorMessage.USER_ERROR_ACCESS_DENIED_FOR_BASIC_AUTH;
import static org.wso2.carbon.identity.api.user.totp.common.TOTPConstants.ErrorMessage.USER_ERROR_QR_CODE_URL_NOT_EXIST;
import static org.wso2.carbon.identity.api.user.totp.common.TOTPConstants.ErrorMessage.USER_ERROR_UNAUTHORIZED_USER;

/**
 * Service class for TOTP API
 */
public class TOTPService {

    private static final Log log = LogFactory.getLog(TOTPService.class);

    /**
     * Retrieve the secret key of a given user.
     *
     * @return Secret Key.
     */
    public TOTPSecretResponseDTO getSecretKey() {

        if (!isValidAuthenticationType()) {
            throw handleError(Response.Status.FORBIDDEN, USER_ERROR_ACCESS_DENIED_FOR_BASIC_AUTH);
        }

        UserRealm userRealm;
        String encoding;

        User user = getUser();
        TOTPSecretResponseDTO totpResponseDTO = new TOTPSecretResponseDTO();
        String secretKey = null;
        String tenantAwareUsername = null;
        Map<String, String> claims = new HashMap<>();

        try {
            userRealm = TOTPUtil.getUserRealm(user.toFullQualifiedUsername());
            tenantAwareUsername = MultitenantUtils.getTenantAwareUsername(user.toFullQualifiedUsername());
            if (userRealm != null) {
                Map<String, String> userClaimValues = userRealm.getUserStoreManager().
                        getUserClaimValues(tenantAwareUsername,
                                new String[] { TOTPAuthenticatorConstants.SECRET_KEY_CLAIM_URL }, null);
                secretKey = userClaimValues.get(TOTPAuthenticatorConstants.SECRET_KEY_CLAIM_URL);
                if (StringUtils.isEmpty(secretKey)) {
                    TOTPAuthenticatorKey key = TOTPKeyGenerator.generateKey(user.getTenantDomain());
                    secretKey = key.getKey();

                    encoding = TOTPUtil.getEncodingMethod(user.getTenantDomain());

                    claims.put(TOTPAuthenticatorConstants.SECRET_KEY_CLAIM_URL, TOTPUtil.encrypt(secretKey));
                    claims.put(TOTPAuthenticatorConstants.ENCODING_CLAIM_URL, encoding);
                    TOTPKeyGenerator.addTOTPClaimsAndRetrievingQRCodeURL(claims, user.toFullQualifiedUsername());
                } else {
                    secretKey = TOTPUtil.decrypt(secretKey);
                }
            }
        } catch (AuthenticationFailedException e) {
            throw handleException(e, USER_ERROR_UNAUTHORIZED_USER);
        } catch (UserStoreException e) {
            throw handleException(e, SERVER_ERROR_RETRIEVING_USERSTORE_MANAGER, tenantAwareUsername);
        } catch (CryptoException e) {
            throw handleException(e, SERVER_ERROR_DECRYPTING_SECRET);
        } catch (TOTPException e) {
            throw handleException(e, SERVER_ERROR_GENERAL);
        }
        totpResponseDTO.setSecret(secretKey);
        return totpResponseDTO;
    }

    /**
     * Generate TOTP Token for a given user.
     *
     * @return Encoded QR Code URL.
     */
    public TOTPResponseDTO initTOTP() {

        if (!isValidAuthenticationType()) {
            throw handleError(Response.Status.FORBIDDEN, USER_ERROR_ACCESS_DENIED_FOR_BASIC_AUTH);
        }

        TOTPResponseDTO totpResponseDTO = new TOTPResponseDTO();
        User user = getUser();
        try {
            Map<String, String> claims = TOTPKeyGenerator.generateClaims(user.toFullQualifiedUsername(), false);
            totpResponseDTO.setQrCodeUrl(TOTPKeyGenerator.addTOTPClaimsAndRetrievingQRCodeURL(claims,
                    user.toFullQualifiedUsername()));
            return totpResponseDTO;
        } catch (TOTPException e) {
            throw handleException(e, SERVER_ERROR_GENERAL);
        }
    }

    /**
     * Generate TOTP QR Code URL for a given user.
     *
     * @return Encoded QR Code URL.
     */
    public TOTPResponseDTO getQRUrlCode() {

        if (!isValidAuthenticationType()) {
            throw handleError(Response.Status.FORBIDDEN, USER_ERROR_ACCESS_DENIED_FOR_BASIC_AUTH);
        }

        TOTPResponseDTO totpResponseDTO = new TOTPResponseDTO();
        User user = getUser();
        try {
            Map<String, String> claims = TOTPKeyGenerator.generateClaims(user.toFullQualifiedUsername(), false);
            if (claims.containsKey(TOTPAuthenticatorConstants.QR_CODE_CLAIM_URL)) {
                totpResponseDTO.setQrCodeUrl(claims.get(TOTPAuthenticatorConstants.QR_CODE_CLAIM_URL));
                return totpResponseDTO;
            }
            throw handleError(Response.Status.NOT_FOUND, USER_ERROR_QR_CODE_URL_NOT_EXIST);
        } catch (TOTPException e) {
            throw handleException(e, SERVER_ERROR_GENERAL);
        }
    }

    /**
     * Resets TOTP credentials of the user.
     */
    public void resetTOTP() {

        if (!isValidAuthenticationType()) {
            throw handleError(Response.Status.FORBIDDEN, USER_ERROR_ACCESS_DENIED_FOR_BASIC_AUTH);
        }

        User user = getUser();
        try {
            TOTPKeyGenerator.resetLocal(user.toFullQualifiedUsername());
        } catch (AuthenticationFailedException e) {
            throw handleException(e, USER_ERROR_UNAUTHORIZED_USER);
        } catch (TOTPException ex) {
            throw handleException(ex, SERVER_ERROR_RETRIEVING_REALM_FOR_USER);
        }
    }

    /**
     * Refreshes TOTP secret key of the user.
     *
     * @return Encoded QR Code URL for refreshed secret key
     */
    public TOTPResponseDTO refreshSecretKey() {

        if (!isValidAuthenticationType()) {
            throw handleError(Response.Status.FORBIDDEN, USER_ERROR_ACCESS_DENIED_FOR_BASIC_AUTH);
        }

        TOTPResponseDTO totpResponseDTO = new TOTPResponseDTO();
        User user = getUser();
        try {
            Map<String, String> claims = TOTPKeyGenerator.generateClaims(user.toFullQualifiedUsername(), true);
            totpResponseDTO.setQrCodeUrl(TOTPKeyGenerator.addTOTPClaimsAndRetrievingQRCodeURL(claims,
                    user.toFullQualifiedUsername()));
        } catch (TOTPException e) {
            throw handleException(e, SERVER_ERROR_GENERAL);
        }
        return totpResponseDTO;
    }

    /**
     * Validates the user entered verification code.
     *
     * @param verificationCode OTP verification code.
     * @return whether OTP is valid or not.
     */
    public TOTPResponseDTO validateTOTP(int verificationCode) {

        TOTPResponseDTO totpResponseDTO = new TOTPResponseDTO();
        String username = getUser().toString();
        TOTPKeyRepresentation encoding = TOTPKeyRepresentation.BASE32;
        String tenantDomain = MultitenantUtils.getTenantDomain(username);
        String encodingMethod;
        try {
            encodingMethod = TOTPUtil.getEncodingMethod(tenantDomain);
            if (TOTPAuthenticatorConstants.BASE64.equals(encodingMethod)) {
                encoding = TOTPKeyRepresentation.BASE64;
            }
            TOTPAuthenticatorConfig.TOTPAuthenticatorConfigBuilder configBuilder =
                    new TOTPAuthenticatorConfig.TOTPAuthenticatorConfigBuilder()
                            .setKeyRepresentation(encoding);
            TOTPAuthenticatorCredentials totpAuthenticator =
                    new TOTPAuthenticatorCredentials(configBuilder.build());
            String secretKey = getSecretKey().getSecret();
            if (log.isDebugEnabled()) {
                log.debug("Validating TOTP verification code for the user: " + username);
            }
            totpResponseDTO.setIsValid(totpAuthenticator.authorize(secretKey, verificationCode));
        } catch (AuthenticationFailedException e) {
            throw handleException(e, USER_ERROR_UNAUTHORIZED_USER);
        }
        return totpResponseDTO;
    }

    /**
     * Get authenticated user.
     *
     * @return Authenticated user
     */
    public static User getUser() {

        return ContextLoader.getUserFromContext();
    }

    /**
     * Handle invalid input
     *
     * @param errorEnum
     * @param data
     * @return APIError
     */
    public APIError handleInvalidInput(TOTPConstants.ErrorMessage errorEnum, String... data) {

        return handleError(Response.Status.HTTP_VERSION_NOT_SUPPORTED, errorEnum);
    }

    /**
     * Handle Exceptions
     *
     * @param e
     * @param errorEnum
     * @return
     */
    private APIError handleException(Exception e, TOTPConstants.ErrorMessage errorEnum, String... data) {

        ErrorResponse errorResponse;
        if (data != null) {
            errorResponse = getErrorBuilder(errorEnum).build(log, e, String.format(errorEnum.getDescription(),
                    (Object[]) data));
        } else {
            errorResponse = getErrorBuilder(errorEnum).build(log, e, errorEnum.getDescription());
        }

        if (e instanceof AuthenticationFailedException) {
            return handleError(Response.Status.UNAUTHORIZED, USER_ERROR_UNAUTHORIZED_USER);
        } else if (e instanceof UserStoreException | e instanceof CryptoException) {
            return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        } else if (e instanceof TOTPException) {
            errorResponse.setDescription(e.getMessage());
            return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        } else {
            return new APIError(Response.Status.BAD_REQUEST, errorResponse);
        }
    }

    /**
     * Handle User errors
     *
     * @param status
     * @param error
     * @return
     */
    private APIError handleError(Response.Status status, TOTPConstants.ErrorMessage error) {

        return new APIError(status, getErrorBuilder(error).build());
    }

    /**
     * Get ErrorResponse Builder for Error enum
     *
     * @param errorEnum
     * @return
     */
    private ErrorResponse.Builder getErrorBuilder(TOTPConstants.ErrorMessage errorEnum) {

        return new ErrorResponse.Builder().withCode(errorEnum.getCode()).withMessage(errorEnum.getMessage())
                .withDescription(errorEnum.getDescription());
    }

    private boolean isValidAuthenticationType() {

        /*
        Check whether the request is authenticated with basic auth. TOTP endpoint should not be allowed for basic
        authentication. This approach can be improved by providing a Level of Assurance (LOA) and checking that in
        TOTPService.
         */
        if (IdentityUtil.threadLocalProperties.get().get(TOTPConstants.AUTHENTICATED_WITH_BASIC_AUTH) != null
                || Boolean.parseBoolean((String) IdentityUtil.threadLocalProperties.get()
                .get(TOTPConstants.AUTHENTICATED_WITH_BASIC_AUTH))) {
            if (log.isDebugEnabled()) {
                log.debug("Not a valid authentication method. "
                        + "This method is blocked for the requests with basic authentication.");
            }
            return false;
        }
        return true;
    }
}
