package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.BarcodeUtil;
import com.increff.pos.util.StringUtil;


@Service
public class ProductService {

	@Autowired
	private ProductDao dao;

	@Autowired
	private BrandDao brandDao;
	
	@Autowired
	private InventoryDao inventoryDao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(ProductPojo p) throws ApiException {
		normalize(p);
		if(StringUtil.isEmpty(p.getName())) {
			throw new ApiException("Name cannot be empty");
		}
		dao.insert(p);
	}

	@Transactional
	public void delete(int id) {
		List<ProductPojo> products = dao.selectAll();
		for(ProductPojo p : products) {
			inventoryDao.delete(p.getId());
		}
		dao.delete(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public ProductPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<ProductPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, ProductPojo p) throws ApiException {
		normalize(p);
		ProductPojo ex = getCheck(id);
		ex.setbrandId(p.getbrandId());
		ex.setMrp(p.getMrp());
		ex.setName(p.getName());
		dao.update(ex);
	}

	@Transactional
	public ProductPojo getCheck(int id) throws ApiException {
		ProductPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Product with given ID does not exit, id: " + id);
		}
		return p;
	}
	@Transactional
	public boolean productExist(int brandId , String name) throws ApiException{
		if(dao.productExist(brandId, name)==null)return false;
		throw new ApiException("Product already exist");
	}
	
	@Transactional
	public int getBrandId(String brand,String category) throws ApiException {
		BrandPojo p = brandDao.checkRepeat(brand, category);
		if(p==null)throw new ApiException("The brand and category does not exist");
		return p.getId();
	}
	
	@Transactional
	public String generateBarcode(ProductPojo p) {
		while(true) {
			if(dao.barcodeExist(BarcodeUtil.generateBarcode())!=null)continue;
			return BarcodeUtil.generateBarcode();
		}
	}
	
	protected static void normalize(ProductPojo p) {
		p. setName(StringUtil.toLowerCase(p. getName()));
	}
	
	
}
