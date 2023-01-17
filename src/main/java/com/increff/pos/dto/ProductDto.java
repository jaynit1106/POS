package com.increff.pos.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;

@Component
public class ProductDto {
	@Autowired
	private final ProductService productService = new ProductService();
	
	@Autowired
	private final BrandService brandService = new BrandService();
	
	public void add(ProductForm form) throws ApiException {
		ProductPojo p = ConvertUtil.objectMapper(form, ProductPojo.class);
		p.setMrp(BigDecimal.valueOf(form.getMrp()).setScale(2,RoundingMode.HALF_UP).doubleValue());
		
		if(form.getBarcode().length()!=8)throw new ApiException("Barcode Should be of 8 characters");
		if(form.getMrp()<0)throw new ApiException("MRP should be positive");
		
		BrandPojo brand = brandService.getBrandId(form.getBrand(), form.getCategory());
		if(Objects.isNull(brand))throw new ApiException("Brand and Category does not exist");
		
		p.setBrandId(brand.getId());
		if(!Objects.isNull(productService.productExist(p.getBrandId(), p.getName())))throw new ApiException("Product already exists");
		if(!Objects.isNull(productService.getProductByBarcode(form.getBarcode())))throw new ApiException("Barcode already exists");
		
		
		productService.add(p);
	}
	
	public ProductData get(int id) throws ApiException {
		ProductPojo product = productService.get(id);
		ProductData data = ConvertUtil.objectMapper(product, ProductData.class);
		data.setMrp(StringUtil.convertMrp(product.getMrp()));
		
		BrandPojo brand = brandService.get(product.getBrandId());
		data.setCategory(brand.getCategory());
		data.setBrand(brand.getBrand());
		return data;
	}
	
	public List<ProductData> getAll() throws ApiException{
		List<ProductPojo> list = productService.getAll();
		List<ProductData> list2 = new ArrayList<>();
		for (ProductPojo p : list) {
			
			ProductData data = ConvertUtil.objectMapper(p, ProductData.class);
			data.setMrp(StringUtil.convertMrp(p.getMrp()));
			
			BrandPojo brand = brandService.get(p.getBrandId());
			data.setCategory(brand.getCategory());
			data.setBrand(brand.getBrand());
			list2.add(data);
		}
		return list2;
	}
	
	public void update(int id,ProductForm form) throws ApiException {
		ProductPojo p = ConvertUtil.objectMapper(form, ProductPojo.class);
		p.setMrp(BigDecimal.valueOf(form.getMrp()).setScale(2,RoundingMode.HALF_UP).doubleValue());
		
		if(form.getBarcode().length()!=8)throw new ApiException("Barcode Should be of 8 characters");
		if(form.getMrp()<0)throw new ApiException("MRP should be positive");
		
		BrandPojo brand = brandService.getBrandId(form.getBrand(),form.getCategory());
		if(Objects.isNull(brand))throw new ApiException("Brand and Category does not exist");
		p.setBrandId(brand.getId());

		ProductPojo prod = productService.productExist(p.getBrandId(), p.getName());
		if(!Objects.isNull(prod)){
			if(!Objects.equals(prod.getId(),id))throw new ApiException("Product already exists");
		}

		ProductPojo product = productService.getProductByBarcode(form.getBarcode());
		if(!Objects.isNull(product)) {
			if(!Objects.equals(product.getId(),id))throw new ApiException("Barcode already exists");
		}
		
		productService.update(id,p);
	}
}
