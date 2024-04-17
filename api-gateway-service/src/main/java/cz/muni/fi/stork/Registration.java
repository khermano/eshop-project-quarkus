package cz.muni.fi.stork;

import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.consul.ConsulClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Registration {
    @ConfigProperty(name = "consul.host") String host;
    @ConfigProperty(name = "consul.port") int port;
    @ConfigProperty(name = "apiGateway-port", defaultValue = "8080") int apiGateway;

    @Inject
    LaunchMode mode;
    /**
     * Register our api-gateway service in Consul.
     *
     * Note: this method is called on a worker thread, and so it is allowed to block.
     */
    public void init(@Observes StartupEvent ev, Vertx vertx) {
        if (mode != LaunchMode.TEST) {
            ConsulClient client = ConsulClient.create(vertx, new ConsulClientOptions().setHost(host).setPort(port));

            client.registerServiceAndAwait(
                    new ServiceOptions().setPort(apiGateway).setAddress("localhost").setName("apiGateway"));
        }
    }

    public void shutDown(@Observes ShutdownEvent ev, Vertx vertx) {
        if (mode != LaunchMode.TEST) {
            ConsulClient client = ConsulClient.create(vertx, new ConsulClientOptions().setHost(host).setPort(port));

            client.deregisterServiceAndAwait("users");
        }
    }
}