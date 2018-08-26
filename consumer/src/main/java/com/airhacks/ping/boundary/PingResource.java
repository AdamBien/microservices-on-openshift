package com.airhacks.ping.boundary;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author airhacks.com
 */
@Path("ping")
public class PingResource {

    private WebTarget microTarget;

    @PostConstruct
    public void initClient() {
        Client client = ClientBuilder.newClient();
        this.microTarget = client.target("http://micro:8080/micro/resources/ping");
    }


    @GET
    public String ping() {
        return "Enjoy Java EE 8, calling micro -> " + this.microTarget.request(MediaType.TEXT_PLAIN).get(String.class);
    }

}
