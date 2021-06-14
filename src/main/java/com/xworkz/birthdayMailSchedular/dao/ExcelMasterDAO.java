package com.xworkz.birthdayMailSchedular.dao;

import java.io.IOException;
import java.util.List;

import com.xworkz.birthdayMailSchedular.dto.Subscriber;

public interface ExcelMasterDAO {
	public List<Subscriber> getListOfSubscribersFromExcel() throws IOException;
}
