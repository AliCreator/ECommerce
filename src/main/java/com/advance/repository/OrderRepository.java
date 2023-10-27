package com.advance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.advance.entity.Order;
import com.advance.enumeration.ComplaintStatus;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long>, ListCrudRepository<Order, Long> {

	@Query(value = "SELECT * FROM orders WHERE user_id = :userId", nativeQuery = true)
	List<Order> getOrdersByUserId(@Param("userId") Long userId);

	List<Order> findByIsPaid(Boolean isPaid);

	List<Order> findByIsDelivered(Boolean isDelivered);

	List<Order> findByComplaint_Status(ComplaintStatus complaint);

	@Modifying
	@Query(value = "UPDATE orders SET is_paid = :status, paid_at = NOW() WHERE id = :orderId", nativeQuery = true)
	void updateOrderPayment(@Param("orderId") Long orderId, @Param("status") Boolean status);

	@Modifying
	@Query(value = "UPDATE orders SET is_delivered = :status, delivered_at = NOW() WHERE id = :orderId", nativeQuery = true)
	void updateOrderDelivery(@Param("orderId") Long orderId, @Param("status") Boolean status);
	
	@Modifying
	@Query(value = "UPDATE orders SET shipping_price = :shippingPrice WHERE id = :orderId", nativeQuery = true)
	void updateOrderShippingPrice(@Param("orderId") Long orderId, @Param("shippingPrice") Double shippingPrice);

	@Modifying
	@Query(value = "UPDATE orders o SET o.total_price = o.total_price - :totalPrice WHERE id = :orderId", nativeQuery = true)
	void updateOrderTotalPrice(@Param("orderId") Long orderId, @Param("totalPrice") Double totalPrice); 

	
}
