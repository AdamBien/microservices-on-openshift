package com.airhacks.ping.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

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
    public String ping() {
        return "Enjoy Java EE 8, calling micro -> " + this.microClient.ping();
    }

}
