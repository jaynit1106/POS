package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.increff.pos.model.OrderData;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.util.TimestampUtil;

@Component
public class OrderDto {

	@Autowired
	private OrderService orderService;
	
	public OrderData add() throws ApiException {
		OrderPojo p = new OrderPojo();
		p.setTimestamp(TimestampUtil.getTimestamp());
		orderService.add(p);
		OrderData data = new OrderData();
		data.setId(p.getId());
		data.setTimestamp(p.getTimestamp());
		return data;
	}
	
	public OrderData get(@PathVariable int id) throws ApiException {
		return convert(orderService.get(id));
	}
	
	public List<OrderData> getAll() {
		List<OrderPojo> list = orderService.getAll();
		List<OrderData> list2 = new ArrayList<OrderData>();
		for (OrderPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}
	

	private static OrderData convert(OrderPojo p) {
		OrderData d = new OrderData();
		d.setId(p.getId());
		d.setTimestamp(p.getTimestamp());
		return d;
	}
}
