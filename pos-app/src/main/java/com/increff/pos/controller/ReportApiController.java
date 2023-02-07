package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.ReportsDto;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ReportApiController {

	@Autowired
	private final ReportsDto dto = new ReportsDto();

	@ApiOperation(value = "gives inventory report")
	@RequestMapping(path = "/api/reports/inventory", method = RequestMethod.POST)
	public List<InventoryReportData> getInventoryReport(@RequestBody InventoryReportForm form) throws ApiException {
		return dto.getInventoryReport(form);
	}
	
	@ApiOperation(value = "gives sales report")
	@RequestMapping(path = "/api/reports/sales", method = RequestMethod.POST)
	public List<SalesReportData> getSalesReport(@RequestBody SalesReportForm form) throws ApiException{
		return dto.getSalesReport(form);
	}

	@ApiOperation(value = "gives sales report")
	@RequestMapping(path = "/api/reports/brand", method = RequestMethod.POST)
	public List<BrandReportData> getBrandReport(@RequestBody BrandReportForm form){
		return dto.getBrandReport(form);
	}
}
