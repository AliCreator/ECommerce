package com.advance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.advance.entity.Complaint;
import com.advance.enumeration.ComplaintReason;
import com.advance.enumeration.ComplaintStatus;

public interface ComplaintRepository extends PagingAndSortingRepository<Complaint, Long>, ListCrudRepository<Complaint, Long>{

	@Query(value = "SELECT * FROM complaints WHERE id = :complaintId", nativeQuery = true)
	Complaint findByComplaintId(@Param("complaintId") Long complaintId);
	
	@Query(value = "SELECT * FROM complaints WHERE user_id = :userId", nativeQuery = true)
	List<Complaint> findByUser(@Param("userId") Long userId);
	
	
	@Query(value = "SELECT * FROM complaints WHERE eomployee_id = :employeeId", nativeQuery = true)
	List<Complaint> findByEmployee(@Param("employeeId") Long employeeId);
	
	
	List<Complaint> findByStatus(ComplaintStatus status);
	List<Complaint> findByReason(ComplaintReason reasone);

	@Modifying
	@Query(value = "UPDATE complaints SET status = :status WHERE id = :complaintId", nativeQuery = true)
	Complaint updateComplaintStatus(@Param("complaintId") Long complaintId, @Param("status") ComplaintStatus status);
	
	@Modifying
	@Query(value ="UPDATE complaints SET employee_id = :employeeId WHERE id = :complaintId", nativeQuery = true)
	Complaint udpateComplaintEmployee(@Param("complaintId") Long complaintId, @Param("employeeId") Long employeeId);
}
