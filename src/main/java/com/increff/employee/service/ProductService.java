package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.BarcodeUtil;
import com.increff.employee.util.StringUtil;


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

	@Transactional
	public void delete(int id) {
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
	public int getBrandId(String brand,String category) throws ApiException {
		List<BrandPojo> p = dao.getBrandId(brand, category);
		if(p.isEmpty())throw new ApiException("The brand and category does not exist");
		return p.get(0).getId();
	}
	
	@Transactional
	public String generateBarcode(ProductPojo p) {
		while(true) {
			if(dao.barcodeExist(BarcodeUtil.generateBarcode()))continue;
			return BarcodeUtil.generateBarcode();
		}
	}
	
	protected static void normalize(ProductPojo p) {
		p. setName(StringUtil.toLowerCase(p. getName()));
	}
	
	
}
