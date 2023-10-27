package com.advance.resources;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.advance.dto.UserDTO;
import com.advance.entity.MyResponse;
import com.advance.entity.User;
import com.advance.entity.UserPrincipal;
import com.advance.enumeration.RoleType;
import com.advance.form.ForgotPasswordForm;
import com.advance.form.LoginForm;
import com.advance.form.ResetForgotPasswordForm;
import com.advance.form.UpdateForm;
import com.advance.form.UpdatePasswordForm;
import com.advance.provider.TokenProvider;
import com.advance.service.UserService;

import groovy.util.logging.Slf4j;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class UserResources {

	private final UserService userService;
	private final AuthenticationManager authManager;
	private final TokenProvider provider;

	@PostMapping("/register")
	public ResponseEntity<MyResponse> register(@RequestBody User user) {
		userService.register(user);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("User has been registered!").httpStatus(HttpStatus.CREATED).status(HttpStatus.CREATED.value())
				.build();
		return ResponseEntity.created(getURI()).body(myResponse);
	}

	@PostMapping("/login")
	public ResponseEntity<MyResponse> login(@RequestBody LoginForm form, HttpServletResponse response) {
		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(form.getEmail(), form.getPassword()));
		String token = provider.generateToken((UserPrincipal) authentication.getPrincipal());
		Cookie cookie = createCookie(token);
		response.addCookie(cookie);
		UserDTO dto = ((UserPrincipal) authentication.getPrincipal()).getUser();
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Logged in successfully!").data(Map.of("user", dto)).build();

		return ResponseEntity.ok().body(myResponse);
	}

	@PostMapping("/logout")
	public ResponseEntity<MyResponse> logout(HttpServletResponse response) {
		Cookie jwt = new Cookie("jwt", null);
		jwt.setMaxAge(0);
		jwt.setHttpOnly(true);
		response.addCookie(jwt);

		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("User has successfully logged out!").httpStatus(HttpStatus.OK).status(HttpStatus.OK.value())
				.build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/find/me")
	public ResponseEntity<MyResponse> findMyProfile(@AuthenticationPrincipal User user) {
		UserDTO dto = userService.getUserByEmail(user.getEmail());
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("User retrieved!").httpStatus(HttpStatus.OK).status(HttpStatus.OK.value())
				.data(Map.of("user", dto)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/forgot/password")
	public ResponseEntity<MyResponse> forgotPassword(@RequestBody ForgotPasswordForm form) {
		userService.sendResetPasswordUrl(form.getEmail());
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Forgot password email sent!").httpStatus(HttpStatus.OK).status(HttpStatus.OK.value()).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PostMapping("/update/password/{key}")
	public ResponseEntity<MyResponse> updatePasswordWithKey(@PathVariable("key") String key,
			@Valid @RequestBody ResetForgotPasswordForm form) {
		userService.renewPassword(key, form.getPassword(), form.getConfirmPassword());
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Password updated. Please login!").httpStatus(HttpStatus.OK).status(HttpStatus.OK.value())
				.build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/verify/account/{key}")
	public ResponseEntity<MyResponse> verifyAccountAfterRegister(@PathVariable("key") String key) {
		userService.verifyAccount(key);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Account verified. Please login!").httpStatus(HttpStatus.OK).status(HttpStatus.OK.value())
				.build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/update/password")
	public ResponseEntity<MyResponse> updatePassword(@AuthenticationPrincipal User user,
			@Valid @RequestBody UpdatePasswordForm form) {
		userService.updateUserPassword(user.getId(), form.getCurrentPassword(), form.getNewPassword(),
				form.getConfirmPassword());
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Password has been updated!").httpStatus(HttpStatus.OK).status(HttpStatus.OK.value()).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/update/info")
	public ResponseEntity<MyResponse> upadteUserInfo(@AuthenticationPrincipal User user, @Valid UpdateForm form) {
		UserDTO dto = userService.updateUserDetails(form);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("User details have been updated!").httpStatus(HttpStatus.OK).status(HttpStatus.OK.value())
				.data(Map.of("user", dto)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/update/role/{roleName}")
	public ResponseEntity<MyResponse> updateUserRole(@AuthenticationPrincipal User user,
			@PathVariable("roleName") String roleName) {
		UserDTO dto = userService.updateUserRole(user.getId(), roleName);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("User role has been updated!").httpStatus(HttpStatus.OK).status(HttpStatus.OK.value())
				.data(Map.of("user", dto)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/get/all")
	public ResponseEntity<MyResponse> getAllUsers() {
		List<UserDTO> allUsers = userService.getAllUsers();
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("All users have been retrieved!").httpStatus(HttpStatus.OK).status(HttpStatus.OK.value())
				.data(Map.of("user", allUsers)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/get/all/paginated")
	public ResponseEntity<MyResponse> getAllUsersWithPagination(
			@RequestParam("pageNumber") Optional<Integer> pageNumber,
			@RequestParam("pageSize") Optional<Integer> pageSize) {
		Page<UserDTO> page = userService.getAllUsersWithPagination(pageNumber.orElse(0), pageSize.orElse(10));
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("Users paginated and retrieved!").httpStatus(HttpStatus.OK).status(HttpStatus.OK.value())
				.data(Map.of("user", page)).build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/update/mfa")
	public ResponseEntity<MyResponse> toggleMfa(@AuthenticationPrincipal User user,
			@RequestParam("status") Boolean status) {
		UserDTO dto = userService.toggleMfa(user.getId(), status);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.message("MFA status updated!").httpStatus(HttpStatus.OK).status(HttpStatus.OK.value())
				.data(Map.of("user", dto)).build();
		return ResponseEntity.ok().body(myResponse);
	}

//	@RequestMapping("/error")
//	public ResponseEntity<MyResponse> handleError(HttpServletRequest req) {
//		return ResponseEntity.badRequest()
//				.body(MyResponse.builder().timestamp(LocalDateTime.now().toString()).httpStatus(HttpStatus.BAD_REQUEST)
//						.status(HttpStatus.BAD_REQUEST.value())
//						.message("There is no endpoint for " + req.getMethod() + " on this server!").build());
//	}

	private Cookie createCookie(String token) {
		Cookie cookie = new Cookie("jwt", token);
		cookie.setMaxAge(86400 * 3);
		cookie.setHttpOnly(true);
		cookie.setDomain("/");
		return cookie;
	}

	private URI getURI() {
		return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/get/<userId>").toUriString());
	}
}
