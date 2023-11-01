package com.advance.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.advance.dto.UserDTO;
import com.advance.entity.User;
import com.advance.enumeration.RoleType;
import com.advance.form.UpdateForm;

import jakarta.validation.Valid;

public interface UserService {

	UserDTO getUserById(Long userId);
	UserDTO getUserByEmail(String email);
	UserDTO register(User user); 
	List<UserDTO> getAllUsers();
	Page<UserDTO> getAllUsersWithPagination(int pageNumber, int pageSize);
	void sendAccountVerificationCode(String email);
	void sendResetPasswordUrl(String email);
	UserDTO verifyPasswordKey(String key);
	void renewPassword(String key, String password, String confirmPassword);
	UserDTO verifyAccount(String key);
	void updateUserDetails(@Valid UpdateForm user);
	void updateUserPassword(Long id, String currentPassword, String newPassword, String confirmNewPassword);
	void updateUserRole(Long id, RoleType roleName);
	void toggleMfa(Long userId, Boolean status);
	
}
