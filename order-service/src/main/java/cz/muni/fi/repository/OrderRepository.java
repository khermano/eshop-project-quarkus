package cz.muni.fi.repository;

import cz.muni.fi.entity.Order;
import cz.muni.fi.enums.OrderState;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<Order> {
	public List<Order> findByUserId(Long userId) {
		return list("userId", userId);
	}

	public List<Order> findByState(OrderState state) {
		return list("state", state);
	}

	public List<Order> getOrdersCreatedBetween(Date startDate, Date endDate, OrderState state) {
		return list("SELECT o FROM Order o WHERE o.state = ?1 AND  o.created BETWEEN ?2 AND ?3", state, startDate, endDate);
	}
}
