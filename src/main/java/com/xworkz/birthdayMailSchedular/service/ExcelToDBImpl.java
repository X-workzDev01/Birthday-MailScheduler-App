package com.xworkz.birthdayMailSchedular.service;

import java.io.IOException;
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
import com.xworkz.birthdayMailSchedular.dao.ExcelMasterDAO;
import com.xworkz.birthdayMailSchedular.dto.GetSubscriberDTO;
import com.xworkz.birthdayMailSchedular.dto.Subscriber;
import com.xworkz.birthdayMailSchedular.entity.DetailsEntity;
import com.xworkz.birthdayMailSchedular.util.EncryptionHelper;

@Service
public class ExcelToDBImpl implements ExcelToDB {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BirthdayMasterDAO dao;
	@Autowired
	private ExcelMasterDAO excelMasterDAO;
	@Autowired
	private EncryptionHelper helper;

	@Override
	public List<GetSubscriberDTO> writeUniqueDataInDB() throws IOException, ParseException {
		logger.info("invoking writeUniqueDataInDB()");
		logger.info("Getting list of subscriber from excel file");
		List<Subscriber> list = excelMasterDAO.getListOfSubscribersFromExcel();
		List<GetSubscriberDTO> updatedList = new ArrayList<GetSubscriberDTO>();
		GetSubscriberDTO dto = null;
		// List<DetailsEntity> upload = new ArrayList<DetailsEntity>();
		logger.info("checking for list of subscriber is empty or not");
		if (Objects.nonNull(list)) {
			logger.info("list of subscriber is not null");
			for (Subscriber subscriber : list) {
				logger.info("checking for subsriber" + subscriber.getFullName() + " is present in DB or not");
				DetailsEntity userEntity = dao.getByEmail(helper.decryptEmailId(subscriber.getEmailId()));
				if (userEntity == null) {
					logger.info("subscriber " + subscriber.getFullName() + " not present");
					DetailsEntity entity = new DetailsEntity();
					logger.info("adding subsriber details to entity");
					entity.setFullName(subscriber.getFullName());
					entity.setEmailId(helper.decryptEmailId(subscriber.getEmailId()));

					int dateOfBirth = helper.decryptDateOfBirth((int) subscriber.getDob());
					entity.setDob(convertIntToDate(dateOfBirth));
					entity.setStatus(false);

					int affectedRows = dao.save(entity);
					if (affectedRows > 0) {
						dto = new GetSubscriberDTO();
						logger.info("Subscriber data added in list with name" + subscriber.getFullName()
								+ " and EmailID " + subscriber.getEmailId());
						dto.setFullName(entity.getFullName());
						dto.setEmail(entity.getEmailId());
						dto.setDob(entity.getDob());
						updatedList.add(dto);
					}
				}
			}
		} else {
			logger.info("Subscriber list sheet is empty");
		}
		return updatedList;
	}

	String convertIntToDate(int n) throws ParseException {
		String dob = String.valueOf(n);
		Date date = null;
		if (dob.length() == 7) {
			SimpleDateFormat format = new SimpleDateFormat("dMMyyyy");
			date = format.parse(dob);
		}else if (dob.length() == 8) {
			SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
			date = format.parse(dob);
		}
		SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
		String dateOfBirth = format1.format(date);
		return dateOfBirth;
	}
	
	

}