package com.increff.pos.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
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
	private  ProductService productService ;
	
	@Autowired
	private  BrandService brandService ;
	
	public void addProduct(ProductForm productForm) throws ApiException {
		StringUtil.normalise(productForm,ProductForm.class);
		ProductPojo productPojo = ConvertUtil.objectMapper(productForm, ProductPojo.class);

		productPojo.setMrp(BigDecimal.valueOf(productForm.getMrp()).setScale(2,RoundingMode.HALF_UP).doubleValue());
		BrandPojo brand = brandService.getBrandByNameAndCategory(productForm.getBrand(), productForm.getCategory());
		productPojo.setBrandId(brand.getId());

		productService.add(productPojo);
	}

	public List<ProductData> getAllProducts() throws ApiException{
		List<ProductPojo> productPojoList = productService.getAllProducts();
		List<ProductData> productDataList = new ArrayList<>();
		for (ProductPojo productPojo : productPojoList) {
			
			ProductData productData = ConvertUtil.objectMapper(productPojo, ProductData.class);
			productData.setMrp(StringUtil.convertMrp(productPojo.getMrp()));
			
			BrandPojo brand = brandService.getBrandById(productPojo.getBrandId());
			productData.setCategory(brand.getCategory());
			productData.setBrand(brand.getBrand());

			productDataList.add(productData);
		}
		return productDataList;
	}
	
	public void updateProduct(int productId,ProductForm productForm) throws ApiException {
		StringUtil.normalise(productForm,ProductForm.class);
		ProductPojo productPojo = ConvertUtil.objectMapper(productForm, ProductPojo.class);
		productPojo.setMrp(BigDecimal.valueOf(productForm.getMrp()).setScale(2,RoundingMode.HALF_UP).doubleValue());

		BrandPojo brandPojo = brandService.getBrandByNameAndCategory(productForm.getBrand(),productForm.getCategory());
		productPojo.setBrandId(brandPojo.getId());
		
		productService.updateProduct(productId,productPojo);
	}

	public List<String> getBarcodeList(){return productService.getBarcodeList();}
}
