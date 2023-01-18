package com.increff.pos.dto;

import java.time.temporal.ChronoUnit;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportForm;
import com.increff.pos.service.ReportsService;

@Component
public class ReportsDto {

	@Autowired
	private final ReportsService reportService = new ReportsService();
	
	public List<InventoryReportData> getInventoryReport() {
		return reportService.getInventoryReport();
	}
	
	public List<SalesReportData> getSalesReport(SalesReportForm form) {

		return reportService.getSalesReport(form.getStartDate(), form.getEndDate());
	}
	
}
