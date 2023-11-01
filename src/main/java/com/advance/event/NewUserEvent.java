package com.advance.event;

import org.springframework.context.ApplicationEvent;

import com.advance.enumeration.EventType;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class NewUserEvent extends ApplicationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EventType type; 
	private String email; 
	
	public NewUserEvent(String email, EventType type) {
		super(email);
		this.email = email; 
		this.type = type;
	}
}
