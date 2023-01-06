package com.increff.pos.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.SalesReportForm;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ReportsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ReportApiController {

	@Autowired
	private ReportsService service;

	@ApiOperation(value = "gives inventory report")
	@RequestMapping(path = "/api/report/inventory", method = RequestMethod.GET)
	public HashMap<Integer,Integer> getInventoryReport() throws ApiException {
		HashMap<Integer,Integer> inventory = service.getInventoryReport();
		return inventory;
//		InventoryReportData  data = new InventoryReportData();
//		data.setBrandId(inventory.get(0));
//		data.setQuantity(inventory.get(1));
//		return data;
	}
	
	@ApiOperation(value = "gives sales report")
	@RequestMapping(path = "/api/report/sales", method = RequestMethod.POST)
	public List<SalesReportData> getSalesReport(@RequestBody SalesReportForm form) throws ApiException {
		return service.getSalesReport(form.getStartDate(), form.getEndDate());
	}
}
