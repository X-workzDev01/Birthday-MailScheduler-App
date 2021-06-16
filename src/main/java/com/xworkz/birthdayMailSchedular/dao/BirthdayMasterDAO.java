package com.xworkz.birthdayMailSchedular.dao;

import java.util.List;

import com.xworkz.birthdayMailSchedular.entity.DetailsEntity;

public interface BirthdayMasterDAO {

	public int save(DetailsEntity entity);
	
	public DetailsEntity getByEmail(String emailId);

	public List<DetailsEntity> getAll();

	int saveAll(List<DetailsEntity> entity);

	public List<DetailsEntity> getByDob(String date);

	public List<DetailsEntity> CurrentMonthBirthdayList(String date);

	public List<DetailsEntity> CurrentWeekBirthdayList(String[] weekDates);
}
