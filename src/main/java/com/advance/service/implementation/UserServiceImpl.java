package com.advance.service.implementation;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.advance.dto.UserDTO;
import com.advance.entity.User;
import com.advance.entity.Verification;
import com.advance.entity.VerificationCode;
import com.advance.enumeration.RoleType;
import com.advance.enumeration.VerificationType;
import com.advance.exception.ApiException;
import com.advance.form.UpdateForm;
import com.advance.repository.UserRepository;
import com.advance.repository.VerificationCodeRepository;
import com.advance.repository.VerificationRepository;
import com.advance.service.EmailService;
import com.advance.service.UserService;
import com.advance.utils.SmsUtil;
import com.advance.utils.UserDtoMapper;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepo;
	private final BCryptPasswordEncoder encoder;
	private final VerificationRepository verificationRepo;
	private final EmailService emailService;
	private final VerificationCodeRepository verificationCodeRepo;

	@Override
	public UserDTO getUserById(Long userId) {
		try {
			Optional<User> user = userRepo.findById(userId);
			if (user.isPresent()) {
				return UserDtoMapper.convertToUserDTO(user.get());
			}
		} catch (Exception e) {
			throw new ApiException("User was not found!");
		}
		return null;
	}

	@Override
	public UserDTO getUserByEmail(String email) {
		try {
			Optional<User> user = userRepo.findByEmail(email);
			if (user.isPresent()) {
				return UserDtoMapper.convertToUserDTO(user.get());
			}
		} catch (Exception e) {
			throw new ApiException("User was not found!");
		}
		return null;
	}

	@Override
	public UserDTO register(User user) {

		if (getUserByEmail(user.getEmail().trim().toLowerCase()) != null) {
			throw new ApiException("User is already registered. Please login!");
		}
		try {
			user.setCreatedAt(LocalDate.now());
			user.setEmail(user.getEmail().trim().toLowerCase());
			user.setIsEnabled(false);
			user.setRole(RoleType.ROLE_USER);
			user.setPassword(encoder.encode(user.getPassword()));

			User newUser = userRepo.save(user);
			// Create verification code and send it through email
			String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(),
					VerificationType.ACCOUNT.name().toLowerCase());
			Verification verification = Verification.builder().createdAt(LocalDateTime.now()).url(verificationUrl)
					.expiredAt(LocalDateTime.now().plusDays(3)).user(newUser).build();
			verificationRepo.save(verification);
//			emailService.send(newUser.getEmail(), newUser.getFirstName(), null, verificationUrl);

			return UserDtoMapper.convertToUserDTO(newUser);

		} catch (Exception e) {
			throw new ApiException("Something went wrong during saving the user!");
		}
	}

	@Override
	public List<UserDTO> getAllUsers() {
		try {
			Iterable<User> all = userRepo.findAll();
			ArrayList<UserDTO> allUsers = new ArrayList<>();
			for (User u : all) {
				UserDTO dto = new UserDTO();
				BeanUtils.copyProperties(u, dto);
				allUsers.add(dto);
			}
			return allUsers;
		} catch (Exception e) {
			throw new ApiException("Something went wrong during retrieving all users!");
		}
	}

	@Override
	public Page<UserDTO> getAllUsersWithPagination(int pageNumber, int pageSize) {
		try {
			Page<User> allUsers = userRepo.findAll(PageRequest.of(pageNumber, pageSize));
			return allUsers.map(u -> UserDtoMapper.convertToUserDTO(u));
		} catch (Exception e) {
			throw new ApiException("Something went wrong during retrieving all users!");
		}
	}

	@Override
	public void sendAccountVerificationCode(String email) {
		// This method is used when you want to send a code to the client's phone number

		try {
			User user = userRepo.findByEmail(email).get();
			if (user == null) {
				throw new ApiException("The user was not found!");
			}

			verificationCodeRepo.deleteVerificationCodeByUserId(user.getId());

			// Generate new code
			String code = randomAlphabetic(8).toUpperCase();
			LocalDateTime createdAt = LocalDateTime.now();
			LocalDateTime expiredAt = createdAt.plusDays(1);

			VerificationCode verificationCode = VerificationCode.builder().createdAt(createdAt).expiredAt(expiredAt)
					.code(code).user(user).build();
			verificationCodeRepo.save(verificationCode);
			String messageBody = "Your verification code is: \n" + code;
			SmsUtil.sendSms(user.getPhone(), messageBody);
		} catch (Exception e) {
			throw new ApiException("There was a problem while sending the verification code to phone!");
		}

	}

	@Override
	public UserDTO verifyPasswordKey(String key) {
		if (isPasswordKeyExpired(key, VerificationType.PASSWORD))
			throw new ApiException("The key is expired. Please try to reset your password again!");
		try {
			return UserDtoMapper.convertToUserDTO(
					userRepo.getUserByPasswordKey(getVerificationUrl(key, VerificationType.PASSWORD.name())));
		} catch (Exception e) {
			throw new ApiException("There was a problem while retrieving the user through password key!");
		}
	}

	private boolean isPasswordKeyExpired(String key, VerificationType password) {
		Verification verification = verificationRepo.getVerificationByUrl(getVerificationUrl(key, password.name()));
		return verification.getExpiredAt().isBefore(LocalDateTime.now());
	}

	@Override
	public void renewPassword(String key, String password, String confirmPassword) {
		if (!password.equals(confirmPassword))
			throw new ApiException("Password and confirmed password should match!");
		try {
			User user = userRepo.updateUserPasswordWithKey(encoder.encode(password),
					getVerificationUrl(key, VerificationType.PASSWORD.name()));
			Verification ver = verificationRepo
					.getVerificationByUrl(getVerificationUrl(key, VerificationType.PASSWORD.name()));
			verificationRepo.deleteById(ver.getId());
			userRepo.save(user);

		} catch (Exception e) {
			throw new ApiException("Something went wrong during updating password with key!");
		}

	}

	@Override
	public UserDTO verifyAccount(String key) {
		try {
			User user = userRepo.getUserByPasswordKey(getVerificationUrl(key, VerificationType.ACCOUNT.name()));
			user.setIsEnabled(true);
			userRepo.save(user);
			return UserDtoMapper.convertToUserDTO(user);
		} catch (Exception e) {
			throw new ApiException("Something went wrong during verifying account with key!");
		}
	}

	@Override
	public UserDTO updateUserDetails(@Valid UpdateForm user) {
		try {
			return UserDtoMapper.convertToUserDTO(
					userRepo.udpateUserFields(user.getId(), user.getFirstName(), user.getLastName(), user.getPhone()));
		} catch (Exception e) {
			throw new ApiException("Something went wrong during updating user details!");
		}
	}

	@Override
	public void updateUserPassword(Long id, String currentPassword, String newPassword, String confirmNewPassword) {
		if (!newPassword.equals(confirmNewPassword))
			throw new ApiException("Your new password and confirm password do not match. Please try again!");
		try {
			User user = userRepo.findById(id).get();
			if (!encoder.matches(currentPassword, user.getPassword()))
				throw new ApiException("Your current password is incorrect. Please try again!");
			userRepo.updateUserPassword(id, encoder.encode(newPassword));
		} catch (Exception e) {
			throw new ApiException("Something went wrong during renewing user password!");
		}

	}

	@Override
	public UserDTO updateUserRole(Long id, RoleType roleName) {

		try {

			return UserDtoMapper.convertToUserDTO(userRepo.updateUserRole(id, roleName));

		} catch (Exception e) {
			throw new ApiException("Something went wrong during updating user role!");
		}

	}

	@Override
	public UserDTO toggleMfa(Long userId, Boolean status) {
		try {
			return UserDtoMapper.convertToUserDTO(userRepo.toggleUsingMfa(userId, status));
		} catch (Exception e) {
			throw new ApiException("There was a problem udpating the MFA!");
		}
	}

	private String getVerificationUrl(String key, String type) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path("/auth/verify/account/" + key + "/" + type)
				.toUriString();
	}

	@Override
	public void sendResetPasswordUrl(String email) {
		UserDTO dto = getUserByEmail(email);
		if (dto == null)
			throw new ApiException("There is no user with this email!");
		try {
			String url = getVerificationUrl(UUID.randomUUID().toString(), VerificationType.PASSWORD.name());
			Verification verification = Verification.builder().createdAt(LocalDateTime.now())
					.expiredAt(LocalDateTime.now().plusDays(1)).url(url).user(UserDtoMapper.convertToUser(dto)).build();
			verificationRepo.save(verification);
			System.out.println(verification);
//			emailService.send(dto.getEmail(), dto.getFirstName(), "reset-password", url);

		} catch (Exception e) {
			throw new ApiException("There was a problem sending rset password url!");
		}

	}

}
