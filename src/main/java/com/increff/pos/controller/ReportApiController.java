package com.increff.pos.controller;

import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.SalesReportForm;
import com.increff.pos.dto.ReportsDto;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ReportApiController {

	@Autowired
	private ReportsDto dto;

	@ApiOperation(value = "gives inventory report")
	@RequestMapping(path = "/api/report/inventory", method = RequestMethod.GET)
	public HashMap<Integer,Integer> getInventoryReport() throws ApiException {
		return dto.getInventoryReport();
	}
	
	@ApiOperation(value = "gives sales report")
	@RequestMapping(path = "/api/report/sales", method = RequestMethod.POST)
	public void getSalesReport(@RequestBody SalesReportForm form) throws ApiException, ParserConfigurationException, TransformerException {
		dto.getSalesReport(form);
	}
}