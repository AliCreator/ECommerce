package com.advance.entity;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(value = Include.NON_DEFAULT)
public class MyResponse {

	protected String timestamp; 
	protected String reason; 
	protected String message; 
	protected String developerMessage; 
	protected Integer status; 
	protected HttpStatus httpStatus; 
	protected Map<String, Object> data; 
}
