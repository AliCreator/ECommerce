package com.advance.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity
public class VerificationCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDateTime createdAt; 
	private LocalDateTime expiredAt; 
	
	private String code; 
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user; 
	
	
}
