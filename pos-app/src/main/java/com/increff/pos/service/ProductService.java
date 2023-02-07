package com.increff.pos.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StringUtil;


@Service
@Transactional(rollbackOn  = ApiException.class)
public class ProductService {

	@Autowired
	private  ProductDao dao;

	public void add(ProductPojo p) throws ApiException {
		if(StringUtil.isEmpty(p.getName()))throw new ApiException("Name cannot be empty");
		if(p.getBarcode().length()!=8)throw new ApiException("Barcode Should be of 8 characters");
		if(p.getMrp()<0)throw new ApiException("MRP should be positive");
		if(Objects.nonNull(dao.getProductByNameAndBrandId(p.getBrandId(),p.getName())))throw new ApiException("Product already Exists");
		if(Objects.nonNull(dao.barcodeExist(p.getBarcode())))throw new ApiException("Barcode already Exists");

		dao.insert(p);
	}

	public ProductPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	public List<ProductPojo> getAll() {
		return dao.selectAll(ProductPojo.class);
	}

	public void update(int id, ProductPojo p) throws ApiException {
		if(p.getBarcode().length()!=8)throw new ApiException("Barcode Should be of 8 characters");
		if(p.getMrp()<0)throw new ApiException("MRP should be positive");

		ProductPojo prod = dao.getProductByNameAndBrandId(p.getBrandId(), p.getName());
		if(!Objects.isNull(prod)){
			if(!Objects.equals(prod.getId(),id))throw new ApiException("Product already exists");
		}

		ProductPojo product = dao.barcodeExist(p.getBarcode());
		if(!Objects.isNull(product)) {
			if(!Objects.equals(product.getId(),id))throw new ApiException("Barcode already exists");
		}

		ProductPojo ex = getCheck(id);
		ex.setMrp(p.getMrp());
		ex.setName(p.getName());
	}

	public ProductPojo getCheck(int id) throws ApiException {
		ProductPojo p = dao.select(id,ProductPojo.class);
		if (Objects.isNull(p)) {
			throw new ApiException("Product with given ID does not exit, id: " + id);
		}
		return p;
	}

	public ProductPojo getProductByNameAndBrandId(int brandId , String name){
		return dao.getProductByNameAndBrandId(brandId, name);
	}
	
	public ProductPojo getProductByBarcode(String barcode)throws ApiException{
		ProductPojo p = dao.barcodeExist(barcode);
		if(Objects.isNull(p))throw new ApiException("Product Does Not Exists");
		return p;
	}

	public List<String> getBarcodeList(){
		List<String> list = dao.getBarcodeList();
		Collections.sort(list);
		Collections.reverse(list);
		return list;
	}

	

	
}
