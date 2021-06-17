package com.xworkz.birthdayMailSchedular.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xworkz.birthdayMailSchedular.entity.DetailsEntity;

@Component
public class BirthdayMasterDAOImpl implements BirthdayMasterDAO {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SessionFactory factoryBean;

	@Override
	public int save(DetailsEntity entity) {
		Transaction transaction = null;
		Session session = null;
		logger.info("invoking ");
		int n = 0;
		try {
			logger.info("inside try block");
			logger.info("opening session");
			session = factoryBean.openSession();
			logger.info("creating transaction object");
			transaction = session.beginTransaction();
			logger.info("inserting data into table");
			n = (int) session.save(entity);
			logger.info("invoking transaction.commit()");
			transaction.commit();

		} catch (Exception e) {
			logger.error("you have an exception " + e.getMessage(), e);

		} finally {
			logger.info("closing DB connection");
			if (session != null) {
				session.close();
				logger.info("session closed");
			}
		}
		return n;
	}

	@Override
	public int saveAll(List<DetailsEntity> entity) {
		Transaction transaction = null;
		Session session = null;
		int n = 0;
		try {
			logger.info("inside try block");
			logger.info("opening session");
			session = factoryBean.openSession();
			logger.info("creating transaction object");
			transaction = session.beginTransaction();
			logger.info("inserting list of data into table");
			n = (int) session.save(entity);
			logger.info("invoking transaction.commit()");
			transaction.commit();

		} catch (Exception e) {
			logger.error("you have an exception " + e.getMessage(), e);

		} finally {
			logger.info("closing DB connection");
			if (session != null) {
				session.close();
				logger.info("session closed");
			}
		}
		return n;
	}

	@Override
	public List<DetailsEntity> getAll() {
		Session session = null;
		List<DetailsEntity> detailsEntity = new ArrayList<DetailsEntity>();
		try {
			session = factoryBean.openSession();
			Query<DetailsEntity> query = session.createNamedQuery("getAll");
			detailsEntity = query.list();
		} catch (Exception e) {
			logger.error("you have an exception " + e.getMessage(), e);
		} finally {
			logger.info("closing DB connection");
			if (session != null) {
				session.close();
				logger.info("session closed");
			}
		}
		return detailsEntity;
	}

	@Override
	public DetailsEntity getByEmail(String emailId) {
		Session session = null;
		logger.info("invoking {}");
		DetailsEntity detailsEntity = new DetailsEntity();
		try {
			logger.info("Opening session");
			session = factoryBean.openSession();
			logger.info("executing native query");
			Query query = session.createNamedQuery("getByEmailId");
			logger.info("setting parameter for native query");
			query.setParameter("email", emailId);
			logger.info("Extracting unique result from query");
			detailsEntity = (DetailsEntity) query.uniqueResult();
			logger.info("return DetailsEntity ", detailsEntity);
		} catch (Exception e) {
			logger.error("you have an exception " + e.getMessage(), e);
		} finally {
			logger.info("closing DB connection");
			if (session != null) {
				session.close();
				logger.info("session closed");
			}
		}
		return detailsEntity;
	}
	
	@Override
	public List<DetailsEntity> getByDob(String dob) {
		Session session = null;
		logger.info("invoking {}");
		List<DetailsEntity> list = new ArrayList<DetailsEntity>();
		try {
			logger.info("Opening session");
			session = factoryBean.openSession();
			logger.info("executing native query");
			Query query = session.createNamedQuery("getByDob");
			logger.info("setting parameter for native query");
			query.setParameter("dob", dob);
			logger.info("Extracting result from query");
			list = query.list();
			logger.info("return DetailsEntity ", list);
		} catch (Exception e) {
			logger.error("you have an exception " + e.getMessage(), e);
		} finally {
			logger.info("closing DB connection");
			if (session != null) {
				session.close();
				logger.info("session closed");
			}
		}
		return list;
	}

	@Override
	public List<DetailsEntity> CurrentMonthBirthdayList(String thisMonth) {
		Session session = null;
		logger.info("invoking {}");
		List<DetailsEntity> list = new ArrayList<DetailsEntity>();
		try {
			logger.info("Opening session");
			session = factoryBean.openSession();
			logger.info("executing native query");
			Query query = session.createNamedQuery("currentMonthBirthdayList");
			logger.info("setting parameter for native query");
			query.setParameter("dob", thisMonth);
			logger.info("Extracting result from query");
			list = query.list();
			logger.info("return DetailsEntity ", list);
		} catch (Exception e) {
			logger.error("you have an exception " + e.getMessage(), e);
		} finally {
			logger.info("closing DB connection");
			if (session != null) {
				session.close();
				logger.info("session closed");
			}
		}
		return list;
	}

	@Override
	public List<DetailsEntity> CurrentWeekBirthdayList(String[] weekDates) {
		Session session = null;
		logger.info("invoking {}");
		List<DetailsEntity> list = new ArrayList<DetailsEntity>();
		try {
			logger.info("Opening session");
			session = factoryBean.openSession();
			logger.info("executing native query");
			Query query = session.createNamedQuery("currentWeekBirthdayList");
			logger.info("setting parameter for native query");
			query.setParameter("weekDate1", weekDates[0]);
			query.setParameter("weekDate2", weekDates[1]);
			query.setParameter("weekDate3", weekDates[2]);
			query.setParameter("weekDate4", weekDates[3]);
			query.setParameter("weekDate5", weekDates[4]);
			query.setParameter("weekDate6", weekDates[5]);
			query.setParameter("weekDate7", weekDates[6]);
			logger.info("Extracting result from query");
			list = query.list();
			logger.info("return DetailsEntity ", list);
		} catch (Exception e) {
			logger.error("you have an exception " + e.getMessage(), e);
		} finally {
			logger.info("closing DB connection");
			if (session != null) {
				session.close();
				logger.info("session closed");
			}
		}
		return list;
	}

	@Override
	public int updateStatusByEmailId(String emailId) {
		Session session = null;
		logger.info("invoking {}");
		int rowsAffected = 0;
		try {
			logger.info("Opening session");
			session = factoryBean.openSession();
			logger.info("executing native query");
			Query query = session.createNamedQuery("updateStatusByEmailId");
			logger.info("setting parameter for native query");
			query.setParameter("status", true);
			query.setParameter("emailId", emailId);
			logger.info("Extracting unique result from query");
			rowsAffected = query.executeUpdate();
			logger.info("number of rowsAffected  ", rowsAffected);
		} catch (Exception e) {
			logger.error("you have an exception " + e.getMessage(), e);
		} finally {
			logger.info("closing DB connection");
			if (session != null) {
				session.close();
				logger.info("session closed");
			}
		}
		return rowsAffected;
	}
}
