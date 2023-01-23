package com.increff.pos.dao;

import java.util.List;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderItemPojo;

@Repository
public class OrderItemDao extends AbstractDao {

	private static String select_by_order_id = "select p from OrderItemPojo p where orderid=:orderId";

	public List<OrderItemPojo> selectByOrderId(int orderId) {
		TypedQuery<OrderItemPojo> query = getQuery(select_by_order_id, OrderItemPojo.class);
		query.setParameter("orderId", orderId);
		return query.getResultList();
	}

	public void update(OrderItemPojo p) {
	}

}
