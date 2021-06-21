package com.xworkz.birthdayMailSchedular.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xworkz.birthdayMailSchedular.dao.BirthdayMasterDAO;
import com.xworkz.birthdayMailSchedular.dto.AddSubscriberDTO;
import com.xworkz.birthdayMailSchedular.dto.GetSubscriberDTO;
import com.xworkz.birthdayMailSchedular.entity.DetailsEntity;
import com.xworkz.birthdayMailSchedular.util.HelperUtil;
import com.xworkz.birthdayMailSchedular.util.MailSchedularConstants;

@Service
public class DBSubscriberServiceImpl implements DBSubscriberService {

	@Autowired
	private BirthdayMasterDAO dao;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String validateAndAdd(AddSubscriberDTO dto) throws ParseException {

		DetailsEntity entity = new DetailsEntity();
		if (Objects.nonNull(dto)) {
			logger.info("cheking for Subscriber already exist or not");
			DetailsEntity detailsEntity = dao.getByEmail(dto.getEmailId());
			if (!Objects.nonNull(detailsEntity)) {
				logger.info("Subscriber not exist");
				logger.info("copying properties from AddSubscriberDTO to DetailsEntity");
				entity.setFullName(dto.getFullName());
				entity.setEmailId(dto.getEmailId());
				logger.info("formatting Date of Birth");
				entity.setDob(HelperUtil.formateDateOfBirth(dto.getDob()));
				logger.info("DetailsEntity", entity);
				int affectedRows = dao.save(entity);
				if (affectedRows > 0) {
					logger.info("Subscriber Added with name " + entity.getFullName());
					return "Subscriber Added with name " + entity.getFullName();
				}
			} else {
				logger.info("Subscriber already exist");
				return "Subscriber already exist";
			}
		} else {
			logger.info("AddSubscriberDTO is null");
			return "Data not saved";
		}
		return null;
	}

	
	
	public Integer formateDate(Date date) throws ParseException {
		logger.info("formating date");
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");

		String d = dateFormat.format(date);
		logger.info("date formated and converting into Integer");
		Integer dob = Integer.parseInt(d);
		logger.info("date converted into Integer " + dob);
		return dob;
	}

	@Override
	public List<GetSubscriberDTO> getTodaysBirthdayList() {
		List<GetSubscriberDTO> todayBirthdayList = new ArrayList<>();
		GetSubscriberDTO dto = null;
		try {
			List<DetailsEntity> detailsEntityList = dao.getByDob(HelperUtil.getTodaysDate());
			if (Objects.nonNull(detailsEntityList)) {
				for (DetailsEntity detailsEntity : detailsEntityList) {
					dto = new GetSubscriberDTO();
					dto.setFullName(detailsEntity.getFullName());
					dto.setDob(detailsEntity.getDob());
					dto.setEmail(detailsEntity.getEmailId());
					dto.setStatus(detailsEntity.isStatus());
					todayBirthdayList.add(dto);
				}
			} else {
				logger.info("Subscriber List from getListOfSubscribersFromExcel Is null");
			}
		} catch (Exception e) {
			logger.error("Error msg is {} ", e.getMessage(), e);
		}
		return todayBirthdayList;
	}

	@Override
	public List<GetSubscriberDTO> getCurrentMonthBirthdayList() {
		List<GetSubscriberDTO> currentMonthBirthdayList = new ArrayList<>();
		GetSubscriberDTO dto = null;
		try {
			Date today = new Date();
			SimpleDateFormat format = new SimpleDateFormat(MailSchedularConstants.SimpleMonthWriteFormat_value);
			String date = format.format(today);
			List<DetailsEntity> detailsEntityList = dao.CurrentMonthBirthdayList(date);
			if (Objects.nonNull(detailsEntityList)) {
				for (DetailsEntity detailsEntity : detailsEntityList) {
					dto = new GetSubscriberDTO();
					dto.setFullName(detailsEntity.getFullName());
					dto.setDob(detailsEntity.getDob());
					dto.setEmail(detailsEntity.getEmailId());
					dto.setStatus(detailsEntity.isStatus());
					currentMonthBirthdayList.add(dto);
				}
			} else {
				logger.info("Subscriber List from getListOfSubscribersFromExcel Is null");
			}
		} catch (Exception e) {
			logger.error("Error msg is {} ", e.getMessage(), e);
		}
		return currentMonthBirthdayList;
	}

	@Override
	public List<GetSubscriberDTO> getCurrentWeekBirthdayList() {
		List<GetSubscriberDTO> currentMonthBirthdayList = new ArrayList<>();
		GetSubscriberDTO dto = null;
		try {
			String weekDates[] = HelperUtil.getCurrentWeekDate();
			if (Objects.nonNull(weekDates)) {
				List<DetailsEntity> detailsEntityList = dao.CurrentWeekBirthdayList(weekDates);
				if (Objects.nonNull(detailsEntityList)) {
					for (DetailsEntity detailsEntity : detailsEntityList) {
						dto = new GetSubscriberDTO();
						dto.setFullName(detailsEntity.getFullName());
						dto.setDob(detailsEntity.getDob());
						dto.setEmail(detailsEntity.getEmailId());
						dto.setStatus(detailsEntity.isStatus());
						currentMonthBirthdayList.add(dto);
					}
				} else {
					logger.info("Subscriber List from getListOfSubscribersFromExcel Is null");
				}
			} else {
				logger.info("weekDates array is null");
			}

		} catch (Exception e) {
			logger.error("Error msg is {} ", e.getMessage(), e);
		}
		return currentMonthBirthdayList;
	}

	@Override
	public int updateStatus() {
		logger.info("invoking updateStatus() in service");
		int numberOfRowAffected=dao.updateStatus();
		return numberOfRowAffected;
	}
}