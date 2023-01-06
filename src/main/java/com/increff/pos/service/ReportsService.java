package com.increff.pos.service;
import java.util.*;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;


@Service
public class ReportsService {

	@Autowired
	private BrandDao brandDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private InventoryDao inventoryDao;
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private OrderItemDao orderItemDao;


	@Transactional
	public HashMap<Integer,Integer> getInventoryReport() throws ApiException {
		List<InventoryPojo> list = inventoryDao.selectAll();
		HashMap<Integer,Integer> inventory = new HashMap<Integer,Integer>();
		for(InventoryPojo p : list) {
			ProductPojo product = productDao.select(p.getId());
			int quant = 0;
			int brandId = product.getbrandId();
			
			if(inventory.get(brandId)!= null)quant = inventory.get(brandId);
			inventory.put(brandId , quant + p.getQuantity());
		}
		return inventory;
	}
	
	@Transactional
	public List<SalesReportData> getSalesReport(String startDate , String endDate) throws ApiException {
		// querying the time range
		List<OrderPojo> list = orderDao.selectRange(startDate, endDate);
		
		//creating the list of order items based on orderId
		List<OrderItemPojo> orders = new ArrayList<>();
		for(OrderPojo p : list) {
			List<OrderItemPojo> items = orderItemDao.selectByOrderId(p.getId());
			for(OrderItemPojo item : items) {
				orders.add(item);
			}
		}
		
		//generate product revenues
		HashMap<Integer,Double> revenueProducts = getProductsRevenue(orders);
		List<ProductPojo> products = new ArrayList<>();
		for(int id : revenueProducts.keySet()) {
			products.add(productDao.select(id));
		}
		
		//generate brand revenues
		HashMap<Integer,Double> revenueBrands = getBrandsRevenue(revenueProducts,products);
		
		//generate sales report
		List<SalesReportData> data = new ArrayList<>();
		for(int id : revenueBrands.keySet()) {
			SalesReportData item = new SalesReportData();
			BrandPojo p = brandDao.select(id);
			item.setBrand(p.getBrand());
			item.setCategory(p.getCategory());
			item.setRevenue(revenueBrands.get(id));
			data.add(item);
		}
		return data;
	}

	private HashMap<Integer,Double> getBrandsRevenue(HashMap<Integer,Double> revenueProducts,List<ProductPojo> products){
		
		HashMap<Integer,Double> revenueBrands = new HashMap<Integer,Double>();
		for(ProductPojo product : products) {
			double price = 0;
			int brandId = product.getbrandId();
			int productId = product.getId();
			if(revenueBrands.get(brandId)!=null)price = revenueBrands.get(brandId);
			revenueBrands.put(brandId, price + revenueProducts.get(productId));
		}
		
		return revenueBrands;
	}
	
	private HashMap<Integer,Double> getProductsRevenue(List<OrderItemPojo> orders){
		
		HashMap<Integer,Double> revenueProducts = new HashMap<Integer,Double>();
		for(OrderItemPojo item : orders) {
			double price = 0;
			int productId = item.getProductId();
			if(revenueProducts.get(productId)!=null)price = revenueProducts.get(productId);
			revenueProducts.put(productId, price + item.getQuantity()*item.getSellingPrice());
		}
		
		return revenueProducts;
	}
	
}
