
package com.airhacks.ping.boundary;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
public class MicroClient {
    private WebTarget microTarget;

    @PostConstruct
    public void initClient() {
        Client client = ClientBuilder.newClient();
        this.microTarget = client.target("http://micro:8080/micro/resources/ping");
    }

    @GET
    public String ping() {
        try {
            return "Enjoy Java EE 8, calling micro -> " + this.microTarget.request(MediaType.TEXT_PLAIN).get(String.class);
        } catch (ProcessingException ex) {
            Throwable cause = ex.getCause();
            String message = cause.getClass().getSimpleName() + ":" + cause.getMessage();
            throw new WebApplicationException(Response.status(503).header("cause", message).build());
        }
    }
}
