package com.advance.entity;


import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name; 
	private String description; 
	private Float price; 
	private Boolean availability; 
	private Integer inStock; 
//	private Category category; 
	private Boolean isFeatured;
	private Integer numReviews; 
	private Double rating; 
	private String coverPhoto; 
	private Long boughtNum;
	private List<String> allPhotos; 
	
//	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinColumn(name = "detail_id")
//	private Detail details;
	
	
	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDate createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDate updateAt;
	
}