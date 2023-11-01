package com.advance.exception;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.advance.entity.MyResponse;
import com.auth0.jwt.exceptions.JWTDecodeException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler implements ErrorController {

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode statusCode, WebRequest request) {
		return new ResponseEntity<Object>(
				MyResponse.builder().timestamp(LocalDateTime.now().toString()).message(ex.getMessage())
						.httpStatus(HttpStatus.resolve(statusCode.value())).status(statusCode.value()).build(),
				statusCode);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode statusCode, WebRequest req) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		String allErrors = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
		return new ResponseEntity<Object>(
				MyResponse.builder().timestamp(LocalDateTime.now().toString()).message(allErrors)
						.httpStatus(HttpStatus.resolve(statusCode.value())).status(statusCode.value()).build(),
				statusCode);
	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<MyResponse> sQLIntegrityConstraintViolationException(
			SQLIntegrityConstraintViolationException exception) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(
				MyResponse.builder().timestamp(LocalDateTime.now().toString())
						.reason(exception.getMessage().contains("Duplicate entry") ? "Information already exists"
								: exception.getMessage())
						.developerMessage(exception.getMessage()).status(HttpStatus.BAD_REQUEST.value())
						.httpStatus(HttpStatus.BAD_REQUEST).build(),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<MyResponse> entityNotFoundException(EntityNotFoundException exception) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(
				MyResponse.builder().timestamp(LocalDateTime.now().toString())
						.reason(exception.getMessage().contains("expected 1, actual 0") ? "Record not found"
								: exception.getMessage())
						.developerMessage(exception.getMessage()).status(HttpStatus.BAD_REQUEST.value())
						.httpStatus(HttpStatus.BAD_REQUEST).build(),
				HttpStatus.BAD_REQUEST);
	}

	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<MyResponse> badCredentialsException(BadCredentialsException exception) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.reason(exception.getMessage() + ", Incorrect email or password")
				.developerMessage(exception.getMessage()).status(HttpStatus.BAD_REQUEST.value())
				.httpStatus(HttpStatus.BAD_REQUEST).build(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<MyResponse> apiException(ApiException exception) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.reason(exception.getMessage()).developerMessage(exception.getMessage())
				.status(HttpStatus.BAD_REQUEST.value()).httpStatus(HttpStatus.BAD_REQUEST).build(),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<MyResponse> accessDeniedException(AccessDeniedException exception) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(
				MyResponse.builder().timestamp(LocalDateTime.now().toString())
						.reason("Access denied. You don\'t have access").developerMessage(exception.getMessage())
						.status(HttpStatus.FORBIDDEN.value()).httpStatus(HttpStatus.FORBIDDEN).build(),
				HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<MyResponse> exception(Exception exception) {
		log.error(exception.getMessage());
		System.out.println(exception);
		return new ResponseEntity<>(
				MyResponse.builder().timestamp(LocalDateTime.now().toString())
						.reason(exception.getMessage() != null
								? (exception.getMessage().contains("expected 1, actual 0") ? "Record not found"
										: exception.getMessage())
								: "Some error occurred")
						.developerMessage(exception.getMessage()).status(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).build(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(JWTDecodeException.class)
	public ResponseEntity<MyResponse> exception(JWTDecodeException exception) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.reason("Could not decode the token").developerMessage(exception.getMessage())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).build(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<MyResponse> emptyResultDataAccessException(EmptyResultDataAccessException exception) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(
				MyResponse.builder().timestamp(LocalDateTime.now().toString())
						.reason(exception.getMessage().contains("expected 1, actual 0") ? "Record not found"
								: exception.getMessage())
						.developerMessage(exception.getMessage()).status(HttpStatus.BAD_REQUEST.value())
						.httpStatus(HttpStatus.BAD_REQUEST).build(),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<MyResponse> disabledException(DisabledException exception) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(
				MyResponse.builder().timestamp(LocalDateTime.now().toString()).developerMessage(exception.getMessage())
						// .reason(exception.getMessage() + ". Please check your email and verify your
						// account.")
						.reason("User account is currently disabled").status(HttpStatus.BAD_REQUEST.value())
						.httpStatus(HttpStatus.BAD_REQUEST).build(),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(LockedException.class)
	public ResponseEntity<MyResponse> lockedException(LockedException exception) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(
				MyResponse.builder().timestamp(LocalDateTime.now().toString()).developerMessage(exception.getMessage())
						// .reason(exception.getMessage() + ", too many failed attempts.")
						.reason("User account is currently locked").status(HttpStatus.BAD_REQUEST.value())
						.httpStatus(HttpStatus.BAD_REQUEST).build(),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<MyResponse> dataAccessException(DataAccessException exception) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(
				MyResponse.builder().timestamp(LocalDateTime.now().toString())
						.reason(processErrorMessage(exception.getMessage()))
						.developerMessage(processErrorMessage(exception.getMessage()))
						.status(HttpStatus.BAD_REQUEST.value()).httpStatus(HttpStatus.BAD_REQUEST).build(),
				HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<MyResponse> createErrorHttpResponse(HttpStatus httpStatus, String reason,
			Exception exception) {
		return new ResponseEntity<>(
				MyResponse.builder().timestamp(LocalDateTime.now().toString()).developerMessage(exception.getMessage())
						.reason(reason).status(httpStatus.value()).httpStatus(httpStatus).build(),
				httpStatus);
	}

	private String processErrorMessage(String errorMessage) {
		if (errorMessage != null) {
			if (errorMessage.contains("Duplicate entry") && errorMessage.contains("AccountVerifications")) {
				return "You already verified your account.";
			}
			if (errorMessage.contains("Duplicate entry") && errorMessage.contains("ResetPasswordVerifications")) {
				return "We already sent you an email to reset your password.";
			}
			if (errorMessage.contains("Duplicate entry")) {
				return "Duplicate entry. Please try again.";
			}
		}
		return "Some error occurred";
	}

}
