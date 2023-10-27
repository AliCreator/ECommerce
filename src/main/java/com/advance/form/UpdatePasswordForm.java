package com.advance.form;

import jakarta.validation.constraints.NotNull;
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
public class UpdatePasswordForm {

	@NotNull(message = "Current password field cannot be empty!")
	private String currentPassword; 
	
	@NotNull(message = "New password field cannot be empty!")
	private String newPassword; 
	
	@NotNull(message = "Confirm password field cannot be empty!")
	private String confirmPassword;
}
