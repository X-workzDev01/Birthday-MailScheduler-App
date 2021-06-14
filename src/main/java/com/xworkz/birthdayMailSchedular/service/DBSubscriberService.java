package com.xworkz.birthdayMailSchedular.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.xworkz.birthdayMailSchedular.dto.AddSubscriberDTO;
import com.xworkz.birthdayMailSchedular.dto.GetSubscriberDTO;

public interface DBSubscriberService {
	
	public String validateAndAdd(AddSubscriberDTO dto) throws ParseException;

	public List<GetSubscriberDTO> getTodaysBirthdayList();

	public List<GetSubscriberDTO> getCurrentMonthBirthdayList();

	public List<GetSubscriberDTO> getCurrentWeekBirthdayList();
	
}
