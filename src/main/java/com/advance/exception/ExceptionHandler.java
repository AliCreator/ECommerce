package com.advance.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.advance.entity.MyResponse;

public class ExceptionHandler extends ResponseEntityExceptionHandler implements ErrorController{

	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode statusCode, WebRequest request) {
		return new ResponseEntity<Object>(	
				MyResponse.builder().timestamp(LocalDateTime.now().toString()).message(ex.getMessage())
						.httpStatus(HttpStatus.resolve(statusCode.value())).status(statusCode.value()).build(),
				statusCode);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest req) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		String allErrors = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
		return new ResponseEntity<Object>(
				MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message(allErrors)
				.httpStatus(HttpStatus.resolve(statusCode.value()))
				.status(statusCode.value())
				.build()
				,statusCode);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(ApiException.class)
	public ResponseEntity<MyResponse> handleApiException(ApiException ex) {
		return new ResponseEntity<MyResponse>(
				MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.reason(ex.getMessage())
				.message(ex.getMessage())
				.httpStatus(HttpStatus.BAD_REQUEST)
				.status(HttpStatus.BAD_REQUEST.value())
				.build()
				,HttpStatus.BAD_REQUEST);
	}

}
