package com.advance.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.advance.entity.User;
import com.advance.entity.UserPrincipal;
import com.advance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			return convertToUserPrincipal(userRepository.findByEmail(email).get());
		} catch (Exception e) {
			throw new UsernameNotFoundException("User was not found!");
		}
	}

	private UserDetails convertToUserPrincipal(User user) {
		return new UserPrincipal(user, user.getRole());

	}
}
