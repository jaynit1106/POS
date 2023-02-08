package com.increff.pos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;


@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderItemService {

	@Autowired
	private  OrderItemDao orderItemDao;
	
	

	public void addOrderItems(List<OrderItemPojo> orderItemPojoList) throws ApiException {
		for(OrderItemPojo orderItemPojo : orderItemPojoList) {
			orderItemDao.insert(orderItemPojo);
		}
	}

	public List<OrderItemPojo> getOrderItemByOrderId(int orderId) {
		return orderItemDao.selectByOrderId(orderId);
	}
	
}
