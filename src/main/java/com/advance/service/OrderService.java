package com.advance.service;

import java.util.List;

import com.advance.entity.Order;
import com.advance.enumeration.ComplaintStatus;

public interface OrderService {

	Order addOrder(Order order);

	List<Order> getOrdersByUserId(Long userId);

	List<Order> getOrdersByPaidStatus(Boolean status);

	List<Order> getOrdersByDeliveredStatus(Boolean status);

	List<Order> getOrdersByComplaintStatus(ComplaintStatus status);

	Order getOrderById(Long orderId);

	void updateOrder(Order order);

	void updateOrderPayment(Long orderId, Boolean status);

	void updateOrderDelivery(Long orderId, Boolean status);

	void updateOrderShippingPrice(Long orderId, Double shippingPrice);

	void updateOrderByDiscount(Long orderId, Double discount);

	Boolean deleteOrder(Long orderId);
}
