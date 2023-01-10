package com.increff.pos.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;

@Component
public class ProductDto {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;
	
	public void add(ProductForm form) throws ApiException {
		ProductPojo p = convert(form);
		
		if(form.getBarcode().length()!=8)throw new ApiException("Barcode Should be of 8 characters");
		if(form.getMrp()<0)throw new ApiException("MRP should be positive");
		
		BrandPojo brand = brandService.getBrandId(form.getBrand(), form.getCategory());
		if(brand == null)throw new ApiException("Brand and Category does not exist");
		
		p.setbrandId(brand.getId());
		if(productService.productExist(p.getbrandId(), p.getName())!=null)throw new ApiException("Product already exists");
		if(productService.getProductByBarcode(form.getBarcode())!=null)throw new ApiException("Barcode already exists");
		
		
		productService.add(p);
	}
	
	public ProductData get(int id) throws ApiException {
		ProductPojo product = productService.get(id);
		ProductData data = convert(product);
		BrandPojo brand = brandService.get(product.getbrandId());
		data.setCategory(brand.getCategory());
		data.setBrand(brand.getBrand());
		return data;
	}
	
	public List<ProductData> getAll() throws ApiException{
		List<ProductPojo> list = productService.getAll();
		List<ProductData> list2 = new ArrayList<ProductData>();
		for (ProductPojo p : list) {
			ProductData data = convert(p);
			BrandPojo brand = brandService.get(p.getbrandId());
			data.setCategory(brand.getCategory());
			data.setBrand(brand.getBrand());
			list2.add(data);
		}
		return list2;
	}
	
	public void update(int id,ProductForm form) throws ApiException {
		ProductPojo p = convert(form);
		if(form.getBarcode().length()!=8)throw new ApiException("Barcode Should be of 8 characters");
		if(form.getMrp()<0)throw new ApiException("MRP should be positive");
		BrandPojo brand = brandService.getBrandId(form.getBrand(),form.getCategory());
		if(brand == null)throw new ApiException("Brand and Category does not exist");
		p.setbrandId(brand.getId());
		
		if(productService.productExist(p.getbrandId(), p.getName())!=null)throw new ApiException("Product already exists");
		ProductPojo product = productService.getProductByBarcode(form.getBarcode());
		if(product!=null) {
			if(product.getId()!=id)throw new ApiException("Barcode already exists");
		}
		
		productService.update(id,p);
	}
	
	private static ProductData convert(ProductPojo p) {
		ProductData d = new ProductData();
		d.setName(p.getName());
		d.setBarcode(p.getBarcode());
		d.setMrp(p.getMrp());
		d.setId(p.getId());
		return d;
	}

	private static ProductPojo convert(ProductForm f) {
		ProductPojo p = new ProductPojo();
		p.setName(f.getName());
		p.setMrp(BigDecimal.valueOf(f.getMrp()).setScale(3,RoundingMode.HALF_UP).doubleValue());
		p.setBarcode(f.getBarcode());
		return p;
	}
	
}
