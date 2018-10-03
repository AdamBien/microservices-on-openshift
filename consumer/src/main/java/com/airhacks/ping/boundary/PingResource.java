package com.airhacks.ping.boundary;

import java.util.concurrent.TimeUnit;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Path("ping")
public class PingResource {

    @Inject
    MicroClient microClient;


    @GET
    public void ping(@Suspended AsyncResponse response) {
        response.setTimeout(2, TimeUnit.SECONDS);
        response.setTimeoutHandler(this::handleTimeout);
        this.microClient.ping().whenComplete((payload, t) -> this.handle(response, payload, t));
    }

    void handle(AsyncResponse response, String payload, Throwable t) {
        if (t != null) {
            Throwable rootCause = t.getCause().getCause();
            String message = rootCause.getClass().getSimpleName() + ":" + rootCause.getMessage();
            Response errorResponse = Response.status(503).header("cause", message).build();
            response.resume(errorResponse);
        } else {
            response.resume(payload);
        }
    }

    void handleTimeout(AsyncResponse asyncResponse) {
        Response errorResponse = Response.status(503).header("cause", "consumer took too long").build();
        asyncResponse.resume(errorResponse);
    }
}
