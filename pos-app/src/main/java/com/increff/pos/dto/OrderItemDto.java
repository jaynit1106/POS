package com.increff.pos.dto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.Base64Util;
import com.increff.pos.util.JSONUTil;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ConvertUtil;
import org.json.simple.JSONObject;
@Component
public class OrderItemDto {
	@Autowired
	private final OrderItemService orderItemService = new OrderItemService();
	
	@Autowired
	private final ProductService productService = new ProductService();


	public OrderItemData get(int id) throws ApiException {
		return ConvertUtil.objectMapper(orderItemService.get(id),OrderItemData.class);
	}
	
	public List<OrderItemData> getAll() {
		List<OrderItemPojo> list = orderItemService.getAll();
		List<OrderItemData> list2 = new ArrayList<>();
		for (OrderItemPojo p : list) {
			list2.add(ConvertUtil.objectMapper(p,OrderItemData.class));
		}
		return list2;
	}
	
	public void update(int id,OrderItemForm form) throws ApiException {
		OrderItemPojo p = ConvertUtil.objectMapper(form,OrderItemPojo.class);
		orderItemService.update(id, p);
	}
	
	public List<OrderItemData> getOrderItemByOrderID(int id) throws ApiException {
		List<OrderItemPojo> list = orderItemService.getOrderItemByOrderId(id);
		List<OrderItemData> items = new ArrayList<>();
		for(OrderItemPojo p : list) {
			OrderItemData item = ConvertUtil.objectMapper(p, OrderItemData.class);
			ProductPojo product = productService.get(p.getProductId());
			item.setBarcode(product.getBarcode());
			item.setName(product.getName());
			items.add(item);
		}
		return items;
	}
}
