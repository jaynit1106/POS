package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.ProductService;

@Component
public class OrderItemDto {
	
	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private ProductService productService;
	
	public void add(OrderItemForm form,int id) throws ApiException {
		OrderItemPojo p =convert(form);
		p.setOrderId(id);
		p.setProductId(productService.getProductIdByBarcode(form.getBarcode()));
		orderItemService.add(p);
	}
	
	public OrderItemData get(int id) throws ApiException {
		return convert(orderItemService.get(id));
	}
	
	public List<OrderItemData> getAll() {
		List<OrderItemPojo> list = orderItemService.getAll();
		List<OrderItemData> list2 = new ArrayList<OrderItemData>();
		for (OrderItemPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}
	
	public void update(int id,OrderItemForm form) throws ApiException {
		OrderItemPojo p = convert(form);
		orderItemService.update(id, p);
	}
	

	private static OrderItemData convert(OrderItemPojo p) {
		OrderItemData d = new OrderItemData();
		d.setId(p.getId());
		d.setQuantity(p.getQuantity());
		d.setSellingPrice(p.getSellingPrice());
		return d;
	}

	private static OrderItemPojo convert(OrderItemForm f) {
		OrderItemPojo p = new OrderItemPojo();
		p.setQuantity(f.getQuantity());
		p.setSellingPrice(f.getSellingPrice());
		return p;
	}
	
}
