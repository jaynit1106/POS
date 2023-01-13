package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.BarcodeUtil;
import com.increff.pos.util.StringUtil;


@Service
public class ProductService {

	@Autowired
	private ProductDao dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(ProductPojo p) throws ApiException {
		normalize(p);
		if(StringUtil.isEmpty(p.getName())) {
			throw new ApiException("Name cannot be empty");
		}
		dao.insert(p);
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
		ex.setBrandId(p.getBrandId());
		ex.setMrp(p.getMrp());
		ex.setName(p.getName());
		ex.setBarcode(p.getBarcode());
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
	public ProductPojo productExist(int brandId , String name) throws ApiException{
		return dao.productExist(brandId, name);
	}
	
	@Transactional
	public ProductPojo getProductByBarcode(String barcode) throws ApiException {
		ProductPojo p = dao.barcodeExist(barcode);
		return p;
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
