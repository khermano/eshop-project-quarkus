package cz.muni.fi.resource;

import cz.muni.fi.stork.OrderClient;
import cz.muni.fi.enums.Action;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * REST Controller for Orders
 */
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class OrderResource {
    @Inject
    @RestClient
    private OrderClient orderClient;

    /**
     * Returns all orders according to the given parameters
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/orders?status=ALL
     * or curl -i -X GET http://localhost:8080/eshop-rest/orders?status=ALL&last_week=TRUE
     * 
     * @param status can be {ALL, RECEIVED, CANCELED, SHIPPED, DONE}
     *               defines orders with StateOrder (RECEIVED, CANCELED, SHIPPED, DONE) or ALL orders
     * @param lastWeek if true we consider only orders from last 7 days
     *                 default value is false
     * @return list of orders by given parameters, 406 if status parameter not valid
     */
    @GET
    public Response getOrders(@QueryParam("status") String status, @QueryParam("last_week") @DefaultValue("false") boolean lastWeek) {
        return orderClient.getOrders(status, lastWeek);
    }

    /**
     * Returns all orders created by a user with the given id
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/orders/by_user_id/1
     *
     * @param userId id of the user who created orders
     * @return list of orders created by user with given id
     */
    @GET
    @Path("/by_user_id/{userId}")
    public Response getOrdersByUserId(long userId) {
        return orderClient.getOrdersByUserId(userId);
    }

    /**
     * Returns order with given id
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/orders/1
     *
     * @param id of the order
     * @return order with given id, 404 if order with given id doesn't exist
     */
    @GET
    @Path("/{id}")
    public Response getOrder(long id) {
        return orderClient.getOrder(id);
    }

    /**
     * Perform one action on the order
     * Either cancelling, shipping or finishing the order
     * The only allowed changes of state are: RECEIVED -> CANCELED (action=CANCEL),
     * RECEIVED -> SHIPPED (action=SHIP), SHIPPED -> DONE (action=FINISH)
     * e.g.: curl -i -X POST http://localhost:8080/eshop-rest/orders/2?action=FINISH
     *
     * @param orderId id of the order
     * @param action one of CANCEL, SHIP, FINISH
     * @return order on which action was performed, 404 if the action parameter is invalid (originally 406, but Quarkus is behaving
     *         different from Spring Boot when enum is invalid), 500 if order with given ID doesn't exist or something else went wrong
     */
    @POST
    @Path("/{orderId}")
    public Response shipOrder(long orderId, @QueryParam("action") Action action) {
        return orderClient.shipOrder(orderId, action);
    }
}
