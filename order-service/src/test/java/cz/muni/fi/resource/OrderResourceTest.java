package cz.muni.fi.resource;

import cz.muni.fi.dto.OrderDTO;
import cz.muni.fi.entity.Order;
import cz.muni.fi.enums.OrderState;
import cz.muni.fi.repository.OrderRepository;
import cz.muni.fi.service.OrderService;
import cz.muni.fi.client.UserClient;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
public class OrderResourceTest {
    @InjectMock
    private OrderService orderService;

    @InjectMock
    private OrderRepository orderRepository;

    private UserClient userClient;

    @Mock
    private PanacheQuery panacheQuery;

    @Inject
    private OrderResource orderResource;

    private static List<Order> orders;

    private static List<OrderDTO> orderDTOs;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeAll
    public static void ordersAndDtoSetup() {
        Order orderOne = new Order();
        orderOne.setId(1L);
        orderOne.setState(OrderState.DONE);
        orderOne.setCreated(Calendar.getInstance().getTime());
        orderOne.setUserId(1L);

        Order orderTwo = new Order();
        orderTwo.setId(2L);
        orderTwo.setState(OrderState.CANCELED);
        orderTwo.setCreated(Calendar.getInstance().getTime());
        orderTwo.setUserId(1L);

        Order orderThree = new Order();
        orderThree.setId(3L);
        orderThree.setState(OrderState.RECEIVED);
        orderThree.setCreated(Calendar.getInstance().getTime());
        orderThree.setUserId(1L);

        Order orderFour = new Order();
        orderFour.setId(4L);
        orderFour.setState(OrderState.SHIPPED);
        orderFour.setCreated(Calendar.getInstance().getTime());
        orderFour.setUserId(1L);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -10);

        Order orderFive = new Order();
        orderFive.setId(5L);
        orderFive.setState(OrderState.DONE);
        orderFive.setCreated(cal.getTime());
        orderFive.setUserId(1L);

        Order orderSix = new Order();
        orderSix.setId(6L);
        orderSix.setState(OrderState.SHIPPED);
        orderSix.setCreated(cal.getTime());
        orderSix.setUserId(1L);

        orders = List.of(orderOne, orderTwo, orderThree, orderFour, orderFive, orderSix);

        OrderDTO mockedOrderDTO = new OrderDTO();
        mockedOrderDTO.setId(1L);
        mockedOrderDTO.setState(OrderState.DONE);

        OrderDTO mockedOrderDTO2 = new OrderDTO();
        mockedOrderDTO2.setId(2L);
        mockedOrderDTO2.setState(OrderState.CANCELED);

        OrderDTO mockedOrderDTO3 = new OrderDTO();
        mockedOrderDTO3.setId(3L);
        mockedOrderDTO3.setState(OrderState.CANCELED);

        OrderDTO mockedOrderDTO4 = new OrderDTO();
        mockedOrderDTO4.setId(4L);
        mockedOrderDTO4.setState(OrderState.CANCELED);

        OrderDTO mockedOrderDTO5 = new OrderDTO();
        mockedOrderDTO5.setId(5L);
        mockedOrderDTO5.setState(OrderState.CANCELED);

        OrderDTO mockedOrderDTO6 = new OrderDTO();
        mockedOrderDTO6.setId(6L);
        mockedOrderDTO6.setState(OrderState.SHIPPED);

        orderDTOs = List.of(mockedOrderDTO, mockedOrderDTO2, mockedOrderDTO3, mockedOrderDTO4, mockedOrderDTO5, mockedOrderDTO6);
    }

    @Test
    public void getAllOrders() {
        List<Order> o2 = Arrays.asList(orders.get(0), orders.get(1), orders.get(5));

        doReturn(panacheQuery).when(orderRepository).findAll();
        doReturn(o2).when(panacheQuery).list();
        doReturn(orderDTOs.get(0)).when(orderService).getOrderDTOFromOrder(o2.get(0));
        doReturn(orderDTOs.get(1)).when(orderService).getOrderDTOFromOrder(o2.get(1));
        doReturn(orderDTOs.get(5)).when(orderService).getOrderDTOFromOrder(o2.get(2));

        given()
                .queryParam("status", "ALL")
                .when().get("/orders")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", hasItems(1, 2, 6))
                .body("state", hasItems("DONE", "CANCELED", "SHIPPED"));
    }

    @Test
    public void getAllOrdersByState() {
        List<Order> o2 = Arrays.asList(orders.get(0), orders.get(5));

        doReturn(o2).when(orderRepository).findByState(OrderState.DONE);
        doReturn(orderDTOs.get(0)).when(orderService).getOrderDTOFromOrder(o2.get(0));
        doReturn(orderDTOs.get(5)).when(orderService).getOrderDTOFromOrder(o2.get(1));

        given()
                .queryParam("status", "DONE")
                .when().get("/orders")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", hasItems(1))
                .body("state", hasItems("DONE"));
    }

    @Test
    public void getAllOrdersByUserId() {
        List<Order> o2 = Arrays.asList(orders.get(0), orders.get(1), orders.get(5));


        doReturn(o2).when(orderRepository).findByUserId(1L);
        doReturn(orderDTOs.get(0)).when(orderService).getOrderDTOFromOrder(o2.get(0));
        doReturn(orderDTOs.get(1)).when(orderService).getOrderDTOFromOrder(o2.get(1));
        doReturn(orderDTOs.get(5)).when(orderService).getOrderDTOFromOrder(o2.get(2));

        given()
                .when().get("/orders/by_user_id/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", hasItems(1, 2, 6))
                .body("state", hasItems("DONE", "CANCELED", "SHIPPED"));
    }

    @Test
    public void getValidOrder() {
        List<Order> o2 = Arrays.asList(orders.get(0), orders.get(1), orders.get(5));

        doReturn(o2.get(0)).when(orderRepository).findById(1L);
        doReturn(o2.get(1)).when(orderRepository).findById(2L);
        doReturn(o2.get(2)).when(orderRepository).findById(6L);
        doReturn(orderDTOs.get(0)).when(orderService).getOrderDTOFromOrder(o2.get(0));
        doReturn(orderDTOs.get(1)).when(orderService).getOrderDTOFromOrder(o2.get(1));
        doReturn(orderDTOs.get(5)).when(orderService).getOrderDTOFromOrder(o2.get(2));

        given()
                .when().get("/orders/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("state", is("DONE"));

        given()
                .when().get("/orders/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("state", is("CANCELED"));

        given()
                .when().get("/orders/6")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("state", is("SHIPPED"));
    }

     @Test
    public void getInvalidOrder() {
         doReturn(null).when(orderRepository).findById(1L);

         given()
                 .when().get("/orders/1")
                 .then()
                 .statusCode(404);
    }

    @Test
    public void shipOrder() {
        doReturn(orders.get(0)).when(orderRepository).findById(1L);
        doNothing().when(orderService).shipOrder(any(Order.class), any(OrderState.class));
        doReturn(orderDTOs.get(0)).when(orderService).getOrderDTOFromOrder(any(Order.class));

        given()
                .queryParam("action", "SHIP")
                .when().post("/orders/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", is(1));
    }

    @Test
    public void shipOrderInvalidAction() {
        given()
                .queryParam("action", "INVALID")
                .when().post("/orders/1")
                .then()
                .statusCode(404);
    }
}
