package com.increff.pos.model;


public class SalesReportData {
	private String brand;
	private String category;
	private double revenue;
	private int quantity;
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public double getRevenue() {
		return revenue;
	}
	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}
	
	
}
