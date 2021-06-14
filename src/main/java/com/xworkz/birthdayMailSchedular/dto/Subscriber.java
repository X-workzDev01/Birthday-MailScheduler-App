package com.xworkz.birthdayMailSchedular.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Subscriber implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fullName;
	private String emailId;
	private double dob;

	public Subscriber() {

	}
	
	public Subscriber(String fullName, String email, double date) {
		this.dob = date;
		this.fullName = fullName;
		this.emailId = email;
	}

}
