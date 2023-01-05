package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.OrderData;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.util.TimestampUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderApiController {

	@Autowired
	private OrderService service;

	@ApiOperation(value = "Adds a Order")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public void add() throws ApiException {
		OrderPojo p = new OrderPojo();
		p.setTimestamp(TimestampUtil.getTimestamp());
		service.add(p);
	}

	
	@ApiOperation(value = "Deletes a Order")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets a Order by ID")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.GET)
	public OrderData get(@PathVariable int id) throws ApiException {
		OrderPojo p = service.get(id);
		return convert(p);
	}

	@ApiOperation(value = "Gets list of all Orders")
	@RequestMapping(path = "/api/order", method = RequestMethod.GET)
	public List<OrderData> getAll() {
		List<OrderPojo> list = service.getAll();
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
