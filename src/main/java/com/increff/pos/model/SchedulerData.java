package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchedulerData {
	private String date;
	private int invoiced_orders_count;
	private int invoiced_items_count;
	private double total_revenue;


}
