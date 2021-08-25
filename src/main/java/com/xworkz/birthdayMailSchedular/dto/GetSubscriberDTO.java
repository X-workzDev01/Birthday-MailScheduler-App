package com.xworkz.birthdayMailSchedular.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class GetSubscriberDTO {

	private String fullName;
	private String dob;
	private String email;
	private String status;
}
