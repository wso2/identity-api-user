package org.wso2.carbon.identity.rest.api.user.push.v1.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Custom exception for push device management.
 */
public class PushDeviceManagementException extends WebApplicationException {

    public PushDeviceManagementException(Response.Status status, Error error) {

        super(Response.status(status).entity(error).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .build());
    }

    public PushDeviceManagementException(Response.Status status) {

        super(Response.status(status).build());
    }
}
