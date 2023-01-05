package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;


@Service
public class InventoryService {

	@Autowired
	private InventoryDao dao;
	
	@Autowired
	private BrandDao brandDao;

	@Autowired
	private ProductDao productDao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(InventoryPojo p) throws ApiException {
		dao.insert(p);
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
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
		if (p == null) {
			throw new ApiException("Product with given ID does not exit, id: " + id);
		}
		return p;
	}
	
	@Transactional
	public boolean inventoryExist(int id) throws ApiException {
		InventoryPojo p = dao.select(id);
		if (p == null)return false;
		throw new ApiException("Product Quantity already Exist");
	}
	
	@Transactional
	public int getBrandId(String brand,String category) throws ApiException {
		BrandPojo p = brandDao.checkRepeat(brand, category);
		if(p==null)throw new ApiException("The brand and category does not exist");
		return p.getId();
	}
	
	@Transactional
	public int getProductId(int brandId,String name) throws ApiException {
		ProductPojo p = productDao.productExist(brandId, name);
		if(p==null)throw new ApiException("The product does not exist");
		return p.getId();
	}
	
	
	
	
}
