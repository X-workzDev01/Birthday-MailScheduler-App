package com.xworkz.birthdayMailSchedular.dao;

import java.util.List;

import com.xworkz.birthdayMailSchedular.entity.DetailsEntity;

public interface BirthdayMasterDAO {

	public int save(DetailsEntity entity);
	
	public DetailsEntity getByEmail(String emailId);

	public List<DetailsEntity> getAll();

	int saveAll(List<DetailsEntity> entity);

	public List<DetailsEntity> getByDob(Integer dob);

	public List<DetailsEntity> CurrentMonthBirthdayList(int thisMonth);

	public List<DetailsEntity> CurrentWeekBirthdayList(int[] weekDates);
}
