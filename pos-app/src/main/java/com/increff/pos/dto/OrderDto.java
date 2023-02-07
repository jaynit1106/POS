package com.increff.pos.dto;

import java.io.*;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.Base64Util;
import com.increff.pos.util.JSONUTil;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.ConvertUtil;
import org.springframework.util.FileCopyUtils;

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
	public int addOrder(List<OrderItemForm> orderItemForms) throws ApiException, ParserConfigurationException, TransformerException, IOException {
		List<JSONObject> errors = new ArrayList<>();
		List<OrderItemPojo> orderItemPojoList = new ArrayList<>();

		//checks for the product and conversion to pojo
		for(OrderItemForm orderItemForm : orderItemForms) {
			OrderItemPojo orderItemPojo =ConvertUtil.objectMapper(orderItemForm, OrderItemPojo.class);
			orderItemPojo.setOrderId(1);
			ProductPojo productPojo;

			try {
				productPojo = productService.getProductByBarcode(orderItemForm.getBarcode());
				if(productPojo.getMrp()<orderItemForm.getSellingPrice()){
					errors.add(JSONUTil.getJSONObject(orderItemPojo.getBarcode(),"Selling price should be less than "+productPojo.getMrp()));
				}
			}catch (ApiException e){
				errors.add(JSONUTil.getJSONObject(orderItemPojo.getBarcode(),"Product "+orderItemForm.getBarcode()+" Does Not exists"));
				continue;
			}

			orderItemPojo.setProductId(productPojo.getId());
			orderItemPojoList.add(orderItemPojo);
		}
		if(errors.size()>0){
			throw new ApiException(JSONValue.toJSONString(errors));
		}
		//checking for the inventory
		inventoryService.checkAndCreateOrder(orderItemPojoList);

		//creating order Id
		OrderPojo orderPojo = new OrderPojo();
		orderService.addOrder(orderPojo);
		for (OrderItemPojo orderItemPojo : orderItemPojoList) {
			orderItemPojo.setOrderId(orderPojo.getId());
		}

		//adding the order-items to DB
		orderItemService.addOrderItems(orderItemPojoList);

		//returning the order Id
		return orderPojo.getId();
	}

	
	public OrderData getOrderById(int orderId) throws ApiException {
		OrderPojo orderPojo = orderService.getOrderById(orderId);

		OrderData orderData = new OrderData();
		orderData.setTimestamp(orderPojo.getTimestamp().toString());
		orderData.setId(orderPojo.getId());

		return  orderData;
	}
	
	public List<OrderData> getAllOrders() {
		List<OrderPojo> orderPojoList = orderService.getAllOrders();
		List<OrderData> orderDataList = new ArrayList<>();
		for (OrderPojo orderPojo : orderPojoList) {

			OrderData orderData = new OrderData();
			orderData.setId(orderPojo.getId());
			orderData.setTimestamp(orderPojo.getTimestamp().toString());

			orderDataList.add(orderData);
		}
		Collections.reverse(orderDataList);
		return orderDataList;
	}
	
	public void downloadPdf(int id, HttpServletRequest request,HttpServletResponse response) throws ApiException {
		try {
			String path = new File("./src/main/resources/com/increff/pos/pdf/Invoice "+id+".pdf").getAbsolutePath();
			File file = new File(path);

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			String mimeType = URLConnection.guessContentTypeFromStream(inputStream);

			if(mimeType == null) {
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);
			response.setContentLength((int) file.length());
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));

			FileCopyUtils.copy(inputStream, response.getOutputStream());
		}catch (Exception e) {
			throw new ApiException("Unable to download the file");
		}
	}

	public void generatedPdf(int orderId) throws ApiException {
		List<OrderItemPojo> orderItemPojoList = orderItemService.getOrderItemByOrderId(orderId);
		List<OrderItemData> orderItemDataList = new ArrayList<>();
		List<JSONObject> errors = new ArrayList<>();

		for(OrderItemPojo orderItemPojo : orderItemPojoList) {
			OrderItemData orderItemData = ConvertUtil.objectMapper(orderItemPojo, OrderItemData.class);

			ProductPojo productPojo = productService.getProductById(orderItemPojo.getProductId());
			orderItemData.setBarcode(productPojo.getBarcode());
			orderItemData.setName(productPojo.getName());

			orderItemDataList.add(orderItemData);
		}

		try {
			String base64 = pdfDto.getBase64(orderItemDataList);
			Base64Util.savePdf(base64,"Invoice "+ orderItemDataList.get(0).getOrderId());
		}catch (Exception e){
			errors.add(JSONUTil.getJSONObject("server","Server error"));
		}

		if(errors.size()>0)throw new ApiException(JSONValue.toJSONString(errors));
	}
}
