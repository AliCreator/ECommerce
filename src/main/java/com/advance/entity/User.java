package com.advance.entity;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.advance.enumeration.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
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
@Table(name = "Users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String firstName; 
	private String lastName;
	private String email; 
	private String password; 
	private String profilePicture; 
	private String phone;
	
	@OneToMany(fetch = FetchType.EAGER)
	 @JoinTable(
		        name = "user_orders", 
		        joinColumns = @JoinColumn(name = "user_id"), 
		        inverseJoinColumns = @JoinColumn(name = "order_id")
		    )
	@JsonIgnore
	private List<Order> orders;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="user_wishlist", 
			joinColumns =  @JoinColumn(name = "user_id"), 
			inverseJoinColumns = @JoinColumn(name = "product_id")
			)
	@JsonIgnore
	private List<Product> wishList; 
	
	private Boolean isEnabled;
	private Boolean isUsringMfa;
	
	@Enumerated(EnumType.STRING)
	private RoleType role;
	
	@CreationTimestamp
	private LocalDate createdAt;
	
	@UpdateTimestamp
	private LocalDate updateAt;
	
}
