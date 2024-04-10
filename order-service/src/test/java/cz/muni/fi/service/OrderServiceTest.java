package cz.muni.fi.service;

import cz.muni.fi.entity.Order;
import cz.muni.fi.enums.OrderState;
import cz.muni.fi.repository.OrderRepository;
import cz.muni.fi.service.impl.OrderServiceImpl;
import io.quarkus.test.junit.QuarkusTest;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order orderReceived;

    private Order orderShipped;

    @BeforeEach
    public void createOrders() {
        orderReceived = new Order();
        orderShipped = new Order();

        orderReceived.setState(OrderState.RECEIVED);
        orderShipped.setState(OrderState.SHIPPED);
    }

    @BeforeEach
    public void setup() throws ServiceException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllOrdersLastWeekTest() {
        Calendar cal = Calendar.getInstance();
        cal.set(2015, Calendar.FEBRUARY, 10, 0, 0, 0);
        cal.add(Calendar.DAY_OF_MONTH, -7);

        Order o = new Order();
        o.setId(4L);
        o.setState(OrderState.CANCELED);
        o.setCreated(new Date());

        when(orderRepository.getOrdersCreatedBetween(any(Date.class), any(Date.class), any())).thenReturn(Collections.singletonList(o));

        List<Order> orders = orderService.getAllOrdersLastWeek(OrderState.CANCELED);
        Assertions.assertEquals(1, orders.size());
        Assertions.assertEquals(4L, orders.get(0).getId());
    }

    @Test
    public void ship() {
        orderService.shipOrder(orderReceived, OrderState.SHIPPED);
        Assertions.assertEquals(orderReceived.getState(), OrderState.SHIPPED);
    }

    @Test
    public void finish() {
        orderService.shipOrder(orderShipped, OrderState.DONE);
        Assertions.assertEquals(orderShipped.getState(), OrderState.DONE);
    }

    @Test
    public void cancel() {
        orderService.shipOrder(orderReceived, OrderState.CANCELED);
        Assertions.assertEquals(orderReceived.getState(), OrderState.CANCELED);
    }
}
