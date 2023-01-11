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
	
	public void add(List<OrderItemForm> form) throws ApiException {
		List<OrderItemPojo> items = new ArrayList<>(); 
		for(OrderItemForm f : form) {
			OrderItemPojo p =convert(f);
			p.setOrderId(1);
			ProductPojo product = productService.getProductByBarcode(f.getBarcode());
			if(product==null)throw new ApiException("The Product "+f.getBarcode()+" Does not exist");
			if(f.getQuantity()<=0)throw new ApiException("Please Enter a Valid Quantity for "+product.getBarcode());
			if(f.getSellingPrice()<0)throw new ApiException("Please Enter a Valid Price for "+product.getBarcode());
			p.setProductId(product.getId());
			items.add(p);
		}
		
		inventoryService.checkAndCreateOrder(items);	
		
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
	
	public List<OrderItemData> getOrderItemByOrderID(int id) throws ApiException, ParserConfigurationException, TransformerException{
		List<OrderItemPojo> list = orderItemService.getOrderItemByOrderId(id);
		List<OrderItemData> items = new ArrayList<>();
		for(OrderItemPojo p : list) {
			OrderItemData item = convert(p);
			item.setBarcode(productService.get(p.getProductId()).getBarcode());
			items.add(item);
		}
		generateInvoiceXML.createXml(items);
		generateInvoicePdf.createPdf();
		return items;
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
