package com.airhacks.ping.boundary;

import java.util.concurrent.TimeUnit;
import javax.enterprise.context.RequestScoped;
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
@RequestScoped
@Path("ping")
public class PingResource {

    @Inject
    PingService service;

    @GET
    public void ping(@Suspended AsyncResponse response) {
        response.setTimeout(500, TimeUnit.MILLISECONDS);
        response.setTimeoutHandler(this::handleTimeout);
        response.resume(this.service.message());
    }

    void handleTimeout(AsyncResponse asyncResponse) {
        Response error = Response.status(503).
                header("cause", "micro is overloaded, reponse takes longer than 500ms").
                build();
        asyncResponse.resume(error);
    }

}
