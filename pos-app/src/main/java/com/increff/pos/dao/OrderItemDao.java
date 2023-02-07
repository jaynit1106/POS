package com.increff.pos.dao;

import java.util.List;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderItemPojo;

@Repository
public class OrderItemDao extends AbstractDao {

	private static String SELECT_BY_ORDER_ID = "select p from OrderItemPojo p where orderid=:orderId";

	public List<OrderItemPojo> selectByOrderId(int orderId) {
		TypedQuery<OrderItemPojo> query = getQuery(SELECT_BY_ORDER_ID, OrderItemPojo.class);
		query.setParameter("orderId", orderId);
		return query.getResultList();
	}


}
