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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.advance.entity.Complaint;
import com.advance.entity.ComplaintMessage;
import com.advance.entity.MyResponse;
import com.advance.entity.Order;
import com.advance.entity.User;
import com.advance.enumeration.ComplaintReason;
import com.advance.enumeration.ComplaintStatus;
import com.advance.service.ComplaintService;
import com.advance.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintResources {

	private final ComplaintService complaintService;
	private final OrderService orderService;

	@PostMapping("/add/{orderId}")
	public ResponseEntity<MyResponse> addComplaint(@AuthenticationPrincipal User user,
			@PathVariable("orderId") Long orderId, @RequestBody Complaint complaint) {
		Order order = orderService.getOrderById(orderId);
		complaint.setUser(user);
		complaint.setOrder(order);
		Complaint newComplaint = complaintService.addComplaint(complaint);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Complaint added").status(HttpStatus.CREATED.value()).httpStatus(HttpStatus.CREATED)
				.data(Map.of("complaint", newComplaint)).build();

		return ResponseEntity.created(getURI()).body(myResponse);
	}

	@GetMapping("/find/{complaintId}/complaint")
	public ResponseEntity<MyResponse> getComplaintById(@PathVariable("complaintId") Long complaintId,
			@AuthenticationPrincipal User user) {
		Complaint complaint = complaintService.findById(user.getId(), complaintId);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Complaint retrieved").status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK)
				.data(Map.of("complaint", complaint)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/find/all/user/{userId}")
	public ResponseEntity<MyResponse> getAllComplaintsByUserId(@PathVariable("userId") Long userId,
			@AuthenticationPrincipal User user) {
		List<Complaint> allComplaints = complaintService.getComplaintByUserId(userId);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Complaints retrieved").status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK)
				.data(Map.of("complaints", allComplaints)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/find/all/employee/{employeeId}")
	public ResponseEntity<MyResponse> getAllComplaintsByEmployeeId(@PathVariable("employeeId") Long employeeId) {
		List<Complaint> allComplaints = complaintService.getComplaintByEmployeeId(employeeId);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Complaints retrieved").status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK)
				.data(Map.of("complaints", allComplaints)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/find/all/status/{status}")
	public ResponseEntity<MyResponse> getAllComplaintsByStatus(@PathVariable("status") String status) {
		List<Complaint> allComplaints = complaintService.getComplaintsByStatus(ComplaintStatus.valueOf(status));
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Complaints retrieved").status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK)
				.data(Map.of("complaints", allComplaints)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/find/all/reason/{reason}")
	public ResponseEntity<MyResponse> getAllComplaintsByReason(@PathVariable("reason") String reason) {
		List<Complaint> allComplaints = complaintService.getComplaintsByReasone(ComplaintReason.valueOf(reason));
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Complaints retrieved").status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK)
				.data(Map.of("complaints", allComplaints)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/update/employee/{complaintId}/{employeeId}")
	public ResponseEntity<MyResponse> updateComplaintEmployee(@PathVariable("employeeId") Long employeeId,
			@PathVariable("complaintId") Long complaintId) {
		Complaint complaint = complaintService.assignEmployee(complaintId, employeeId);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Complaints updated with new employee assignment").status(HttpStatus.OK.value())
				.httpStatus(HttpStatus.OK).data(Map.of("complaint", complaint)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/update")
	public ResponseEntity<MyResponse> updateComplaint(@RequestBody Complaint complaint) {
		Complaint updateComplaint = complaintService.updateComplaint(complaint);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Complaints updated").status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK)
				.data(Map.of("complaint", updateComplaint)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/update/comment/{complaintId}")
	public ResponseEntity<MyResponse> addNewComment(@AuthenticationPrincipal User user,
			@PathVariable("complaintId") Long complaintId, @RequestBody ComplaintMessage message) {
		Complaint complaint = complaintService.addMessage(user.getId(), complaintId, message);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Message added").status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK)
				.data(Map.of("complaint", complaint)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@DeleteMapping("/delete/{complaintId}")
	public ResponseEntity<MyResponse> deleteComplaint(@AuthenticationPrincipal User user,
			@PathVariable("complaintId") Long complaintId) {
		Boolean status = complaintService.deleteComplaint(user.getId(), complaintId);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.status(status ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
				.httpStatus(status ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
				.message(status ? "Complaint deleted" : "An error occured").build();
		return ResponseEntity.ok().body(myResponse);
	}

	private URI getURI() {
		return URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/get/<complaintId>").toUriString());
	}
}
