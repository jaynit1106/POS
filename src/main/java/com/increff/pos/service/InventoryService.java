package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.TimestampUtil;


@Service
public class InventoryService {

	@Autowired
	private InventoryDao dao;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@Transactional(rollbackOn = ApiException.class)
	public void add(InventoryPojo p) throws ApiException {
		dao.insert(p);
	}

	@Transactional(rollbackOn = ApiException.class)
	public InventoryPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<InventoryPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, InventoryPojo p) throws ApiException {
		InventoryPojo ex = getCheck(id);
		ex.setQuantity(p.getQuantity());
		dao.update(ex);
	}

	@Transactional
	public InventoryPojo getCheck(int id) throws ApiException {
		InventoryPojo p = dao.select(id);
		return p;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public void checkAndCreateOrder(List<OrderItemPojo> items) throws ApiException {
		for(OrderItemPojo p : items) {
			InventoryPojo inventory = dao.select(p.getProductId());
			int quantity = inventory.getQuantity();
			if(quantity<p.getQuantity())throw new ApiException("Only "+quantity+" pieces left of " + productService.get(p.getProductId()).getBarcode());
			quantity=quantity-p.getQuantity();
			inventory.setQuantity(quantity);
			update(inventory.getId(), inventory);
		}
		
		OrderPojo p = new OrderPojo();
		p.setTimestamp(TimestampUtil.getTimestamp());
		orderService.add(p);
		
		for(int i=0;i<items.size();i++) {
			items.get(i).setOrderId(p.getId());
		}
		orderItemService.add(items);
	}
	
	
	
	
}
