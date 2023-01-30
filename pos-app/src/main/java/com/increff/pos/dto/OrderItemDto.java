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
	
	@Autowired
	private final InventoryService inventoryService = new InventoryService();

	@Autowired
	private final OrderService orderService = new OrderService();

	@Transactional(rollbackOn = ApiException.class)
	public void add(List<OrderItemForm> form) throws ApiException, ParserConfigurationException, TransformerException, IOException {
		List<JSONObject> errors = new ArrayList<>();
		List<OrderItemPojo> items = new ArrayList<>();
		for(OrderItemForm f : form) {
			OrderItemPojo p =ConvertUtil.objectMapper(f, OrderItemPojo.class);
			p.setOrderId(1);
			ProductPojo product;
			try {
				product = productService.getProductByBarcode(f.getBarcode());
			}catch (ApiException e){
				errors.add(JSONUTil.getJSONObject("Product "+f.getBarcode()+" Does Not exists"));
				continue;
			}
			p.setProductId(product.getId());
			items.add(p);
		}

		List<JSONObject> inventoryErrors = inventoryService.checkAndCreateOrder(items);
		errors.addAll(inventoryErrors);
		OrderPojo order = new OrderPojo();
		orderService.add(order);
		for (OrderItemPojo item : items) {
			item.setOrderId(order.getId());
		}

		orderItemService.add(items);


		List<OrderItemData> list = new ArrayList<>();
		for(OrderItemPojo p : items) {
			OrderItemData item = ConvertUtil.objectMapper(p, OrderItemData.class);
			ProductPojo product = productService.get(p.getProductId());
			item.setBarcode(product.getBarcode());
			item.setName(product.getName());
			list.add(item);
		}

		try {
			String base64 = Base64Util.getBase64(list);
			Base64Util.savePdf(base64,"Invoice "+ list.get(0).getOrderId());
		}catch (Exception e){
			errors.add(JSONUTil.getJSONObject("Server error"));
		}

		if(errors.size()>0)throw new ApiException(JSONValue.toJSONString(errors));
	}
	
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
