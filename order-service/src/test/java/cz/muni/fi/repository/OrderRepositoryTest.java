package cz.muni.fi.repository;

import cz.muni.fi.entity.Order;
import cz.muni.fi.enums.OrderState;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@QuarkusTest
@Transactional
public class OrderRepositoryTest {
	@Inject
	public OrderRepository orderRepository;

	private Order o1;
	private Order o2;
	private Order o3;
	private Date date1;

	private final Long user1Id = Long.MAX_VALUE;

	@BeforeEach
	public void createOrders(){
		Calendar cal = Calendar.getInstance();
		cal.set(2015, Calendar.JANUARY, 1);
		date1 = cal.getTime();
		cal.set(2015, Calendar.APRIL, 5);
		Date date2 = cal.getTime();
		cal.set(1902, Calendar.JUNE, 6);
		Date date3 = cal.getTime();
		
		o1 = new Order();
		o1.setCreated(date1);
		o1.setState(OrderState.CANCELED);
		o1.setUserId(user1Id);

		o2 = new Order();
		o2.setCreated(date2);
		o2.setState(OrderState.RECEIVED);
		o2.setUserId(user1Id);

		o3 = new Order();
		o3.setCreated(date3);
		o3.setState(OrderState.CANCELED);
		o3.setUserId(2L);
		
		orderRepository.persist(o1);
		orderRepository.persist(o2);
		orderRepository.persist(o3);
	}

	@AfterEach
	public void deleteOrders() {
		orderRepository.delete(o1);
		orderRepository.delete(o2);
		orderRepository.delete(o3);
	}

	@Test
	public void nonExistentReturnsNull() {
		Order found = orderRepository.findById(Long.MAX_VALUE - 1);
		Assertions.assertNull(found);
	}
	
	@Test
	public void find() {
		Order found = orderRepository.findById(o1.getId());

		Assertions.assertNotNull(found);
        Assertions.assertEquals(0, found.getCreated().compareTo(date1));
		Assertions.assertEquals(OrderState.CANCELED, found.getState());
		Assertions.assertEquals(user1Id, found.getUserId());
	}

	@Test
	public void findByUser() {
		List<Order> orders = orderRepository.findByUserId(user1Id);
		Assertions.assertEquals(2, orders.size());
	}

	@Test
	public void getOrdersWithState() {
		List<Order> canceled = orderRepository.findByState(OrderState.CANCELED);
		List<Order> received = orderRepository.findByState(OrderState.RECEIVED);

		Assertions.assertTrue(canceled.size() >= 2);
        Assertions.assertFalse(received.isEmpty());
	}

	@Test
	public void getOrdersCreatedBetween() {
		Calendar cal = Calendar.getInstance();
		cal.set(1900, Calendar.JANUARY, 1);
		Date date1 = cal.getTime();
		cal.set(1901, Calendar.APRIL, 5);
		Date date2 = cal.getTime();

		Assertions.assertEquals(0, orderRepository.getOrdersCreatedBetween(date1, date2, OrderState.RECEIVED).size());

		cal.set(1902, Calendar.JANUARY, 1);
		Date date3 = cal.getTime();
		cal.set(1903, Calendar.MAY, 5);
		Date date4 = cal.getTime();
		Assertions.assertEquals(1, orderRepository.getOrdersCreatedBetween(date3, date4, OrderState.CANCELED).size());
	}
}
