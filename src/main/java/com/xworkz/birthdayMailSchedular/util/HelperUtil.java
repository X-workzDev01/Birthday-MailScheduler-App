package com.xworkz.birthdayMailSchedular.util;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xworkz.birthdayMailSchedular.service.MailSchedularServiceImpl;

@Component
public class HelperUtil {
	
	private static Logger logger = LoggerFactory.getLogger(MailSchedularServiceImpl.class);
	
	public static String[] getCurrentWeekDate() {
		logger.info("invoking getCurrentWeekDate() of class HelperUtil ");
		String date[] = new String[7];
		LocalDate now = LocalDate.now();
		DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
		LocalDate startOfCurrentWeek = now.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
		LocalDate printDate = startOfCurrentWeek;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MailSchedularConstants.SimpleTodayDateFormat_value);
		logger.debug("Adding week dates in String array");
		for (int i = 0; i < 7; i++) {
			date[i] = printDate.format(formatter); 
			logger.debug("first date of the week " + date[i]);
			printDate = printDate.plusDays(1);
		}
		return date;
	}
	
	public static String formateDateOfBirth(Date date) {
		logger.info("formating date");
		SimpleDateFormat dateFormat = new SimpleDateFormat(MailSchedularConstants.SimpleDateOfBirthFormat_value);
		
		String d = dateFormat.format(date);
		logger.info("date formated and converting into Integer");
		return d;
	}
}
