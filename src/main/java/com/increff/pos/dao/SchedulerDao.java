package com.increff.pos.dao;

import java.util.List;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.SchedulerPojo;

@Repository
public class SchedulerDao extends AbstractDao {

	
	private static String select_date = "select p from SchedulerPojo p where date=:date";

	
	public SchedulerPojo select(String date) {
		TypedQuery<SchedulerPojo> query = getQuery(select_date, SchedulerPojo.class);
		query.setParameter("date", date);
		return getSingle(query);
	}

}
