package com.xworkz.birthdayMailSchedular.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "birthday_master")
@NamedQueries({
	@NamedQuery(name = "getByEmailId" , query = "from DetailsEntity as bm where bm.emailId= :email"), 
	@NamedQuery(name = "getAll", query = "from DetailsEntity as bm"),
	@NamedQuery(name = "getByDob" , query = "from DetailsEntity as bm where bm.dob like CONCAT(:dob,'%')"),
	@NamedQuery(name = "currentMonthBirthdayList" , query = "from DetailsEntity as bm where bm.dob like CONCAT('_',:dob,'%') OR bm.dob like CONCAT('__',:dob,'%')"),
	@NamedQuery(name = "currentWeekBirthdayList" , query = "from DetailsEntity as bm where bm.dob like CONCAT(:weekDate1,'%') OR  bm.dob like CONCAT(:weekDate2,'%') OR bm.dob like CONCAT(:weekDate3,'%') OR bm.dob like CONCAT(:weekDate4,'%') OR bm.dob like CONCAT(:weekDate5,'%') OR bm.dob like CONCAT(:weekDate6,'%') OR bm.dob like CONCAT(:weekDate7,'%')"),
	})

public class DetailsEntity {
	
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "FULLNAME")
	private String fullName;
	@Column(name = "EMAIL_ID")
	private String emailId;
	@Column(name = "DOB")
	private Integer dob;

}
