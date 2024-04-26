package cz.muni.fi.sampledata;

import cz.muni.fi.entity.Order;
import cz.muni.fi.entity.OrderItem;
import cz.muni.fi.enums.OrderState;
import cz.muni.fi.service.OrderService;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Random;

@ApplicationScoped
@Transactional
public class SampleDataLoading {
    final static Logger log = LoggerFactory.getLogger(SampleDataLoading.class);

    @Inject
    private OrderService orderService;

    private static Date daysBeforeNow(int days) {
        return Date.from(ZonedDateTime.now().minusDays(days).toInstant());
    }

    private void createOrder(Long userId, Date created, OrderState state, OrderItem... items) {
        Order order = new Order();
        order.setUserId(userId);
        order.setCreated(created);
        order.setState(state);
        for (OrderItem item : items) {
            order.addOrderItem(item);
        }
        orderService.createOrder(order);
    }

    private OrderItem createOrderItem(Long productId, int amount) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(productId);
        orderItem.setAmount(amount);
        return orderItem;
    }
    @Startup
    public void loadProductSampleData() {
        createOrder(1L, daysBeforeNow(10), OrderState.DONE, createOrderItem(24L, 5), createOrderItem(10L, 1));
        createOrder(1L, daysBeforeNow(6), OrderState.SHIPPED, createOrderItem(13L, 3), createOrderItem(8L, 3));
        createOrder(1L, daysBeforeNow(3), OrderState.CANCELED, createOrderItem(24L, 10), createOrderItem(13L, 1));
        createOrder(1L, daysBeforeNow(2), OrderState.RECEIVED, createOrderItem(24L, 10), createOrderItem(13L, 1));
        createOrder(2L, daysBeforeNow(1), OrderState.RECEIVED, createOrderItem(24L, 1), createOrderItem(13L, 1), createOrderItem(8L, 1));
        createOrder(3L, daysBeforeNow(1), OrderState.RECEIVED, createOrderItem(24L, 15), createOrderItem(13L, 7), createOrderItem(8L, 2));

        this.createTestOrders();

        log.info("Loaded eShop orders.");
    }

    private void createTestOrders() {
        Random random = new Random();
        OrderState[] orderStates = OrderState.values();

        for (int i = 1; i <= 1000; i++) {
            createOrder(random.nextLong(1000) + 1, daysBeforeNow(random.nextInt(100)), orderStates[i % 4], createOrderItem(random.nextLong(1000) + 1, random.nextInt(5) + 1));
        }
    }
}
