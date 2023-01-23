package com.increff.pos.dto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportForm;

@Component
public class ReportsDto {

	@Autowired
	private final BrandService brandService = new BrandService();
	@Autowired
	private final ProductService productService = new ProductService();
	@Autowired
	private final InventoryService inventoryService = new InventoryService();
	@Autowired
	private final OrderService orderService = new OrderService();
	@Autowired
	private final OrderItemService orderItemService = new OrderItemService();
	
	public List<InventoryReportData> getInventoryReport() throws ApiException {
		List<InventoryPojo> list = inventoryService.getAll();
		HashMap<Integer,Integer> inventory = new HashMap<>();
		for(InventoryPojo p : list) {
			ProductPojo product = productService.get(p.getId());
			int quant = 0;
			int brandId = product.getBrandId();

			if(inventory.get(brandId)!= null)quant = inventory.get(brandId);
			inventory.put(brandId , quant + p.getQuantity());
		}

		List<InventoryReportData> list2 = new ArrayList<>();
		for(Integer id : inventory.keySet()) {
			BrandPojo brand = brandService.get(id);
			InventoryReportData data = new InventoryReportData();
			data.setBrand(brand.getBrand());
			data.setCategory(brand.getCategory());
			data.setQuantity(inventory.get(id));
			list2.add(data);
		}

		return list2;
	}
	
	public List<SalesReportData> getSalesReport(SalesReportForm form) throws ApiException {
		Instant startDate = form.getStartDate();
		Instant endDate = form.getEndDate();

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
		return data;
	}
	
}
