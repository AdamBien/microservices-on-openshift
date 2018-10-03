package com.airhacks.ping.boundary;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
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
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Path("ping")
public class PingResource {

    @Inject
    MicroClient microClient;

    LongAdder errorCount;

    @PostConstruct
    public void init() {
        this.errorCount = new LongAdder();
    }



    @GET
    public void ping(@Suspended AsyncResponse response) {
        response.setTimeout(2, TimeUnit.SECONDS);
        response.setTimeoutHandler(this::handleTimeout);
        if (errorCount.intValue() >= 3) {
            Response errorResponse = Response.status(503).header("cause", "consumer: cb open").build();
            response.resume(errorResponse);
        }
        this.microClient.ping().whenComplete((payload, t) -> this.handle(response, payload, t));
    }

    void handle(AsyncResponse response, String payload, Throwable t) {
        if (t != null) {
            Throwable rootCause = t.getCause().getCause();
            String message = rootCause.getClass().getSimpleName() + ":" + rootCause.getMessage();
            Response errorResponse = Response.status(503).header("cause", message).build();
            errorCount.increment();
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
