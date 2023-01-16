package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.SchedulerData;
import com.increff.pos.model.SchedulerForm;
import com.increff.pos.pojo.SchedulerPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.SchedulerService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.TimestampUtil;


@Component
public class SchedulerDto {
	
	@Autowired
	private final SchedulerService schedulerService = new SchedulerService();
	
	public void add(SchedulerForm form) throws ApiException{
		form.setDate(TimestampUtil.getTimestamp().substring(0,10));
		SchedulerPojo p = new SchedulerPojo();
		SchedulerPojo data = schedulerService.get(form.getDate());
		if(Objects.isNull(data)) {
			p.setDate(form.getDate());
			p.setInvoiced_orders_count(1);
			p.setInvoiced_items_count(form.getItems_count());
			p.setTotal_revenue(form.getRevenue());
			schedulerService.add(p);
		}else {
			update(form.getDate(), form);
			
		}
		
	}
	
	public SchedulerData get(String date) throws ApiException {
		SchedulerPojo p = schedulerService.get(date);
		return ConvertUtil.objectMapper(p, SchedulerData.class);
	}
	
	public List<SchedulerData> getAll() {
		List<SchedulerPojo> list = schedulerService.getAll();
		List<SchedulerData> list2 = new ArrayList<>();
		for (SchedulerPojo p : list) {
			list2.add(ConvertUtil.objectMapper(p, SchedulerData.class));
		}
		Collections.reverse(list2);
		return list2;
	}
	
	public void update(String date, SchedulerForm f) throws ApiException {
		SchedulerPojo p = new SchedulerPojo();
		SchedulerPojo data = schedulerService.get(date);
		p.setDate(data.getDate());
		p.setInvoiced_orders_count(data.getInvoiced_orders_count()+1);
		p.setInvoiced_items_count(data.getInvoiced_items_count()+f.getItems_count());
		p.setTotal_revenue(data.getTotal_revenue()+f.getRevenue());
		schedulerService.update(date, p);
	}
}
