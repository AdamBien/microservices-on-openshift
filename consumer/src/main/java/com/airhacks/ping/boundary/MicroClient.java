
package com.airhacks.ping.boundary;

import com.airhacks.configuration.boundary.DefaultValue;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.inject.Inject;
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
public class MicroClient {
    private WebTarget microTarget;

    @Resource
    ManagedThreadFactory mtf;

    @Inject
    @DefaultValue("micro")
    String host;

    @Inject
    @DefaultValue("4")
    int maxPoolSize;

    @Inject
    @DefaultValue("200")
    long connectTimeout;


    @PostConstruct
    public void initClient() {
        ExecutorService mes = new ThreadPoolExecutor(2, this.maxPoolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(2),
                this.mtf);
        Client client = ClientBuilder.newBuilder().
                executorService(mes).
                connectTimeout(this.connectTimeout, TimeUnit.MILLISECONDS).
                readTimeout(500, TimeUnit.MILLISECONDS).
                build();
        this.microTarget = client.target("http://" + this.host + ":8080/micro/resources/ping");
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
