package com.advance.entity;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.advance.dto.UserDTO;
import com.advance.enumeration.RoleType;
import com.advance.enumeration.UserAuthority;
import com.advance.utils.UserDtoMapper;
import com.advance.utils.UserRoleUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {


	private static final long serialVersionUID = 1L;
	private final User user;
	private final RoleType role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<UserAuthority> authorities = UserRoleUtils.getAuthoritiesForRole(this.role);
		return authorities.stream().map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.name()))
				.collect(Collectors.toSet());
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.getIsEnabled();
	}
	
	public UserDTO getUser() {
		return UserDtoMapper.convertToUserDTO(user);
	}

}
