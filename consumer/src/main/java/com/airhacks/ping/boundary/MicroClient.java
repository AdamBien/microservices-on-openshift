
package com.airhacks.ping.boundary;

import com.airhacks.breakr.boundary.Breakr;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author airhacks.com
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Interceptors(Breakr.class)
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

    public CompletionStage<String> ping() {
        return this.microTarget.
                request(MediaType.TEXT_PLAIN).
                rx().
                get(String.class).
                thenApply(this::prepend);
    }

    String prepend(String input) {
        return "Enjoy Java EE 8, calling micro -> " + input;
    }
}
