package com.advance.service.implementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.advance.entity.Order;
import com.advance.enumeration.ComplaintStatus;
import com.advance.exception.ApiException;
import com.advance.repository.OrderRepository;
import com.advance.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepo;

	@Override
	public Order addOrder(Order order) {
		try {
			order.setCreatedAt(LocalDate.now());
			order.setIsPaid(false);
			order.setIsDelivered(false);
			return orderRepo.save(order);
		} catch (Exception e) {
			throw new ApiException("Something went wrong while adding order!");
		}
	}

	@Override
	public List<Order> getOrdersByUserId(Long userId) {
		try {
			return orderRepo.getOrdersByUserId(userId);

		} catch (Exception e) {
			throw new ApiException("Something went wrong while getting orders by user id!");
		}
	}

	@Override
	public List<Order> getOrdersByPaidStatus(Boolean status) {
		try {
			return orderRepo.findByIsPaid(status);
		} catch (Exception e) {
			throw new ApiException("Something went wrong while getting orders by payment status!");
		}
	}

	@Override
	public List<Order> getOrdersByDeliveredStatus(Boolean status) {
		try {
			return orderRepo.findByIsDelivered(status);
		} catch (Exception e) {
			throw new ApiException("Something went wrong while getting orders by delivery status!");
		}
	}

	@Override
	public List<Order> getOrdersByComplaintStatus(ComplaintStatus status) {
		try {
			return orderRepo.findByComplaint_Status(status);
		} catch (Exception e) {
			throw new ApiException("Something went wrong while getting orders by complaint status!");
		}
	}

	@Override
	public Order getOrderById(Long orderId) {
		try {
			Optional<Order> order = orderRepo.findById(orderId);
			if (order.isPresent()) {
				return order.get();
			}
			return null;
		} catch (Exception e) {
			throw new ApiException("Something went wrong while retriving order!");
		}
	}

	@Override
	public void updateOrder(Order order) {
		try {
			orderRepo.save(order);
		} catch (Exception e) {
			throw new ApiException("Something went wrong while updating order!");
		}

	}

	@Override
	public void updateOrderPayment(Long orderId, Boolean status) {
		try {
			orderRepo.updateOrderPayment(orderId, status);
		} catch (Exception e) {
			throw new ApiException("Something went wrong while updating order payment status!");
		}

	}

	@Override
	public void updateOrderDelivery(Long orderId, Boolean status) {
		try {
			orderRepo.updateOrderDelivery(orderId, status);
		} catch (Exception e) {
			throw new ApiException("Something went wrong while updating order delivery status!");
		}
	}

	@Override
	public void updateOrderShippingPrice(Long orderId, Double shippingPrice) {
		try {
			orderRepo.updateOrderShippingPrice(orderId, shippingPrice);
		} catch (Exception e) {
			throw new ApiException("Something went wrong while updating order shipping price!");
		}

	}

	@Override
	public void updateOrderByDiscount(Long orderId, Double discount) {
		try {
			orderRepo.updateOrderTotalPrice(orderId, discount);
		} catch (Exception e) {
			throw new ApiException("Something went wrong while updating order order discount!");
		}

	}

	@Override
	public Boolean deleteOrder(Long orderId) {
		try {
			orderRepo.deleteById(orderId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
