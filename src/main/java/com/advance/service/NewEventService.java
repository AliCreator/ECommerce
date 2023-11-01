package com.advance.service;

import java.util.Collection;

import com.advance.entity.UserEvent;
import com.advance.enumeration.EventType;

public interface NewEventService {

	Collection<UserEvent> getEventsByUserId(Long userId); 
	void addUserEvent(String email, EventType type, String device, String ipAddress);
	void addUserEvent(Long userId, EventType type, String device, String ipAddress);
}
