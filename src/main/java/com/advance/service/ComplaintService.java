package com.advance.service;

import java.util.List;

import com.advance.entity.Complaint;
import com.advance.entity.ComplaintMessage;
import com.advance.enumeration.ComplaintReason;
import com.advance.enumeration.ComplaintStatus;

public interface ComplaintService {

	Complaint addComplaint(Complaint complaint); 
	
	Complaint findById(Long userId, Long complaintId); 
	
	List<Complaint> getComplaintByEmployeeId(Long employeeId); 

	List<Complaint> getComplaintByUserId(Long userId); 
	
	List<Complaint> getComplaintsByStatus(ComplaintStatus status); 
	
	List<Complaint> getComplaintsByReasone(ComplaintReason reason);
	Complaint addMessage(Long userId, Long complaintId,ComplaintMessage message);
	Complaint assignEmployee(Long complaintId, Long employeeId); 
	Complaint updateComplaintSatus(Long complaintId, ComplaintStatus status); 
	Complaint updateComplaint(Complaint complaint); 
	
	Boolean deleteComplaint(Long userId, Long complaintId); 
}
