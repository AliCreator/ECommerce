package com.advance.resources;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.advance.entity.MyResponse;
import com.advance.entity.Order;
import com.advance.entity.User;
import com.advance.enumeration.ComplaintStatus;
import com.advance.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderResources {

	private final OrderService orderService;

	@PostMapping("/add")
	public ResponseEntity<MyResponse> addOrder(@AuthenticationPrincipal User user, @RequestBody Order order) {
		order.setUser(user);
		Order newOrder = orderService.addOrder(order);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString()).message("Order added")
				.httpStatus(HttpStatus.CREATED).status(HttpStatus.CREATED.value()).data(Map.of("order", newOrder))
				.build();

		return ResponseEntity.created(getURI()).body(myResponse);
	}

	@GetMapping("/find/id/{id}")
	public ResponseEntity<MyResponse> getOrderById(@PathVariable("id") Long id) {
		Order orderById = orderService.getOrderById(id);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Order retrieved!").status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK)
				.data(Map.of("order", orderById)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/find/all/user/{id}")
	public ResponseEntity<MyResponse> getOrdersByUserId(@PathVariable("id") Long id) {
		List<Order> ordersByUserId = orderService.getOrdersByUserId(id);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Orders retrieved!").status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK)
				.data(Map.of("orders", ordersByUserId)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/find/all/paid")
	public ResponseEntity<MyResponse> getOrdersByPaymentStatus(@RequestParam("status") Boolean status) {
		List<Order> ordersByPaidStatus = orderService.getOrdersByPaidStatus(status);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Orders retrieved!").status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK)
				.data(Map.of("orders", ordersByPaidStatus)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/find/all/delivered")
	public ResponseEntity<MyResponse> getOrdersByDeliveryStatus(@RequestParam("status") Boolean status) {
		List<Order> ordersByDeliveredStatus = orderService.getOrdersByDeliveredStatus(status);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Orders retrieved!").status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK)
				.data(Map.of("orders", ordersByDeliveredStatus)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/find/all/complaint")
	public ResponseEntity<MyResponse> getOrdersByComplaintStatus(@RequestParam("status") ComplaintStatus status) {
		List<Order> ordersByComplaintStatus = orderService.getOrdersByComplaintStatus(status);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Orders retrieved!").status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK)
				.data(Map.of("orders", ordersByComplaintStatus)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/update")
	public ResponseEntity<MyResponse> updateOrder(@RequestBody Order order) {
		orderService.updateOrder(order);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString()).message("Order updated!")
				.status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/update/{id}/payment")
	public ResponseEntity<MyResponse> updateOrderPaymentStatus(@PathVariable("id") Long id,
			@RequestParam("status") Boolean status) {
		orderService.updateOrderPayment(id, status);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString()).message("Order updated!")
				.status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/update/{id}/delivery")
	public ResponseEntity<MyResponse> updateOrderDeliveryStatus(@PathVariable("id") Long id,
			@RequestParam("status") Boolean status) {
		orderService.updateOrderDelivery(id, status);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString()).message("Order updated!")
				.status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/update/{id}/shipping")
	public ResponseEntity<MyResponse> updateOrderShippingPrice(@PathVariable("id") Long id,
			@RequestParam("shipping") Double shipping) {
		orderService.updateOrderShippingPrice(id, shipping);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString()).message("Order updated!")
				.status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/update/{id}/discount")
	public ResponseEntity<MyResponse> updateOrderWithDiscountPrice(@PathVariable("id") Long id,
			@RequestParam("discount") Double discount) {
		orderService.updateOrderByDiscount(id, discount);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString()).message("Order updated!")
				.status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<MyResponse> deleteOrder(@PathVariable("id") Long id) {
		Boolean status = orderService.deleteOrder(id);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.status(status ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
				.httpStatus(status ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
				.message(status ? "Order deleted" : "An error occured").build();
		return ResponseEntity.ok().body(myResponse);
	}

	private URI getURI() {
		return URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/find/id/<orderId>").toUriString());
	}
}
