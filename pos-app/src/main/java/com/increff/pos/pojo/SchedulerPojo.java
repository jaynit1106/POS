package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class SchedulerPojo {
	@Id
	private String date;
	private int invoiced_orders_count;
	private int invoiced_items_count;
	private double total_revenue;
}
