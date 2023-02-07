package com.increff.pos.dto;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.util.MapUtil;
import io.swagger.models.auth.In;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportsDto {

	@Autowired
	private  BrandService brandService;
	@Autowired
	private  ProductService productService;
	@Autowired
	private  InventoryService inventoryService;
	@Autowired
	private  OrderService orderService ;
	@Autowired
	private  OrderItemService orderItemService;

	private static Logger logger = Logger.getLogger(ReportsDto.class);


	public List<BrandReportData> getBrandReport(BrandReportForm form){
		logger.info("creating brand report");
		List<BrandPojo> list = brandService.getAll();
		List<BrandReportData> data = new ArrayList<>();
		for(BrandPojo p : list){
			if(!Objects.equals(form.getBrand(),"All") && !Objects.equals(form.getBrand(),p.getBrand()))continue;
			if(!Objects.equals(form.getCategory(),"All") && !Objects.equals(form.getCategory(),p.getCategory()))continue;
			BrandReportData brand = new BrandReportData();
			brand.setBrand(p.getBrand());
			brand.setCategory(p.getCategory());
			data.add(brand);

		}
		logger.info("Created Brand Report at "+(Instant.now()));
		return data;
	}
	public List<InventoryReportData> getInventoryReport(InventoryReportForm form) throws ApiException {
		List<InventoryPojo> list = inventoryService.getAll();
		HashMap<Integer,Integer> inventory = new HashMap<>();
		for(InventoryPojo p : list) {
			ProductPojo product = productService.get(p.getId());
			int quant = 0;
			int brandId = product.getBrandId();

			if(inventory.get(brandId)!= null)quant = inventory.get(brandId);
			inventory.put(brandId , quant + p.getQuantity());
		}

		List<InventoryReportData> Data = new ArrayList<>();
		for(Integer id : inventory.keySet()) {
			BrandPojo brand = brandService.get(id);
			if(!Objects.equals(form.getBrand(),"All") && !Objects.equals(form.getBrand(),brand.getBrand()))continue;
			if(!Objects.equals(form.getCategory(),"All") && !Objects.equals(form.getCategory(),brand.getCategory()))continue;
			InventoryReportData data = new InventoryReportData();
			data.setBrand(brand.getBrand());
			data.setCategory(brand.getCategory());
			data.setQuantity(inventory.get(id));
			Data.add(data);
		}

		logger.info("Created Inventory Report at "+(Instant.now()));
		return Data;
	}
	
	public List<SalesReportData> getSalesReport(SalesReportForm form) throws ApiException {
		Instant range = Instant.now().minus(Period.ofDays(365));
		Instant startDate = form.getStartDate();
		Instant endDate = form.getEndDate();

		int comparedValue = startDate.compareTo(endDate);
		if(comparedValue>0){throw new ApiException("Invalid Time Period");}

		comparedValue = range.compareTo(startDate);
		if(comparedValue>0){throw new ApiException("Only one year is allowed for the reports");}

		comparedValue = Instant.now().compareTo(endDate);
		if(comparedValue<-1){throw new ApiException("Invalid Time Period");}


		// querying the time range
		List<OrderPojo> list = orderService.selectRange(startDate, endDate);

		//creating the list of order items based on orderId
		List<OrderItemPojo> orders = new ArrayList<>();
		for(OrderPojo p : list) {
			List<OrderItemPojo> items = orderItemService.getOrderItemByOrderId(p.getId());
			orders.addAll(items);
		}

		//generate product revenues
		HashMap<Integer,Integer> quantityProducts = (HashMap<Integer, Integer>)orders.stream().collect(Collectors.toMap(OrderItemPojo::getProductId,OrderItemPojo::getQuantity,(s, a)->s+a));
		HashMap<Integer,Double> revenueProducts = MapUtil.getProductsRevenue(orders);
		List<ProductPojo> products = new ArrayList<>();
		for(int id : revenueProducts.keySet()) {
			products.add(productService.get(id));
		}

		//generate brand revenues
		HashMap<Integer,Double> revenueBrands = MapUtil.getBrandsRevenue(revenueProducts,products);
		HashMap<Integer,Integer> quantityBrands = MapUtil.getBrandsQuantity(quantityProducts,products);

		//generate sales report
		List<SalesReportData> data = new ArrayList<>();
		for(int id : revenueBrands.keySet()) {
			SalesReportData item = new SalesReportData();
			BrandPojo p = brandService.get(id);
			item.setBrand(p.getBrand());
			item.setCategory(p.getCategory());
			item.setRevenue(revenueBrands.get(id));
			item.setQuantity(quantityBrands.get(id));
			data.add(item);
		}

		logger.info("Created Inventory Report at "+(Instant.now()));
		return data;
	}
	
}
