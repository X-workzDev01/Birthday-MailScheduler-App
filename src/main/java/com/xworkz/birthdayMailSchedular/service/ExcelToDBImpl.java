package com.xworkz.birthdayMailSchedular.service;

import java.io.IOException;
import java.util.ArrayList;
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

@Service
public class ExcelToDBImpl implements ExcelToDB{
	
	private Logger logger=LoggerFactory.getLogger(getClass());
	
	@Autowired
	private BirthdayMasterDAO dao ;
	@Autowired
	private ExcelMasterDAO excelMasterDAO;
	
//	public ExcelToDBImpl() {
//		logger = LoggerFactory.getLogger(getClass());
//		logger.info("calling counstructor ========== ");
//	}
	
	@Override
	public List<GetSubscriberDTO> writeUniqueDataInDB() throws IOException {
		logger.info("invoking writeUniqueDataInDB()");
		logger.info("Getting list of subscriber from excel file");
		List<Subscriber>  list = excelMasterDAO.getListOfSubscribersFromExcel();
		List<GetSubscriberDTO> updatedList = new ArrayList<GetSubscriberDTO>();
		GetSubscriberDTO dto = new GetSubscriberDTO();
	//	List<DetailsEntity> upload = new ArrayList<DetailsEntity>();
		logger.info("checking for list of subscriber is empty or not");
		if (Objects.nonNull(list)) {
			logger.info("list of subscriber is not null");
			for (Subscriber subscriber : list) {
				logger.info("checking for subsriber"+ subscriber.getFullName()+" is present in DB or not");
				DetailsEntity userEntity =dao.getByEmail(subscriber.getEmailId());
				if (userEntity == null ) {
					logger.info("subscriber "+ subscriber.getFullName()+" not present");
					DetailsEntity entity = new DetailsEntity();
					logger.info("adding subsriber details to entity");
					entity.setFullName(subscriber.getFullName());
					entity.setEmailId(subscriber.getEmailId());
					entity.setDob((int)subscriber.getDob());
					
					int affectedRows =dao.save(entity);
					if (affectedRows >0) {
						logger.info("Subscriber data added in list with name"+ subscriber.getFullName() +" and EmailID " + subscriber.getEmailId());
						dto.setFullName(subscriber.getFullName());
						updatedList.add(dto);
					}
					
//					if (!upload.contains(entity)) {
//						logger.info("Subscriber "+entity.getFullName()+" adding into list");
//						upload.add(entity);
//						logger.info("Subscriber data added in list with name"+ subscriber.getFullName() +" and EmailID " + subscriber.getEmailId());
//						dto.setFullName(subscriber.getFullName());
//						updatedList.add(dto);
//					}
				}
			}
			//int affectedRows=dao.saveAll(upload);
			//logger.info(affectedRows + "subscriber added in db " , upload);
			
		}else {
			logger.info("Subscriber list sheet is empty");
		}
		return updatedList;
	}

	
//	public static void main(String[] args) throws IOException {
//		ExcelToDBImpl dbImpl = new ExcelToDBImpl();
//		dbImpl.writeUniqueDataInDB();
//	}
}
