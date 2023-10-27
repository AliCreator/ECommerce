package com.advance.filter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.advance.provider.TokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

	private static final String JWT = "jwt";
	private final TokenProvider provider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();
		String jwt = null;
		for (Cookie cookie : cookies) {
			if (JWT.equals(cookie.getName())) {
				jwt = cookie.getValue();

				break;
			}
		}

		if (jwt != null && !jwt.isEmpty()) {
			Long id = getUser(request, jwt);
			if (provider.isTokenValid(id, jwt)) {
				List<GrantedAuthority> authorities = provider.getAuthorities(jwt);
				Authentication authentication = provider.getAuthentication(id, authorities, request);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				SecurityContextHolder.clearContext();
			}
		}
		filterChain.doFilter(request, response);
	}

	private Long getUser(HttpServletRequest request, String jwt) {
		return provider.getSubjet(jwt, request);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		// Whitelisted URIs for bypassing the filter
		final Set<String> WHITE_LISTED_URIS = Set.of("/api/auth/register", "/api/auth/login", "/api/auth/forgot/password"); // adjust paths accordingly

		// 1. Check for OPTIONS requests
		if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
			return true;
		}

		// 2. Check for missing JWT cookie
		Cookie[] cookies = request.getCookies();
		boolean jwtCookiePresent = false;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("jwt".equalsIgnoreCase(cookie.getName())) { // assuming "jwt" is the name of your JWT cookie
					jwtCookiePresent = true;
					break;
				}
			}
		}
		if (!jwtCookiePresent) {
			return true;
		}

		// 3. Check for whitelisted URIs
		return WHITE_LISTED_URIS.contains(request.getRequestURI());
	}

}