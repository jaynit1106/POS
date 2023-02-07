package com.increff.pos.dto;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.Base64Util;
import com.increff.pos.util.JSONUTil;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.increff.pos.model.OrderData;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.ConvertUtil;

@Component
public class OrderDto {
	@Autowired
	private PdfDto pdfDto;

	@Autowired
	private  OrderItemService orderItemService;

	@Autowired
	private  ProductService productService;

	@Autowired
	private  InventoryService inventoryService;
	@Autowired
	private  OrderService orderService;

	@Transactional(rollbackOn = ApiException.class)
	public int add(List<OrderItemForm> form) throws ApiException, ParserConfigurationException, TransformerException, IOException {
		List<JSONObject> errors = new ArrayList<>();
		List<OrderItemPojo> items = new ArrayList<>();
		for(OrderItemForm f : form) {
			OrderItemPojo p =ConvertUtil.objectMapper(f, OrderItemPojo.class);
			p.setOrderId(1);
			ProductPojo product;
			try {
				product = productService.getProductByBarcode(f.getBarcode());
				if(product.getMrp()<f.getSellingPrice()){
					errors.add(JSONUTil.getJSONObject(p.getBarcode(),"Selling price should be less than "+product.getMrp()));
				}
			}catch (ApiException e){
				errors.add(JSONUTil.getJSONObject(p.getBarcode(),"Product "+f.getBarcode()+" Does Not exists"));
				continue;
			}
			p.setProductId(product.getId());
			items.add(p);
		}
		if(errors.size()>0){
			throw new ApiException(JSONValue.toJSONString(errors));
		}
		inventoryService.checkAndCreateOrder(items);

		OrderPojo order = new OrderPojo();
		orderService.add(order);
		for (OrderItemPojo item : items) {
			item.setOrderId(order.getId());
		}

		orderItemService.add(items);
		return order.getId();
	}

	
	public OrderData get(int id) throws ApiException {
		OrderPojo p = orderService.get(id);
		OrderData data = new OrderData();
		data.setTimestamp(p.getTimestamp().toString());
		data.setId(p.getId());
		return  data;
	}
	
	public List<OrderData> getAll() {
		List<OrderPojo> list = orderService.getAll();
		List<OrderData> list2 = new ArrayList<>();
		for (OrderPojo p : list) {
			OrderData data = new OrderData();
			data.setId(p.getId());
			data.setTimestamp(p.getTimestamp().toString());
			list2.add(data);
		}
		Collections.reverse(list2);
		return list2;
	}
	
	public void downloadPdf(int id, HttpServletRequest request,HttpServletResponse response) throws ApiException {
		orderService.downloadPdf(id, request, response);	
	}

	public void generatedPdf(int id) throws ApiException {
		List<OrderItemPojo> items = orderItemService.getOrderItemByOrderId(id);
		List<OrderItemData> list = new ArrayList<>();
		List<JSONObject> errors = new ArrayList<>();

		for(OrderItemPojo p : items) {
			OrderItemData item = ConvertUtil.objectMapper(p, OrderItemData.class);
			ProductPojo product = productService.get(p.getProductId());
			item.setBarcode(product.getBarcode());
			item.setName(product.getName());
			list.add(item);
		}

		try {
			String base64 = pdfDto.getBase64(list);
			Base64Util.savePdf(base64,"Invoice "+ list.get(0).getOrderId());
		}catch (Exception e){
			errors.add(JSONUTil.getJSONObject("server","Server error"));
		}

		if(errors.size()>0)throw new ApiException(JSONValue.toJSONString(errors));
	}
}
