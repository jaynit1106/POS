package com.increff.pos.dto;

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
		BrandPojo brand = brandService.getBrandId(form.getBrand(), form.getCategory());
		if(brand == null)throw new ApiException("Brand and Category does not exist");
		
		p.setbrandId(brand.getId());
		p.setBarcode(productService.generateBarcode(p));
		
		if(productService.productExist(p.getbrandId(), p.getName())!=null)throw new ApiException("Product already exists");
		productService.add(p);
	}
	
	public ProductData get(int id) throws ApiException {
		return convert(productService.get(id));
	}
	
	public List<ProductData> getAll() {
		List<ProductPojo> list = productService.getAll();
		List<ProductData> list2 = new ArrayList<ProductData>();
		for (ProductPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}
	
	public void update(int id,ProductForm form) throws ApiException {
		ProductPojo p = convert(form);
		
		BrandPojo brand = brandService.getBrandId(form.getBrand(),form.getCategory());
		if(brand == null)throw new ApiException("Brand and Category does not exist");
		p.setbrandId(brand.getId());
		
		if(productService.productExist(p.getbrandId(), p.getName())!=null)throw new ApiException("Product already exists");
		productService.update(id,p);
	}
	
	private static ProductData convert(ProductPojo p) {
		ProductData d = new ProductData();
		d.setName(p.getName());
		d.setId(p.getId());
		d.setBarcode(p.getBarcode());
		d.setMrp(p.getMrp());
		return d;
	}

	private static ProductPojo convert(ProductForm f) {
		ProductPojo p = new ProductPojo();
		p.setName(f.getName());
		p.setMrp(f.getMrp());
		return p;
	}
	
}
