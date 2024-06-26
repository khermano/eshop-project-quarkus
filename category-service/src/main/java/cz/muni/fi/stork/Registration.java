package cz.muni.fi.stork;

import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.mutiny.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.mutiny.core.Vertx;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class Registration {
    @ConfigProperty(name = "consul.host") String host;
    @ConfigProperty(name = "consul.port") int port;
    @ConfigProperty(name = "categories-port", defaultValue = "8092") int categories;
    private final String appName = "categories";

    @Inject
    LaunchMode mode;
    /**
     * Register our category service in Consul.
     *
     * Note: this method is called on a worker thread, and so it is allowed to block.
     */
    public void init(@Observes StartupEvent ev, Vertx vertx) {
        if (mode != LaunchMode.TEST) {
            ConsulClient client = ConsulClient.create(vertx, new ConsulClientOptions().setHost(host).setPort(port));

            client.registerServiceAndAwait(
                    new ServiceOptions().setPort(categories).setAddress("localhost").setName(appName));
        }
    }

    public void shutDown(@Observes ShutdownEvent ev, Vertx vertx) {
        if (mode != LaunchMode.TEST) {
            ConsulClient client = ConsulClient.create(vertx, new ConsulClientOptions().setHost(host).setPort(port));

            client.deregisterServiceAndAwait(appName);
        }
    }
}