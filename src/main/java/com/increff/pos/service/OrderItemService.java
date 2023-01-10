package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;


@Service
public class OrderItemService {

	@Autowired
	private OrderItemDao dao;
	
	@Autowired
	private InventoryDao inventoryDao;
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private ProductService productService;
	
	@Transactional(rollbackOn = ApiException.class)
	public void checkQuantity(List<OrderItemPojo> items) throws ApiException {
		for(OrderItemPojo p : items) {
			InventoryPojo inventory = inventoryDao.select(p.getProductId());
			int quantity = inventory.getQuantity();
			if(quantity<p.getQuantity())throw new ApiException("Only "+quantity+" pieces left of " + productService.get(p.getProductId()).getBarcode());
			quantity=quantity-p.getQuantity();
			inventory.setQuantity(quantity);
			inventoryService.update(inventory.getId(), inventory);
		}
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(List<OrderItemPojo> items) throws ApiException {
		for(OrderItemPojo p : items) {
			
			dao.insert(p);
		}
		
	}

	@Transactional(rollbackOn = ApiException.class)
	public OrderItemPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<OrderItemPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, OrderItemPojo p) throws ApiException {
		OrderItemPojo ex = getCheck(id);
		ex.setQuantity(p.getQuantity());
		ex.setSellingPrice(p.getSellingPrice());
		ex.setProductId(p.getProductId());
		dao.update(ex);
	}

	@Transactional
	public OrderItemPojo getCheck(int id) throws ApiException {
		OrderItemPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("OrderItem with given ID does not exit, id: " + id);
		}
		return p;
	}
	
	@Transactional
	public List<OrderItemPojo> getOrderItemByOrderId(int id) throws ApiException {
		List<OrderItemPojo> p = dao.selectByOrderId(id);
		return p;
	}
	
}
