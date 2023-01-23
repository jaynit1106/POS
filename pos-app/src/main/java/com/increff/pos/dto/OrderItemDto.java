package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.SchedulerForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.generateInvoicePdf;
import com.increff.pos.util.generateInvoiceXML;

@Component
public class OrderItemDto {
	
	@Autowired
	private final OrderItemService orderItemService = new OrderItemService();
	
	@Autowired
	private final ProductService productService = new ProductService();
	
	@Autowired
	private final InventoryService inventoryService = new InventoryService();
	
	@Autowired
	private final SchedulerDto schedulerDto = new SchedulerDto();

	@Autowired
	private final OrderService orderService = new OrderService();

	@Transactional(rollbackOn = ApiException.class)
	public void add(List<OrderItemForm> form) throws ApiException, ParserConfigurationException, TransformerException {
		List<OrderItemPojo> items = new ArrayList<>(); 
		for(OrderItemForm f : form) {
			OrderItemPojo p =ConvertUtil.objectMapper(f, OrderItemPojo.class);
			p.setOrderId(1);

			ProductPojo product = productService.getProductByBarcode(f.getBarcode());
			p.setProductId(product.getId());

			items.add(p);
		}

		inventoryService.checkAndCreateOrder(items);

		OrderPojo order = new OrderPojo();
		orderService.add(order);
		for (OrderItemPojo item : items) {
			item.setOrderId(order.getId());
		}

		orderItemService.add(items);


		List<OrderItemData> list = new ArrayList<>();
		double total = 0;
		Integer totalItems = 0;
		for(OrderItemPojo p : items) {
			OrderItemData item = ConvertUtil.objectMapper(p, OrderItemData.class);
			ProductPojo product = productService.get(p.getProductId());
			item.setBarcode(product.getBarcode());
			item.setName(product.getName());
			total += (item.getSellingPrice()*item.getQuantity());
			list.add(item);
			totalItems+=p.getQuantity();
		}
		generateInvoiceXML.createXml(list);
		generateInvoicePdf.createPdf("Invoice "+ order.getId());

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
