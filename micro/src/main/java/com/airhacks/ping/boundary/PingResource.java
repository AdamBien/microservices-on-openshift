package com.airhacks.ping.boundary;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
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

    @Resource
    ManagedExecutorService mes;

    @GET
    public void ping(@Suspended AsyncResponse response) {
        response.setTimeout(1500, TimeUnit.MILLISECONDS);
        response.setTimeoutHandler(this::handleTimeout);

        supplyAsync(this.service::message, this.mes).
                thenAccept(response::resume);

    }

    void handleTimeout(AsyncResponse asyncResponse) {
        Response error = Response.status(503).
                header("cause", "micro is overloaded, response takes longer than 500ms").
                build();
        asyncResponse.resume(error);
    }

}
