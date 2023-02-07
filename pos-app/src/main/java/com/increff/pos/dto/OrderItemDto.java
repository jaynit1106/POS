package com.increff.pos.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ConvertUtil;

@Component
public class OrderItemDto {
	@Autowired
	private  OrderItemService orderItemService;
	
	@Autowired
	private  ProductService productService;


	public List<OrderItemData> getOrderItemByOrderID(int id) throws ApiException {
		List<OrderItemPojo> orderItemPojoList = orderItemService.getOrderItemByOrderId(id);
		List<OrderItemData> orderItemDataList = new ArrayList<>();

		for(OrderItemPojo orderItemPojo : orderItemPojoList) {
			OrderItemData orderItemData = ConvertUtil.objectMapper(orderItemPojo, OrderItemData.class);

			ProductPojo productPojo = productService.getProductById(orderItemPojo.getProductId());
			orderItemData.setSellingPrice(String.format("%.2f", new BigDecimal(orderItemPojo.getSellingPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue()));
			orderItemData.setBarcode(productPojo.getBarcode());
			orderItemData.setName(productPojo.getName());

			orderItemDataList.add(orderItemData);
		}

		return orderItemDataList;
	}
}
