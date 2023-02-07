package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.model.data.BrandReportData;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.BrandReportForm;
import com.increff.pos.model.form.InventoryReportForm;
import com.increff.pos.model.form.SalesReportForm;
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
	private ReportsDto reportsDto;

	@ApiOperation(value = "gives inventory report")
	@RequestMapping(path = "/api/reports/inventory", method = RequestMethod.POST)
	public List<InventoryReportData> getInventoryReport(@RequestBody InventoryReportForm inventoryReportForm) throws ApiException {
		return reportsDto.getInventoryReport(inventoryReportForm);
	}
	
	@ApiOperation(value = "gives sales report")
	@RequestMapping(path = "/api/reports/sales", method = RequestMethod.POST)
	public List<SalesReportData> getSalesReport(@RequestBody SalesReportForm salesReportForm) throws ApiException{
		return reportsDto.getSalesReport(salesReportForm);
	}

	@ApiOperation(value = "gives sales report")
	@RequestMapping(path = "/api/reports/brand", method = RequestMethod.POST)
	public List<BrandReportData> getBrandReport(@RequestBody BrandReportForm brandReportForm){
		return reportsDto.getBrandReport(brandReportForm);
	}
}
