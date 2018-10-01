
package com.airhacks.ping.boundary;

import javax.ejb.ApplicationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
@ApplicationException(rollback = true)
public class MicroCommunicationException extends WebApplicationException {

    public MicroCommunicationException(String message) {
        super(Response.status(503).header("cause", message).build());
    }

}
