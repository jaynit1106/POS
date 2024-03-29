package com.increff.pos.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import com.increff.pos.helper.ProductHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StringUtil;


@Service
@Transactional(rollbackOn  = ApiException.class)
public class ProductService {

	@Autowired
	private  ProductDao productDao;

	@Autowired
	private ProductHelper productHelper;

	public void add(ProductPojo productPojo) throws ApiException {
		productHelper.validateProductPojo(productPojo);
		productDao.insert(productPojo);
	}

	public ProductPojo getProductById(int productId) throws ApiException {
		ProductPojo productPojo = productDao.select(productId,ProductPojo.class);
		if (Objects.isNull(productPojo)) {
			throw new ApiException("Product with given ID does not exit, id: " + productId);
		}
		return productPojo;
	}

	public List<ProductPojo> getAllProducts() {
		return productDao.selectAll(ProductPojo.class);
	}

	public void updateProduct(int productId, ProductPojo productPojo) throws ApiException {
		if(Objects.nonNull(productDao.getProductByNameAndBrandId(productPojo.getBrandId(),productPojo.getName()))) {
			if(productDao.getProductByNameAndBrandId(productPojo.getBrandId(),productPojo.getName()).getId()!=productId) {
				throw new ApiException("Product already Exists");
			}
		}

		if(productPojo.getMrp()<=0) {
			throw new ApiException("MRP should be positive");
		}
		ProductPojo ex = getProductById(productId);
		ex.setMrp(productPojo.getMrp());
		ex.setName(productPojo.getName());
	}



	public ProductPojo getProductByNameAndBrandId(int brandId , String productName){
		return productDao.getProductByNameAndBrandId(brandId, productName);
	}
	
	public ProductPojo getProductByBarcode(String barcode)throws ApiException{
		ProductPojo productPojo = productDao.barcodeExist(barcode);
		if(Objects.isNull(productPojo)) {
			throw new ApiException("Product Does Not Exists");
		}

		return productPojo;
	}

	public List<String> getBarcodeList(){
		List<String> barcodeList = productDao.getBarcodeList();
		Collections.sort(barcodeList);
		Collections.reverse(barcodeList);
		return barcodeList;
	}
}
