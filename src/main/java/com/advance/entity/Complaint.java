package com.advance.entity;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
@SuperBuilder
@Entity
@Table(name = "Complaints")
public class Complaint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id")
	@JsonIgnore
	private Order order;
	
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user; 
	
	@ManyToOne
	@JoinColumn(name = "employee_id")
	@JsonIgnore
	private User employee;
	
	@CreationTimestamp
	private LocalDate createdAt;
	
	@UpdateTimestamp
	private LocalDate updatedAt;
	
	@OneToMany(mappedBy = "complaint")
	private List<ComplaintMessage> messages;
	
}