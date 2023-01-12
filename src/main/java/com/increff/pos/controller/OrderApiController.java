package com.increff.pos.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.OrderData;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderApiController {

	@Autowired
	private OrderDto dto;

	@ApiOperation(value = "Adds a Order")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public OrderData add() throws ApiException {
		return dto.add();
	}

	@ApiOperation(value = "Gets a Order by ID")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.GET)
	public OrderData get(@PathVariable int id) throws ApiException {
		return dto.get(id);
	}

	@ApiOperation(value = "Gets list of all Orders")
	@RequestMapping(path = "/api/order", method = RequestMethod.GET)
	public List<OrderData> getAll() {
		return dto.getAll();
	}
	
	@ApiOperation(value = "Gets Pdf")
	@RequestMapping(path = "/api/order/download/{id}", method = RequestMethod.GET)
	public void downloadPdf(@PathVariable int id, HttpServletRequest request,HttpServletResponse response) throws ApiException {
		dto.downloadPdf(id, request, response);
	}
	
}
