package com.advance.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class UpdateForm {

	@NotNull(message = "ID cannot be empty or null!")	
	private Long id; 
	
	@NotEmpty(message = "First name cannot be empty!")
	private String firstName; 
	
	@NotEmpty(message = "Last name cannot be empty!")
	private String lastName; 
	
	@Pattern(regexp = "^\\d{11}$", message = "Please enter 11 digit phone number!")
	private String phone; 
	
}
