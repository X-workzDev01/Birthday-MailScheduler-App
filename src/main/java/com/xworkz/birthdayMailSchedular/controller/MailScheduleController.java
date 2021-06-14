package com.xworkz.birthdayMailSchedular.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.xworkz.birthdayMailSchedular.service.DBSubscriberService;
import com.xworkz.birthdayMailSchedular.service.ExcelToDB;
import com.xworkz.birthdayMailSchedular.service.MailSchedularService;

@RestController
@RequestMapping("/")
public class MailScheduleController {

	private Logger logger = LoggerFactory.getLogger(MailScheduleController.class);

	@Autowired
	private MailSchedularService mailSchedular;
	
//	@Scheduled(cron = "${bday.cron.expression}" , zone = "IST")
//	@RequestMapping(value = "/mailSchedule.do", method = RequestMethod.POST)
//	public ModelAndView sendScheduleMail() {
//		ModelAndView modelAndView = new ModelAndView("index");
//		logger.info("invoked sendScheduleMail() in controller");
//		try {
//			String report = mailSchedular.birthdayMailSender();
//            logger.info("Birthady Mails Task Exccution Done");
//            modelAndView.addObject("mailReport", report);
//			return modelAndView;
//		} catch (URISyntaxException | IOException e) {
//			logger.error("you have an exception in {}"+e.getMessage(), e);
//		}
//		return modelAndView;
//	}

	@RequestMapping(value = "/todayBirthdays.do", method = RequestMethod.GET)
	public ModelAndView getTodaysBirthdayList() {
		ModelAndView modelAndView = new ModelAndView("index");
		logger.info("invoked getTodaysBirthdayList() in MailController");
		try {
			List list = mailSchedular.getTodaysBirthdayList();
			logger.info("sucessfully get list of todays birthday list");
			modelAndView.addObject("birthdayList", list);
			return modelAndView;
		} catch (Exception e) {
			logger.error("you have an exception in {}" + e.getMessage(), e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/weekBirthdays.do", method = RequestMethod.GET)
	public ModelAndView getWeeklyBirthdayList() {
		ModelAndView modelAndView = new ModelAndView("index");
		logger.info("invoked getWeeklyBirthdayList() in MailController");
		try {
			List list = mailSchedular.getCurrentWeekBirthdayList();
			logger.info("sucessfully get list of todays birthday list");
			modelAndView.addObject("birthdayList", list);
			return modelAndView;
		} catch (Exception e) {
			logger.error("you have an exception in {}" + e.getMessage(), e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/monthBirthdays.do", method = RequestMethod.GET)
	public ModelAndView getMonthBirthdayList() {
		ModelAndView modelAndView = new ModelAndView("index");
		logger.info("invoked getMonthBirthdayList() in MailController");
		try {
			List list = mailSchedular.getCurrentMonthBirthdayList();
			logger.info("sucessfully get list of todays birthday list");
			modelAndView.addObject("birthdayList", list);
			return modelAndView;
		} catch (Exception e) {
			logger.error("you have an exception in {}" + e.getMessage(), e);
		}
		return modelAndView;
	}

}