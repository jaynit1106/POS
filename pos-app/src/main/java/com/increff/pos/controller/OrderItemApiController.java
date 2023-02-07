package com.increff.pos.controller;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderItemApiController {

	@Autowired
	private OrderItemDto orderItemDto;

	@ApiOperation(value = "Gets a Order Item by orderID")
	@RequestMapping(path = "/api/orderitems/{orderId}", method = RequestMethod.GET)
	public List<OrderItemData> getOrderItemsById(@PathVariable int orderId) throws ApiException, ParserConfigurationException, TransformerException {
		return orderItemDto.getOrderItemByOrderID(orderId);
	}

}
