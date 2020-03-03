package org.wso2.carbon.identity.api.user.biometric.device.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.authenticator.biometric.device.handler.exception.BiometricDeviceHandlerClientException;
import org.wso2.carbon.identity.application.authenticator.biometric.device.handler.exception.BiometricdeviceHandlerServerException;
import org.wso2.carbon.user.api.UserStoreException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.SQLException;
import javax.mail.AuthenticationFailedException;
import javax.ws.rs.core.Response;

/**
 * The class which handles API errors.
 */
public class HandleException {
    private static final Log log = LogFactory.getLog(HandleException.class);

    public static APIError handleException(Exception e, Constants.ErrorMessages errorEnum,
                                       String... data) {
        ErrorResponse errorResponse;
        if (data != null) {
            errorResponse = getErrorBuilder(errorEnum).build(log, e, String.format(errorEnum.getDescription(),
                    (Object[]) data));
        } else {
            errorResponse = getErrorBuilder(errorEnum).build(log, e, errorEnum.getDescription());
        }
        if (e instanceof AuthenticationFailedException) {
            return handleError(Response.Status.UNAUTHORIZED, Constants.ErrorMessages.
                    ERROR_CODE_GET_DEVICE_SERVER_ERROR);
        } else if (e instanceof UserStoreException) {
            return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        } else if (e instanceof SignatureException) {
            return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        } else if (e instanceof UnsupportedEncodingException) {
            return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        } else if (e instanceof JsonProcessingException) {
            return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        } else if (e instanceof InvalidKeyException) {
            return new APIError(Response.Status.UNAUTHORIZED, errorResponse);
        } else if (e instanceof NoSuchAlgorithmException) {
            return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        } else if (e instanceof SQLException) {
            return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        } else if (e instanceof BiometricDeviceHandlerClientException) {
            if (e.getMessage() == null) {
                errorEnum.setMessage(e.getMessage());
            }
            return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        } else if (e instanceof BiometricdeviceHandlerServerException) {
            if (e.getMessage() == null) {
                errorEnum.setMessage(e.getMessage());
            }
            return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        }  else {
            return new APIError(Response.Status.BAD_REQUEST, errorResponse);
        }
    }
    private static ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessages errorEnum) {

        return new ErrorResponse.Builder().withCode(errorEnum.getCode()).withMessage(errorEnum.getMessage())
                .withDescription(errorEnum.getDescription());
    }
    private static APIError handleError(Response.Status status, Constants.ErrorMessages error) {
        return new APIError(status, getErrorBuilder(error).build());
    }
}
