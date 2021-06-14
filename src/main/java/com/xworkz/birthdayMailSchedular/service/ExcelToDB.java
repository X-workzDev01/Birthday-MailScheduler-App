package com.xworkz.birthdayMailSchedular.service;

import java.io.IOException;
import java.util.List;

import com.xworkz.birthdayMailSchedular.dto.GetSubscriberDTO;

public interface ExcelToDB {
	
	public List<GetSubscriberDTO> writeUniqueDataInDB() throws IOException;

}
