package cz.muni.fi.resource;

import cz.muni.fi.dto.OrderDTO;
import cz.muni.fi.entity.Order;
import cz.muni.fi.enums.Action;
import cz.muni.fi.enums.OrderState;
import cz.muni.fi.repository.OrderRepository;
import cz.muni.fi.service.OrderService;
import cz.muni.fi.stork.UserClient;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * REST Controller for Orders
 */
@Path("/orders") //TODO remove this after adding API GATEWAY
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class OrderResource {
    final static Logger logger = LoggerFactory.getLogger(OrderResource.class);

    @Inject
    private OrderService orderService;

    @Inject
    private OrderRepository orderRepository;

    @RestClient
    private UserClient userClient;

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
        logger.debug("rest getOrders({},{})", lastWeek, status);

        if (status.equalsIgnoreCase("ALL")) {
            List<OrderDTO> orderDTOs = new ArrayList<>();
            for (Order order: orderRepository.findAll().list()) {
                orderDTOs.add(orderService.getOrderDTOFromOrder(order));
            }
            return Response.ok(orderDTOs).build();
        }

        if (!OrderState.contains(status)) {
            return Response.status(406).build();
        }

        final OrderState os = OrderState.valueOf(status);
        List<OrderDTO> orderDTOs = new ArrayList<>();

        if (lastWeek) {
            for (Order order: orderService.getAllOrdersLastWeek(os)) {
                orderDTOs.add(orderService.getOrderDTOFromOrder(order));
            }
        } else {
            for (Order order: orderRepository.findByState(os)) {
                orderDTOs.add(orderService.getOrderDTOFromOrder(order));
            }
        }
        return Response.ok(orderDTOs).build();
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
        logger.debug("rest getOrderByUserId({})", userId);

        List<OrderDTO> orderDTOs = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        try {
            if (userClient.getUser(userId).getStatus() == HttpStatus.SC_OK) {
                orders = orderRepository.findByUserId(userId);
            }
        } catch (Exception e) {
            // we needed to catch the error caused by not existing user to reproduce behaviour of the original project and return empty list instead
            return Response.ok(orderDTOs).build();
        }

        for (Order order : orders) {
            orderDTOs.add(orderService.getOrderDTOFromOrder(order));
        }

        return Response.ok(orderDTOs).build();
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
        logger.debug("rest getOrder({})", id);

        Order order = orderRepository.findById(id);
        if (order != null) {
            return Response.ok(orderService.getOrderDTOFromOrder(order)).build();
        } else {
            return Response.status(404,"The requested resource was not found").build();
        }
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
     * @return order on which action was performed, 406 if the action parameter is invalid, 500 if order with given ID doesn't exist or something else went wrong
     */
    @POST
    @Path("/{orderId}")
    public Response shipOrder(long orderId, @QueryParam("action") Action action) {
        logger.debug("rest shipOrder({})", orderId);

        Order order = orderRepository.findById(orderId);
        if (order == null) {
            return Response.status(500).build();
        }

        if (action == Action.CANCEL) {
            orderService.shipOrder(order, OrderState.CANCELED);
        } else if (action == Action.SHIP) {
            orderService.shipOrder(order, OrderState.SHIPPED);
        } else if (action == Action.FINISH) {
            orderService.shipOrder(order, OrderState.DONE);
        }

        order = orderRepository.findById(orderId);
        if (order != null) {
            return Response.ok(orderService.getOrderDTOFromOrder(order)).build();
        } else {
            return Response.status(500).build();
        }
    }
}
