package com.xworkz.birthdayMailSchedular.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AddSubscriberDTO {

	private String fullName;
	private String emailId;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dob;
}
