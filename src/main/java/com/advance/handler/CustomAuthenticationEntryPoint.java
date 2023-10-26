package com.advance.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.advance.entity.MyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("You need to login!").httpStatus(HttpStatus.UNAUTHORIZED)
				.status(HttpStatus.UNAUTHORIZED.value()).build();

		response.setContentType(APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());

		OutputStream stream = response.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();

		mapper.writeValue(stream, myResponse);
		stream.flush();
	}

}
