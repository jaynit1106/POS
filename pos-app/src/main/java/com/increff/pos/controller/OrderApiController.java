package com.increff.pos.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderItemForm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderApiController {

	@Autowired
	private  OrderDto orderDto;

	private static Logger logger = Logger.getLogger(OrderApiController.class);

	@ApiOperation(value = "Adds a Order")
	@RequestMapping(path = "/api/orders", method = RequestMethod.POST)
	public void addOrder(@RequestBody List<OrderItemForm> orderItemForms) throws ApiException, ParserConfigurationException, TransformerException, IOException {
		int orderId = orderDto.addOrder(orderItemForms);
		orderDto.generatedPdf(orderId);
		logger.info("Created PDF for " + orderId);
	}

	@ApiOperation(value = "Gets a Order by ID")
	@RequestMapping(path = "/api/orders/{orderId}", method = RequestMethod.GET)
	public OrderData getOrderById(@PathVariable int orderId) throws ApiException {
		return orderDto.getOrderById(orderId);
	}

	@ApiOperation(value = "Gets list of all Orders")
	@RequestMapping(path = "/api/orders", method = RequestMethod.GET)
	public List<OrderData> getAllOrders() {
		return orderDto.getAllOrders();
	}
	
	@ApiOperation(value = "Gets Pdf")
	@RequestMapping(path = "/api/orders/download/{orderId}", method = RequestMethod.GET)
	public void downloadPdf(@PathVariable int orderId, HttpServletRequest request,HttpServletResponse response) throws ApiException {
		orderDto.downloadPdf(orderId, request, response);
	}

	@ApiOperation(value = "Gets a Order Item by orderID")
	@RequestMapping(path = "/api/orderitems/{orderId}", method = RequestMethod.GET)
	public List<OrderItemData> getOrderItemsById(@PathVariable int orderId) throws ApiException, ParserConfigurationException, TransformerException {
		return orderDto.getOrderItemByOrderID(orderId);
	}
	
}
