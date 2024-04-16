package cz.muni.fi.client;

import cz.muni.fi.enums.Action;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "stork://orders")
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
public interface OrderClient {
    @GET
    Response getOrders(@QueryParam("status") String status, @QueryParam("last_week") @DefaultValue("false") boolean lastWeek);

    @GET
    @Path("/by_user_id/{userId}")
    Response getOrdersByUserId(long userId);

    @GET
    @Path("/{id}")
    Response getOrder(long id);

    @POST
    @Path("/{orderId}")
    Response shipOrder(long orderId, @QueryParam("action") Action action);
}
