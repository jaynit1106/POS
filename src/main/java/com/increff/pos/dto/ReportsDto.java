package com.increff.pos.dto;

import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ReportsService;
import com.increff.pos.util.generatePdf;
import com.increff.pos.util.generateXml;

@Component
public class ReportsDto {

	@Autowired
	private ReportsService reportService;
	
	public HashMap<Integer,Integer> getInventoryReport() throws ApiException {
		return reportService.getInventoryReport();
	}
	
	public void getSalesReport(SalesReportForm form) throws ApiException, ParserConfigurationException, TransformerException {
		form.setStartDate(form.getStartDate()+" 00:00");
		form.setEndDate(form.getEndDate()+" 23:59");
		List<SalesReportData> data = reportService.getSalesReport(form.getStartDate(), form.getEndDate());
		generateXml.createXml(data);
		generatePdf.createPdf();
	}
	
}
