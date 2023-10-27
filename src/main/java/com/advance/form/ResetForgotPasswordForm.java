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
public class ResetForgotPasswordForm {

	@NotNull(message = "Please provide your new password")
	private String password; 
	
	@NotNull(message = "Please provide the confirm password")
	private String confirmPassword;
}
