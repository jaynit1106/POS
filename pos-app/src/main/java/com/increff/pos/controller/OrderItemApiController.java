package com.increff.pos.controller;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderItemApiController {

	@Autowired
	private final OrderItemDto dto = new OrderItemDto();

	@ApiOperation(value = "Adds a Order Item")
	@RequestMapping(path = "/api/orderitem", method = RequestMethod.POST)
	public void add(@RequestBody List<OrderItemForm> form) throws ApiException, ParserConfigurationException, TransformerException, IOException {
		dto.add(form);
	}

	@ApiOperation(value = "Gets a Order Item by orderID")
	@RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.GET)
	public List<OrderItemData> get(@PathVariable int id) throws ApiException, ParserConfigurationException, TransformerException {
		return dto.getOrderItemByOrderID(id);
	}

	@ApiOperation(value = "Gets list of all Order Items")
	@RequestMapping(path = "/api/orderitem", method = RequestMethod.GET)
	public List<OrderItemData> getAll() {
		return dto.getAll();
	}

	@ApiOperation(value = "Updates a product Quantity")
	@RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody OrderItemForm form) throws ApiException {
		dto.update(id, form);
	}
	


}
