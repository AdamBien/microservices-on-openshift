
package com.airhacks.ping.boundary;

import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author airhacks.com
 */
@Stateless
public class MicroClient {
    private WebTarget microTarget;

    @PostConstruct
    public void initClient() {
        Client client = ClientBuilder.newBuilder().
                connectTimeout(200, TimeUnit.MILLISECONDS).
                readTimeout(500, TimeUnit.MILLISECONDS).
                build();
        this.microTarget = client.target("http://localhost:8080/micro/resources/ping");
    }

    @GET
    public String ping() {
        try {
            return "Enjoy Java EE 8, calling micro -> " + this.microTarget.request(MediaType.TEXT_PLAIN).get(String.class);
        } catch (ProcessingException ex) {
            Throwable cause = ex.getCause();
            String message = cause.getClass().getSimpleName() + ":" + cause.getMessage();
            throw new MicroCommunicationException(message);
        }
    }
}
