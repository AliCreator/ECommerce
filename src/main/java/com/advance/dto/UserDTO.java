package com.advance.dto;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.advance.entity.Order;
import com.advance.entity.Product;
import com.advance.enumeration.RoleType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(value = Include.NON_DEFAULT)
public class UserDTO {

	private Long id;
	
	private String firstName; 
	private String lastName;
	private String email; 
	private String profilePicture; 
	
	private List<Order> orders;
	private List<Product> wishList; 
	
	private Boolean isEnabled;
	
	private RoleType role;
	
	@CreationTimestamp
	private LocalDate createdAt;
	
	@UpdateTimestamp
	private LocalDate updateAt;
}