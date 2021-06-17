package com.xworkz.birthdayMailSchedular.service;

import java.util.List;

import org.springframework.mail.javamail.MimeMessagePreparator;

import com.xworkz.birthdayMailSchedular.dto.GetSubscriberDTO;

public interface SpringMailService {

	public boolean validateAndSendMailByMailId(MimeMessagePreparator messagePreparator);

	public List<GetSubscriberDTO> sendBirthdayMails();
}
