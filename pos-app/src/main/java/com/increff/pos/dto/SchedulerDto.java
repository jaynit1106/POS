package com.increff.pos.dto;

import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.increff.pos.model.SchedulerForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import io.swagger.models.auth.In;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.increff.pos.model.SchedulerData;
import com.increff.pos.pojo.SchedulerPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.SchedulerService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.TimestampUtil;


@Component
public class SchedulerDto {
	
	@Autowired
	private  SchedulerService schedulerService;
	@Autowired
	private  OrderService orderService;
	@Autowired
	private  OrderItemService orderItemService;

	private static Logger logger = Logger.getLogger(SchedulerDto.class);

	@Scheduled(cron = "00 00 12 * * *")
	public void add() throws ApiException{
		SchedulerPojo p = new SchedulerPojo();
		p.setDate(TimestampUtil.getTimestamp().substring(0,10));
		List<OrderPojo> list = orderService.selectRange(Instant.parse(TimestampUtil.getTimestamp().substring(0,10)+"T00:00:00Z"),Instant.parse(TimestampUtil.getTimestamp().substring(0,10)+"T23:59:59Z"));
		p.setInvoiced_orders_count(list.size());
		double revenue = 0;
		Integer totalItems = 0;
		for(OrderPojo order : list){
			List<OrderItemPojo> item = orderItemService.getOrderItemByOrderId(order.getId());
			for(OrderItemPojo pojo : item){
				totalItems+=pojo.getQuantity();
				revenue+=(pojo.getQuantity()*pojo.getSellingPrice());
			}
		}
		p.setTotal_revenue(revenue);
		p.setInvoiced_items_count(totalItems);
		schedulerService.add(p);
		logger.info("Created Daily report at "+ Instant.now());
	}
	
	public SchedulerData get(String date) throws ApiException {
		SchedulerPojo p = schedulerService.get(date);
		return ConvertUtil.objectMapper(p, SchedulerData.class);
	}
	
	public List<SchedulerData> getRange(SchedulerForm form) throws ApiException {
		List<SchedulerPojo> list = schedulerService.getRange(form.getStartDate(),form.getEndDate());
		List<SchedulerData> list2 = new ArrayList<>();
		for (SchedulerPojo p : list) {
			list2.add(ConvertUtil.objectMapper(p, SchedulerData.class));
		}
		Collections.reverse(list2);
		return list2;
	}

}
