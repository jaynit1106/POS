package com.increff.pos.dao;

import java.util.List;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderItemPojo;

@Repository
public class OrderItemDao extends AbstractDao {
	
	private static String select_id = "select p from OrderItemPojo p where id=:id";
	private static String select_all = "select p from OrderItemPojo p";
	private static String select_by_order_id = "select p from OrderItemPojo p where orderid=:orderId";

	
	public OrderItemPojo select(int id) {
		TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<OrderItemPojo> selectAll() {
		TypedQuery<OrderItemPojo> query = getQuery(select_all, OrderItemPojo.class);
		return query.getResultList();
	}
	
	public List<OrderItemPojo> selectByOrderId(int orderId) {
		TypedQuery<OrderItemPojo> query = getQuery(select_by_order_id, OrderItemPojo.class);
		query.setParameter("orderId", orderId);
		return query.getResultList();
	}

	public void update(OrderItemPojo p) {
	}

}
