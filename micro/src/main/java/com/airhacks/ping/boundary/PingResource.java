package com.airhacks.ping.boundary;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author airhacks.com
 */
@RequestScoped
@Path("ping")
public class PingResource {

    @Inject
    PIngService service;

    @GET
    public String ping() {
        return this.service.message();
    }

}
