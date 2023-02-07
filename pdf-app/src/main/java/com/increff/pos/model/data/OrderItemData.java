package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData{
	private int id;
	private int orderId;
	private int productId;
	private String name;
	private String sellingPrice;
	private String barcode;
	private int quantity;


}
