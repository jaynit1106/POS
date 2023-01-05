package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderItemApiController {

	@Autowired
	private OrderItemService service;

	@ApiOperation(value = "Adds a Order Item")
	@RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.POST)
	public void add(@RequestBody OrderItemForm form,@PathVariable int id) throws ApiException {
		OrderItemPojo p =convert(form);
		p.setOrderId(id);
		p.setProductId(service.getProductId(form.getBarcode()));
		service.add(p);
	}

	
	@ApiOperation(value = "Deletes a Order Item")
	@RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets a Order Item by ID")
	@RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.GET)
	public OrderItemData get(@PathVariable int id) throws ApiException {
		OrderItemPojo p = service.get(id);
		return convert(p);
	}

	@ApiOperation(value = "Gets list of all Order Items")
	@RequestMapping(path = "/api/orderitem", method = RequestMethod.GET)
	public List<OrderItemData> getAll() {
		List<OrderItemPojo> list = service.getAll();
		List<OrderItemData> list2 = new ArrayList<OrderItemData>();
		for (OrderItemPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates a product Quantity")
	@RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody OrderItemForm f) throws ApiException {
		OrderItemPojo p = convert(f);
		service.update(id, p);
	}
	

	private static OrderItemData convert(OrderItemPojo p) {
		OrderItemData d = new OrderItemData();
		d.setId(p.getId());
		d.setQuantity(p.getQuantity());
		d.setSellingPrice(p.getSellingPrice());
		return d;
	}

	private static OrderItemPojo convert(OrderItemForm f) {
		OrderItemPojo p = new OrderItemPojo();
		p.setQuantity(f.getQuantity());
		p.setSellingPrice(f.getSellingPrice());
		return p;
	}

}
