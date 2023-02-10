package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter @Setter
@Table(
		name = "scheduler_pojo"
)
public class SchedulerPojo {
	@Id
	private String date;
	@Column(nullable = false)
	private int invoiced_orders_count;
	@Column(nullable = false)
	private int invoiced_items_count;
	@Column(nullable = false)
	private double total_revenue;
}
