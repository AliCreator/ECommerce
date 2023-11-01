package com.advance.utils;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.OutputStream;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import com.advance.entity.MyResponse;
import com.advance.exception.ApiException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ExceptionUtils {

	 public static void processError(HttpServletRequest request, HttpServletResponse response, Exception exception) {
	        if(exception instanceof ApiException || exception instanceof DisabledException || exception instanceof LockedException ||
	                exception instanceof BadCredentialsException || exception instanceof InvalidClaimException) {
	            MyResponse httpResponse = getHttpResponse(response, exception.getMessage(), HttpStatus.BAD_REQUEST);
	            writeResponse(response, httpResponse);
	        } else if (exception instanceof TokenExpiredException) {
	        	MyResponse httpResponse = getHttpResponse(response, exception.getMessage(), HttpStatus.UNAUTHORIZED);
	            writeResponse(response, httpResponse);
	        } else {
	        	MyResponse httpResponse = getHttpResponse(response, "An error occurred. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
	            writeResponse(response, httpResponse);
	        }
	        log.error(exception.getMessage());
	    }
	 
	 private static void writeResponse(HttpServletResponse response, MyResponse httpResponse) {
	        OutputStream out;
	        try{
	            out = response.getOutputStream();
	            ObjectMapper mapper = new ObjectMapper();
	            mapper.writeValue(out, httpResponse);
	            out.flush();
	        }catch (Exception exception) {
	            log.error(exception.getMessage());
	            exception.printStackTrace();
	        }
	    }
	 
	 private static MyResponse getHttpResponse(HttpServletResponse response, String message, HttpStatus httpStatus) {
		 MyResponse httpResponse = MyResponse.builder()
	                .timestamp(LocalDateTime.now().toString())
	                .reason(message)
	                .httpStatus(httpStatus)
	                .status(httpStatus.value())
	                .build();
	        response.setContentType(APPLICATION_JSON_VALUE);
	        response.setStatus(httpStatus.value());
	        return httpResponse;
	    }
}
