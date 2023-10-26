package com.advance.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class Address {

	private String street;
	private String city;
	private String state;
	private String postalCode;
	private String country;
}