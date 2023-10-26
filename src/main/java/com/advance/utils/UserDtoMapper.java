package com.advance.utils;

import org.springframework.beans.BeanUtils;

import com.advance.dto.UserDTO;
import com.advance.entity.User;

public class UserDtoMapper {

	public static UserDTO convertToUserDTO(User user) {
		UserDTO dto = new UserDTO();
		BeanUtils.copyProperties(user, dto);
		return dto;
	}
	
	public static User convertToUser(UserDTO dto) {
		User user = new User();
		BeanUtils.copyProperties(dto, user);
		return user;
	}
}

