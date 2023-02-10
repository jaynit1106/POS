package com.increff.pos.dto;

import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


import com.increff.pos.helper.ReportHelper;
import com.increff.pos.model.data.BrandReportData;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.BrandReportForm;
import com.increff.pos.model.form.InventoryReportForm;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.util.MapUtil;
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


	private static Logger logger = Logger.getLogger(ReportsDto.class);


	public List<BrandReportData> getBrandReport(BrandReportForm brandReportForm){
		logger.info("creating brand report");
		List<BrandPojo> brandPojoList = brandService.getAllBrands();
		List<BrandReportData> brandReportDataList = new ArrayList<>();

		for(BrandPojo brandPojo : brandPojoList){

			if(!Objects.equals(brandReportForm.getBrand(),"All") &&
					!Objects.equals(brandReportForm.getBrand(), brandPojo.getBrand()))continue;

			if(!Objects.equals(brandReportForm.getCategory(),"All") &&
					!Objects.equals(brandReportForm.getCategory(), brandPojo.getCategory()))continue;

			BrandReportData brandReportData = new BrandReportData();
			brandReportData.setBrand(brandPojo.getBrand());
			brandReportData.setCategory(brandPojo.getCategory());

			brandReportDataList.add(brandReportData);

		}
		logger.info("Created Brand Report at "+(Instant.now()));
		return brandReportDataList;
	}
	public List<InventoryReportData> getInventoryReport(InventoryReportForm inventoryReportForm) throws ApiException {
		List<InventoryPojo> inventoryPojoList = inventoryService.getAllInventorys();

		//mapping brands with quantity
		HashMap<Integer,Integer> inventoryMap = getInventoryMap(inventoryPojoList);

		List<InventoryReportData> inventoryReportDataList = new ArrayList<>();
		for(Integer id : inventoryMap.keySet()) {
			BrandPojo brandPojo = brandService.getBrandById(id);
			//applying filters
			if(!Objects.equals(inventoryReportForm.getBrand(),"All") &&
					!Objects.equals(inventoryReportForm.getBrand(), brandPojo.getBrand()))continue;

			if(!Objects.equals(inventoryReportForm.getCategory(),"All") &&
					!Objects.equals(inventoryReportForm.getCategory(), brandPojo.getCategory()))continue;

			//creating the output
			InventoryReportData inventoryReportData = new InventoryReportData();
			inventoryReportData.setBrand(brandPojo.getBrand());
			inventoryReportData.setCategory(brandPojo.getCategory());
			inventoryReportData.setQuantity(inventoryMap.get(id));
			inventoryReportDataList.add(inventoryReportData);
		}

		logger.info("Created Inventory Report at "+(Instant.now()));
		return inventoryReportDataList;
	}
	
	public List<SalesReportData> getSalesReport(SalesReportForm salesReportForm) throws ApiException {
		ReportHelper.salesReportsCheck(salesReportForm);
		// querying the time range
		List<OrderPojo> orderPojoList = orderService
				.selectOrdersInRange(salesReportForm.getStartDate(), salesReportForm.getEndDate());
		//creating the list of order items based on orderId
		List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
		for(OrderPojo orderPojo : orderPojoList) {
			List<OrderItemPojo> items = orderService.getOrderItemByOrderId(orderPojo.getId());
			orderItemPojoList.addAll(items);
		}
		//generate product revenues
		HashMap<Integer,Integer> quantityProducts = (HashMap<Integer, Integer>) orderItemPojoList
				.stream()
				.collect(Collectors.toMap(OrderItemPojo::getProductId,OrderItemPojo::getQuantity,(s, a)->s+a));

		HashMap<Integer,Double> revenueProducts = MapUtil.getProductsRevenue(orderItemPojoList);
		List<ProductPojo> products = new ArrayList<>();
		for(int id : revenueProducts.keySet()) {
			products.add(productService.getProductById(id));
		}

		//generate brand revenues
		HashMap<Integer,Double> revenueBrands = MapUtil.getBrandsRevenue(revenueProducts,products);
		HashMap<Integer,Integer> quantityBrands = MapUtil.getBrandsQuantity(quantityProducts,products);

		//generate sales report
		List<SalesReportData> data = generateSalesReportData(revenueBrands,quantityBrands);
		logger.info("Created Sales Report at "+(Instant.now()));


		return data;
	}

	public List<SalesReportData> generateSalesReportData(HashMap<Integer,Double> revenueBrands,HashMap<Integer,Integer> quantityBrands) throws ApiException {
		List<SalesReportData> data = new ArrayList<>();
		for(int id : revenueBrands.keySet()) {
			SalesReportData item = new SalesReportData();
			BrandPojo p = brandService.getBrandById(id);
			item.setBrand(p.getBrand());
			item.setCategory(p.getCategory());
			item.setRevenue(revenueBrands.get(id));
			item.setQuantity(quantityBrands.get(id));
			data.add(item);
		}

		return data;
	}

	public  HashMap<Integer,Integer> getInventoryMap(List<InventoryPojo> inventoryPojoList) throws ApiException {
		HashMap<Integer,Integer> inventoryMap = new HashMap<>();

		//mapping brands with quantity
		for(InventoryPojo inventoryPojo : inventoryPojoList) {
			ProductPojo productPojo = productService.getProductById(inventoryPojo.getId());

			int quantity = 0;
			int brandId = productPojo.getBrandId();
			if(inventoryMap.get(brandId)!= null) quantity = inventoryMap.get(brandId);

			inventoryMap.put(brandId , quantity + inventoryPojo.getQuantity());
		}
		return inventoryMap;
	}
	
}
