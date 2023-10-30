package com.advance.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.advance.entity.Complaint;
import com.advance.entity.ComplaintMessage;
import com.advance.entity.User;
import com.advance.enumeration.ComplaintReason;
import com.advance.enumeration.ComplaintStatus;
import com.advance.exception.ApiException;
import com.advance.repository.ComplaintRepository;
import com.advance.repository.UserRepository;
import com.advance.service.ComplaintService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ComplaintServiceImpl implements ComplaintService {

	private final ComplaintRepository complaintRepo;
	private final UserRepository userRepo;

	@Override
	public Complaint addComplaint(Complaint complaint) {
		try {
			complaint.setCreatedAt(LocalDate.now());
			complaint.setStatus(ComplaintStatus.OPENED);
			return complaintRepo.save(complaint);
		} catch (Exception e) {
			throw new ApiException("An error occured while adding the complaint!");
		}
	}

	@Override
	public List<Complaint> getComplaintByUserId(Long userId) {
		try {
			return complaintRepo.findByUser(userId);
		} catch (Exception e) {
			throw new ApiException("An error occured while retriving complaints by user id");
		}
	}

	@Override
	public List<Complaint> getComplaintByEmployeeId(Long employeeId) {
		try {
			return complaintRepo.findByEmployee(employeeId);
		} catch (Exception e) {
			throw new ApiException("An error occured while retriving complaints by employee id");
		}
	}

	@Override
	public List<Complaint> getComplaintsByStatus(ComplaintStatus status) {
		try {
			return complaintRepo.findByStatus(status);
		} catch (Exception e) {
			throw new ApiException("An error occured while retriving complaints by status");
		}
	}

	@Override
	public Complaint updateComplaint(Complaint complaint) {
		try {
			return complaintRepo.save(complaint);
		} catch (Exception e) {
			throw new ApiException("An error occured while updating complaint");
		}
	}

	@Override
	public Complaint updateComplaintSatus(Long complaintId, ComplaintStatus status) {
		try {
			return complaintRepo.updateComplaintStatus(complaintId, status);
		} catch (Exception e) {
			throw new ApiException("An error occured while updating complaint status");
		}
	}

	@Override
	public Boolean deleteComplaint(Long userId, Long complaintId) {
		try {
			Complaint complaint = complaintRepo.findByComplaintId(complaintId);
			User user = userRepo.findById(complaintId).orElseThrow(() -> new ApiException("user was not found!"));

			if (complaint == null) {
				throw new ApiException("Complaint not found!");
			}
			if (!compareComplaint(complaint, user)) {
				throw new ApiException("You can only delete your own complaint!");
			} else if (!complaint.getOrder().getIsPaid() || !complaint.getOrder().getIsDelivered()) {
				throw new ApiException("The order is not paid or delivered yet!");
			}
			complaint.setStatus(ComplaintStatus.RESOLVED);
			complaintRepo.save(complaint);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Complaint assignEmployee(Long complaintId, Long employeeId) {
		try {
			return complaintRepo.udpateComplaintEmployee(complaintId, employeeId);
		} catch (Exception e) {
			throw new ApiException("An error occured while updating complaint assigned employee");
		}
	}

	@Override
	public Complaint findById(Long userId, Long complaintId) {
		try {
			Optional<Complaint> complaint = complaintRepo.findById(complaintId);
			Optional<User> optionalUser = userRepo.findById(complaintId);

			if (complaint.isPresent() && optionalUser.isPresent()) {

				Complaint comp = complaint.get();
				User user = optionalUser.get();
				if (compareComplaint(comp, user)) {
					List<ComplaintMessage> messages = comp.getMessages();
					messages.stream().map(m -> {
						if (m.getSender().getId() != user.getId()) {
							m.setSeen(true);
						}
						return m;
					});
					return comp;
				}
			}
			throw new ApiException("Complaint was not found!");
		} catch (Exception e) {
			throw new ApiException("An error occured while retriving complaints by id");
		}
	}

	private boolean compareComplaint(Complaint complaint, User user) {
		if (complaint.getUser().getId().equals(user.getId()) || (user.getRole().name().equalsIgnoreCase("ROLE_EMPLOYEE")
				|| user.getRole().name().equalsIgnoreCase("ROLE_ADMIN")
				|| user.getRole().name().equalsIgnoreCase("ROLE_OWNER"))) {
			return true;
		}
		return false;

	}

	@Override
	public List<Complaint> getComplaintsByReasone(ComplaintReason reason) {
		try {
			return complaintRepo.findByReason(reason);
		} catch (Exception e) {
			throw new ApiException("An error occured while retriving complaints by reasons");
		}
	}

	@Override
	public Complaint addMessage(Long userId, Long complaintId, ComplaintMessage message) {
		try {
			Optional<User> optionalUser = userRepo.findById(complaintId);
			Optional<Complaint> optionalComplaint = complaintRepo.findById(complaintId);
			if (optionalUser.isPresent() && optionalComplaint.isPresent()) {
				User user = optionalUser.get();
				Complaint complaint = optionalComplaint.get();
				if (!compareComplaint(complaint, user)) {
					throw new ApiException("You can only add comment on your own complaint!");
				}
				message.setSender(user);
				message.setComplaint(complaint);
				message.setSeen(false);
				message.setSentAt(LocalDateTime.now());
				message.setUpdatedAt(LocalDateTime.now());
				List<ComplaintMessage> allMessages = complaint.getMessages();
				allMessages.add(message);
				complaint.setMessages(allMessages);
				return complaintRepo.save(complaint);
			}
			throw new ApiException("User or comaplint now found!");
		} catch (Exception e) {
			throw new ApiException("An error occured while adding new message!");
		}
	}

}
