package com.increff.pos.service;

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
	private final OrderItemDao dao = new OrderItemDao();
	
	

	public void add(List<OrderItemPojo> items) throws ApiException {
		for(OrderItemPojo p : items) {
			
			dao.insert(p);
		}
		
	}

	public OrderItemPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	public List<OrderItemPojo> getAll() {
		return dao.selectAll(OrderItemPojo.class);
	}


	public OrderItemPojo getCheck(int id) throws ApiException {
		OrderItemPojo p = dao.select(id,OrderItemPojo.class);
		if (Objects.isNull(p)) {
			throw new ApiException("OrderItem with given ID does not exit, id: " + id);
		}
		return p;
	}
	
	public List<OrderItemPojo> getOrderItemByOrderId(int id) {
		return dao.selectByOrderId(id);
	}
	
}
