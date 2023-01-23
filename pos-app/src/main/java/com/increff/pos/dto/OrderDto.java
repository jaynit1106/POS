package com.increff.pos.dto;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

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
	private final OrderService orderService = new OrderService();
	
	public OrderData add() throws ApiException {
		OrderPojo p = new OrderPojo();
		orderService.add(p);
		OrderData data = new OrderData();
		data.setId(p.getId());
		data.setTimestamp(p.getTimestamp().toString());
		return data;
	}
	
	public OrderData get(@PathVariable int id) throws ApiException {
		OrderPojo p = orderService.get(id);
		OrderData data = new OrderData();
		data.setTimestamp(p.getTimestamp().toString());
		data.setId(p.getId());
		return  data;
	}
	
	public List<OrderData> getAll() {
		List<OrderPojo> list = orderService.getAll();
		List<OrderData> list2 = new ArrayList<>();
		for (OrderPojo p : list) {
			OrderData data = new OrderData();
			data.setId(p.getId());
			data.setTimestamp(p.getTimestamp().toString());
			list2.add(data);
		}
		Collections.reverse(list2);
		return list2;
	}
	
	public void downloadPdf(int id, HttpServletRequest request,HttpServletResponse response) throws ApiException {
		orderService.downloadPdf(id, request, response);	
	}
}