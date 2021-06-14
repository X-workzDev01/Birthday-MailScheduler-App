package com.xworkz.birthdayMailSchedular.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.xworkz.birthdayMailSchedular.dto.GetSubscriberDTO;
import com.xworkz.birthdayMailSchedular.dto.Subscriber;

public interface MailSchedularService {

	public String birthdayMailSender() throws URISyntaxException, IOException;

	public List<GetSubscriberDTO> getTodaysBirthdayList() throws IOException;

	public List<GetSubscriberDTO> getCurrentWeekBirthdayList();

	public List<GetSubscriberDTO> getCurrentMonthBirthdayList();
}
