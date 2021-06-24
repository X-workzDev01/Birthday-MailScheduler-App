package com.xworkz.birthdayMailSchedular.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.xworkz.birthdayMailSchedular.dto.AddSubscriberDTO;
import com.xworkz.birthdayMailSchedular.dto.GetSubscriberDTO;
import com.xworkz.birthdayMailSchedular.service.DBSubscriberService;
import com.xworkz.birthdayMailSchedular.service.ExcelToDB;
import com.xworkz.birthdayMailSchedular.service.SpringMailService;

@Controller
@RequestMapping("/")
public class DBController {

	private Logger logger = LoggerFactory.getLogger(MailScheduleController.class);

	@Autowired 
	private DBSubscriberService dBSubscriberService;
	
	@Autowired
	private SpringMailService springMailService;

	@Autowired
	private ExcelToDB excelToDB;
	
//	@Scheduled(cron = "${bday.cron.expression}" , zone = "IST")
	@RequestMapping(value = "/birthdayMailSchedule.do", method = RequestMethod.GET)
	public ModelAndView sendScheduleMail() {
		ModelAndView modelAndView = new ModelAndView("index");
		logger.info("invoked sendScheduleMail() in controller");
		try {
			List<GetSubscriberDTO> todaysBirthdayList = springMailService.sendBirthdayMails();
            logger.info("Birthady Mails Task Exccution Done");
            if(todaysBirthdayList.size() > 0) {
            	modelAndView.addObject("mailReport", "Birthday mails sent ");
            }else {
            	modelAndView.addObject("mailReport", "No birthday found for today's date,Report Mail sent");
            }
            modelAndView.addObject("birthdayList", todaysBirthdayList);
			return modelAndView;
		} catch (/*URISyntaxException | IOException*/ Exception e) {
			logger.error("you have an exception in {}"+e.getMessage(), e);
		}
		return modelAndView;
	}
	
	@Scheduled(cron = "${status.cron.expression}" , zone = "IST")
	public void updateStatusAsFalse() {
		logger.info("invoked updateStatusAsFalse() in MailController");
		int number=dBSubscriberService.updateStatus();
		logger.info("Total record updated "+number);
	}
	
	@RequestMapping(value = "/todayBirthdaysFromDb.do", method = RequestMethod.GET)
	public ModelAndView getTodaysBirthdayList() {
		ModelAndView modelAndView = new ModelAndView("index");
		logger.info("invoked getTodaysBirthdayList() in MailController");
		try {
			List list = dBSubscriberService.getTodaysBirthdayList();
			logger.info("sucessfully get list of todays birthday list");
			modelAndView.addObject("birthdayList", list);
			return modelAndView;
		} catch (Exception e) {
			logger.error("you have an exception in {}" + e.getMessage(), e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/weekBirthdaysFromDb.do", method = RequestMethod.GET)
	public ModelAndView getWeeklyBirthdayList() {
		ModelAndView modelAndView = new ModelAndView("index");
		logger.info("invoked getWeeklyBirthdayList() in MailController");
		try {
			List list = dBSubscriberService.getCurrentWeekBirthdayList();
			logger.info("sucessfully get list of todays birthday list");
			modelAndView.addObject("birthdayList", list);
			return modelAndView;
		} catch (Exception e) {
			logger.error("you have an exception in {}" + e.getMessage(), e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/monthBirthdaysFromDb.do", method = RequestMethod.GET)
	public ModelAndView getMonthBirthdayList() {
		ModelAndView modelAndView = new ModelAndView("index");
		logger.info("invoked getMonthBirthdayList() in MailController");
		try {
			List list = dBSubscriberService.getCurrentMonthBirthdayList();
			logger.info("sucessfully get list of todays birthday list");
			modelAndView.addObject("birthdayList", list);
			return modelAndView;
		} catch (Exception e) {
			logger.error("you have an exception in {}" + e.getMessage(), e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/excelToDB.do", method = RequestMethod.GET)
	public ModelAndView updateInDB() {
		ModelAndView modelAndView = new ModelAndView("index");
		logger.info("invoked updateInDB() in MailController");
		try {
			List list = excelToDB.writeUniqueDataInDB();
			logger.info("updated the records into data base", list);
			modelAndView.addObject("birthdayList", list);
			return modelAndView;
		} catch (Exception e) {
			logger.error("you have an exception in {}" + e.getMessage(), e);
		}
		return modelAndView;

	}

	@RequestMapping(value = "/addSubscriberToDB.do")
	public ModelAndView addSubscriber(AddSubscriberDTO dto) {
		ModelAndView modelAndView = new ModelAndView("addSubscriber");
		logger.info("invoking {}");
		try {
			String msg = dBSubscriberService.validateAndAdd(dto);
			logger.info( msg);
			modelAndView.addObject("msg", msg);
			return modelAndView;
		} catch (Exception e) {
			logger.error("you have an exception in {}" + e.getMessage(), e);
		}
		return modelAndView;
	}

	
}
