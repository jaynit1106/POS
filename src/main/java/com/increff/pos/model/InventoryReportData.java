package com.increff.pos.model;

import java.util.List;

public class InventoryReportData {
	private List<Integer> brandId;
	private List<Integer> quantity;
	public List<Integer> getBrandId() {
		return brandId;
	}
	public void setBrandId(List<Integer> brandId) {
		this.brandId = brandId;
	}
	public List<Integer> getQuantity() {
		return quantity;
	}
	public void setQuantity(List<Integer> quantity) {
		this.quantity = quantity;
	}


}
