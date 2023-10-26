package com.advance.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.advance.filter.AuthenticationFilter;
import com.advance.handler.CustomAccessDeniedHandler;
import com.advance.handler.CustomAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private static final String[] PUBLIC_URLS = { "/api/auth/login/**", "/api/auth/register/**",
			"/api/auth/resetpassword/**", "/api/auth/verify/code/**", "/api/auth/verify/password/**",
			"/api/auth/verify/account/**", "/api/auth/new/password/**", "/api/products/get/**" };
	private final Integer STRENGTH = 12;

	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	private final AuthenticationFilter authenticationFilter;
	private final UserDetailsService detailsService;
	

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.cors(withDefaults()).csrf(c -> c.disable());
		http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_URLS).permitAll());
		http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
		http.exceptionHandling(
				x -> x.accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint));
		http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder(STRENGTH);
	}
	
	@Bean
	public AuthenticationManager authManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(encoder());
		provider.setUserDetailsService(detailsService);
		return new ProviderManager(provider);
	}

}
