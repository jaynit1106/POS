package com.increff.pos.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.increff.pos.model.form.SchedulerForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.OrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.SchedulerData;
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

	private static Logger logger = Logger.getLogger(SchedulerDto.class);

	@Scheduled(cron = "00 00 12 * * *")
	public void addReport() throws ApiException{

		SchedulerPojo schedulerPojo = new SchedulerPojo();
		schedulerPojo.setDate(TimestampUtil.getTimestamp().substring(0,10));

		List<OrderPojo> orderPojoList = orderService.selectOrdersInRange(Instant.parse(TimestampUtil.getTimestamp().substring(0,10)+"T00:00:00Z"),Instant.parse(TimestampUtil.getTimestamp().substring(0,10)+"T23:59:59Z"));
		schedulerPojo.setInvoiced_orders_count(orderPojoList.size());

		double revenue = 0;
		Integer totalItems = 0;

		for(OrderPojo orderPojo : orderPojoList){
			List<OrderItemPojo> orderItemPojoList = orderService.getOrderItemByOrderId(orderPojo.getId());
			for(OrderItemPojo orderItemPojo : orderItemPojoList){
				totalItems+=orderItemPojo.getQuantity();
				revenue+=(orderItemPojo.getQuantity()*orderItemPojo.getSellingPrice());
			}
		}

		schedulerPojo.setTotal_revenue(revenue);
		schedulerPojo.setInvoiced_items_count(totalItems);

		schedulerService.addReport(schedulerPojo);
		logger.info("Created Daily report at "+ Instant.now());
	}

	public List<SchedulerData> getReportInRange(SchedulerForm schedulerForm) throws ApiException {
		List<SchedulerPojo> schedulerPojoList = schedulerService.getReportInRange(schedulerForm.getStartDate(),schedulerForm.getEndDate());
		List<SchedulerData> schedulerDataList = new ArrayList<>();

		for (SchedulerPojo schedulerPojo : schedulerPojoList) {
			schedulerDataList.add(ConvertUtil.objectMapper(schedulerPojo, SchedulerData.class));
		}

		Collections.reverse(schedulerDataList);
		return schedulerDataList;
	}

}
