package org.wso2.carbon.identity.rest.api.user.push.v1.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.notification.push.device.handler.exception.PushDeviceHandlerClientException;
import org.wso2.carbon.identity.notification.push.device.handler.exception.PushDeviceHandlerException;

import javax.ws.rs.core.Response;

/**
 * Util class for push device management.
 */
public class Util {

    private static final Log LOG = LogFactory.getLog(Util.class);

    /**
     * Handle push device handler exception.
     *
     * @param e PushDeviceHandlerException.
     * @return APIError.
     */
    public static APIError handlePushDeviceHandlerException(PushDeviceHandlerException e) {

        if (e instanceof PushDeviceHandlerClientException) {
            ErrorResponse errorResponse = getErrorBuilder(e).build(LOG, e.getMessage());
            return new APIError(Response.Status.BAD_REQUEST, errorResponse);
        }
        ErrorResponse errorResponse = getErrorBuilder(e).build(LOG, e, e.getMessage());
        return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
    }

    /**
     * Get error builder.
     *
     * @param e PushDeviceHandlerException.
     * @return ErrorResponse.Builder.
     */
    private static ErrorResponse.Builder getErrorBuilder(PushDeviceHandlerException e) {

        return new ErrorResponse.Builder().withCode(e.getErrorCode()).withMessage(e.getMessage());
    }
}
