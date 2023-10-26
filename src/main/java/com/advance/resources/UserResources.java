package com.advance.resources;

import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.advance.entity.MyResponse;
import com.advance.entity.User;
import com.advance.service.UserService;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class UserResources {

	private final UserService userService;

	@PostMapping("/register")
	public ResponseEntity<MyResponse> register(@RequestBody User user) {
		userService.register(user);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("User has been registered!").httpStatus(HttpStatus.CREATED).status(HttpStatus.CREATED.value())
				.build();
		return ResponseEntity.created(getURI()).body(myResponse);
	}

	private URI getURI() {
		return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/get/<userId>").toUriString());
	}
}
