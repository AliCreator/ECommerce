package com.advance.utils;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.advance.enumeration.RoleType;
import com.advance.enumeration.UserAuthority;

public class UserRoleUtils {

	public static Set<UserAuthority> getAuthoritiesForRole(RoleType role) {
		switch (role) {
		case ROLE_OWNER:
			return EnumSet.allOf(UserAuthority.class);
		case ROLE_ADMIN:
			return EnumSet.of(UserAuthority.READ_COMPLAINT, UserAuthority.READ_ORDER, UserAuthority.READ_PRODUCT,
					UserAuthority.READ_USER, UserAuthority.WRITE_PRODUCT, UserAuthority.WRITE_ORDER,
					UserAuthority.WRITE_COMPLATE, UserAuthority.UPDATE_COMPLAINT, UserAuthority.UPDATE_ORDER,
					UserAuthority.DELETE_COMPLAINT, UserAuthority.DELETE_ORDER, UserAuthority.DELETE_PRODUCT);
		case ROLE_CASHIER:
			return EnumSet.of(UserAuthority.READ_COMPLAINT, UserAuthority.READ_ORDER, UserAuthority.READ_PRODUCT,
					UserAuthority.READ_USER, UserAuthority.WRITE_COMPLATE, UserAuthority.WRITE_PRODUCT,
					UserAuthority.DELETE_COMPLAINT, UserAuthority.DELETE_ORDER, UserAuthority.DELETE_PRODUCT,
					UserAuthority.UPDATE_COMPLAINT, UserAuthority.UPDATE_ORDER);
		case ROLE_EMPLOYEE:
			return EnumSet.of(UserAuthority.READ_COMPLAINT, UserAuthority.READ_ORDER, UserAuthority.READ_PRODUCT,
					UserAuthority.READ_USER, UserAuthority.WRITE_PRODUCT, UserAuthority.UPDATE_PRODUCT,
					UserAuthority.UPDATE_COMPLAINT);
		case ROLE_USER:
			return EnumSet.of(UserAuthority.READ_COMPLAINT, UserAuthority.READ_ORDER, UserAuthority.READ_PRODUCT,
					UserAuthority.READ_USER, UserAuthority.WRITE_COMPLATE, UserAuthority.UPDATE_COMPLAINT,
					UserAuthority.WRITE_PRODUCT);
		default:
			return Collections.emptySet();
		}
	}
}
