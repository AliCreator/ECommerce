package com.advance.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.advance.event.NewUserEvent;
import com.advance.service.NewEventService;
import com.advance.utils.RequestUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NewEventListener {

	private final NewEventService eventService; 
	private final HttpServletRequest request; 
	
	
	@EventListener
	public void addEvent(NewUserEvent event) {
		eventService.addUserEvent(event.getEmail(), event.getType(), RequestUtils.getDevice(request), RequestUtils.getIpAddress(request));
	}
	
}
