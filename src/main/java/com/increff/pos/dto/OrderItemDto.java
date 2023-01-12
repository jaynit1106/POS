package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.generateInvoicePdf;
import com.increff.pos.util.generateInvoiceXML;

@Component
public class OrderItemDto {
	
	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private InventoryService inventoryService;
	
	public void add(List<OrderItemForm> form) throws ApiException, ParserConfigurationException, TransformerException {
		List<OrderItemPojo> items = new ArrayList<>(); 
		for(OrderItemForm f : form) {
			OrderItemPojo p =ConvertUtil.objectMapper(f, OrderItemPojo.class);
			p.setOrderId(1);
			ProductPojo product = productService.getProductByBarcode(f.getBarcode());
			if(product==null)throw new ApiException("The Product "+f.getBarcode()+" Does not exist");
			if(f.getQuantity()<=0)throw new ApiException("Please Enter a Valid Quantity for "+product.getBarcode());
			if(f.getSellingPrice()<0)throw new ApiException("Please Enter a Valid Price for "+product.getBarcode());
			p.setProductId(product.getId());
			items.add(p);
		}
		
		int orderId = inventoryService.checkAndCreateOrder(items);
		items = orderItemService.getOrderItemByOrderId(orderId);
		List<OrderItemData> list = new ArrayList<>();
		for(OrderItemPojo p : items) {
			OrderItemData item = ConvertUtil.objectMapper(p, OrderItemData.class);
			ProductPojo product = productService.get(p.getProductId());
			item.setBarcode(product.getBarcode());
			item.setName(product.getName());
			list.add(item);
		}
		generateInvoiceXML.createXml(list);
		generateInvoicePdf.createPdf("Invoice "+String.valueOf(orderId));
		
	}
	
	public OrderItemData get(int id) throws ApiException {
		return ConvertUtil.objectMapper(orderItemService.get(id),OrderItemData.class);
	}
	
	public List<OrderItemData> getAll() {
		List<OrderItemPojo> list = orderItemService.getAll();
		List<OrderItemData> list2 = new ArrayList<OrderItemData>();
		for (OrderItemPojo p : list) {
			list2.add(ConvertUtil.objectMapper(p,OrderItemData.class));
		}
		return list2;
	}
	
	public void update(int id,OrderItemForm form) throws ApiException {
		OrderItemPojo p = ConvertUtil.objectMapper(form,OrderItemPojo.class);
		orderItemService.update(id, p);
	}
	
	public List<OrderItemData> getOrderItemByOrderID(int id) throws ApiException, ParserConfigurationException, TransformerException{
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
