package com.advance.service.implementation;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.advance.entity.User;
import com.advance.entity.UserEvent;
import com.advance.enumeration.EventType;
import com.advance.exception.ApiException;
import com.advance.repository.UserEventRepository;
import com.advance.repository.UserRepository;
import com.advance.service.NewEventService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventServiceImpl implements NewEventService {

	private final UserEventRepository userEventRepo;
	private final UserRepository userRepo;

	@Override
	public Collection<UserEvent> getEventsByUserId(Long userId) {
		try {
			return userEventRepo.findByUser(userId);
		} catch (Exception e) {
			throw new ApiException("User was not found!");
		}
	}

	@Override
	public void addUserEvent(String email, EventType type, String device, String ipAddress) {
		try {
			Optional<User> findByEmail = userRepo.findByEmail(email);
			if (findByEmail.isPresent()) {
				User user = findByEmail.get();
				UserEvent userEvent = new UserEvent();
				userEvent.setUser(user);
				userEvent.setCreatedAt(LocalDateTime.now());
				userEvent.setDevice(device);
				userEvent.setIpAddress(ipAddress);
				userEvent.setEvent(type);
				userEvent.setDescription(type.getDescription());
				userEventRepo.save(userEvent);
			} else {
				throw new ApiException("User was not found!");
			}
		} catch (Exception e) {
			throw new ApiException("Something went wrong. Please try again!");
		}

	}

	@Override
	public void addUserEvent(Long userId, EventType type, String device, String ipAddress) {
		try {
			Optional<User> findByEmail = userRepo.findById(userId);
			if (findByEmail.isPresent()) {
				User user = findByEmail.get();
				UserEvent userEvent = new UserEvent();
				userEvent.setUser(user);
				userEvent.setCreatedAt(LocalDateTime.now());
				userEvent.setDevice(device);
				userEvent.setIpAddress(ipAddress);
				userEvent.setEvent(type);
				userEvent.setDescription(type.getDescription());
				userEventRepo.save(userEvent);
			} else {
				throw new ApiException("User was not found!");
			}
		} catch (Exception e) {
			throw new ApiException("Something went wrong. Please try again!");
		}

	}

}
