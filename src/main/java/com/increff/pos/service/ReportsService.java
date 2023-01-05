package com.increff.pos.service;
import java.util.*;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;


@Service
public class ReportsService {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private InventoryDao inventoryDao;


	@Transactional
	public HashMap<Integer,Integer> getInventoryReport() throws ApiException {
		List<InventoryPojo> list = inventoryDao.selectAll();
		HashMap<Integer,Integer> inventory = new HashMap<Integer,Integer>();
		for(InventoryPojo p : list) {
			ProductPojo product = productDao.select(p.getId());
			int quant = 0;
			if(inventory.get(product.getbrandId())!= null)quant = inventory.get(product.getbrandId());
			inventory.put(product.getbrandId() , quant + p.getQuantity());
		}
		return inventory;
	}
	
	
}
