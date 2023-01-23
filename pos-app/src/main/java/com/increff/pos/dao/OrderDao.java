package com.increff.pos.dao;

import java.time.Instant;
import java.util.List;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderPojo;

@Repository
public class OrderDao extends AbstractDao {

	private static String select_range = "select p from OrderPojo p where timestamp>=:startDate and timestamp<=:endDate";

	public List<OrderPojo> selectRange(Instant startDate , Instant endDate){
		TypedQuery<OrderPojo> query = getQuery(select_range, OrderPojo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}
}