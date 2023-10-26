package com.advance.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.advance.entity.MyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("You do not have enough authority to access this endpoint!").httpStatus(HttpStatus.FORBIDDEN)
				.status(HttpStatus.FORBIDDEN.value()).build();

		response.setContentType(APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.FORBIDDEN.value());

		OutputStream stream = response.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();

		mapper.writeValue(stream, myResponse);

		stream.flush();

	}

}
