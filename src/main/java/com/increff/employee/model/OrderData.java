package com.increff.employee.model;

import java.sql.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class OrderData {
	private int id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
