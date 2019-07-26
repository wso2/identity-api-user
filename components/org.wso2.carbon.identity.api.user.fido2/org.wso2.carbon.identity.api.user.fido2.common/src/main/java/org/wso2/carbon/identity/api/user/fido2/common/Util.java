package org.wso2.carbon.identity.api.user.fido2.common;

import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;

import javax.ws.rs.core.Response;

/**
 * Utility class for FIDO2 API.
 */
public class Util {

    public static APIError handleError(Response.Status status, Constants.ErrorMessages error, String... data) {

        String message;
        if (data != null) {
            message = String.format(error.getMessage(), data);
        } else {
            message = error.getMessage();
        }
        return new APIError(status, new ErrorResponse.Builder().withCode(error.getCode()).withMessage(message)
                .withDescription(error.getDescription()).build());
    }
}
