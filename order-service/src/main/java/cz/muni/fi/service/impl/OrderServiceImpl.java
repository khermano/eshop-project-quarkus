package cz.muni.fi.service.impl;

import cz.muni.fi.dto.OrderDTO;
import cz.muni.fi.dto.OrderItemDTO;
import cz.muni.fi.dto.ProductDTO;
import cz.muni.fi.entity.Order;
import cz.muni.fi.entity.OrderItem;
import cz.muni.fi.enums.OrderState;
import cz.muni.fi.exception.EshopServiceException;
//import cz.muni.fi.feign.ProductInterface;
//import cz.muni.fi.feign.UserInterface;
import cz.muni.fi.repository.OrderItemRepository;
import cz.muni.fi.repository.OrderRepository;
import cz.muni.fi.service.BeanMappingService;
import cz.muni.fi.service.OrderService;
import cz.muni.fi.utils.Transition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderItemRepository orderItemRepository;

    @Inject
	private OrderRepository orderRepository;

//	@Inject
//	private UserInterface userInterface;
//
//	@Inject
//	private ProductInterface productInterface;

	@Inject
	private BeanMappingService beanMappingService;

    @Override
    public void createOrder(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItemRepository.persist(orderItem);
        }
        orderRepository.persist(order);
    }

	@Override
	public List<Order> getAllOrdersLastWeek(OrderState state) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, -7);
		Date lastWeek = calendar.getTime();

        return orderRepository.getOrdersCreatedBetween(lastWeek, new Date(), state);
	}

	private final Set<Transition> allowedTransitions = new HashSet<>();
	{
		allowedTransitions.add(new Transition(OrderState.RECEIVED,
				OrderState.SHIPPED));
		allowedTransitions.add(new Transition(OrderState.RECEIVED,
				OrderState.CANCELED));
		allowedTransitions.add(new Transition(OrderState.SHIPPED,
				OrderState.DONE));
	}

	@Override
	public void shipOrder(Order order, OrderState state) {
		checkTransition(order.getState(), state);
		order.setState(state);
		orderRepository.persist(order);
	}

//	@Override
//	public OrderDTO getOrderDTOFromOrder(Order order) {
//		OrderDTO orderDTO = beanMappingService.mapTo(order, OrderDTO.class);
//		orderDTO.setUser(userInterface.getUser(order.getUserId()).getBody());
//		List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
//
//		for (OrderItem orderItem : order.getOrderItems()) {
//			OrderItemDTO orderItemDTO = beanMappingService.mapTo(orderItem, OrderItemDTO.class);
//			ProductDTO productDTO = productInterface.getProduct(orderItem.getProductId()).getBody();
//			orderItemDTO.setProduct(productDTO);
//			orderItemDTOs.add(orderItemDTO);
//		}
//
//		orderDTO.setOrderItems(orderItemDTOs);
//		return orderDTO;
//	}

	private void checkTransition(OrderState oldState, OrderState newState) {
		if (!allowedTransitions.contains(new Transition(oldState, newState)))
			throw new EshopServiceException("The transition from: " + oldState
					+ " to " + newState + " is not allowed!");
	}
}
