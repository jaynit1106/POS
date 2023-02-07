package com.increff.pos.dao;

import java.time.Instant;
import java.util.List;
import javax.persistence.TypedQuery;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.SchedulerPojo;

@Repository
public class SchedulerDao extends AbstractDao {

	
	private static String SELECT_DATE = "select p from SchedulerPojo p where date=:date";
	private static String SELECT_RANGE = "select p from SchedulerPojo p where date>=:startDate and date<=:endDate";
	
	public SchedulerPojo select(String date) {
		TypedQuery<SchedulerPojo> query = getQuery(SELECT_DATE, SchedulerPojo.class);
		query.setParameter("date", date);
		return getSingle(query);
	}

	public List<SchedulerPojo> selectRange(String startDate,String endDate){
		TypedQuery<SchedulerPojo> query = getQuery(SELECT_RANGE, SchedulerPojo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

}
