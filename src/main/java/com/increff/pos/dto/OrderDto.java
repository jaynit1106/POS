package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.increff.pos.model.OrderData;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.util.ConvertUtil;
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
		return ConvertUtil.objectMapper(orderService.get(id),OrderData.class);
	}
	
	public List<OrderData> getAll() {
		List<OrderPojo> list = orderService.getAll();
		List<OrderData> list2 = new ArrayList<OrderData>();
		for (OrderPojo p : list) {
			list2.add(ConvertUtil.objectMapper(p, OrderData.class));
		}
		Collections.reverse(list2);
		return list2;
	}
	
	public void downloadPdf(int id, HttpServletRequest request,HttpServletResponse response) throws ApiException {
		orderService.downloadPdf(id, request, response);	
	}
}
