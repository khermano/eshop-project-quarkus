package cz.muni.fi.service;

import cz.muni.fi.dto.OrderDTO;
import cz.muni.fi.entity.Order;
import cz.muni.fi.enums.OrderState;
import java.util.List;

public interface OrderService {
	void createOrder(Order order);

	List<Order> getAllOrdersLastWeek(OrderState state);

	void shipOrder(Order order, OrderState state);

	OrderDTO getOrderDTOFromOrder(Order order);
}
