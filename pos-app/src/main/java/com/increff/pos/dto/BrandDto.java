package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ConvertUtil;


@Component
public class BrandDto {
	
	@Autowired
	BrandService brandService;
	
	public void addBrand(BrandForm brandForm) throws ApiException{
		StringUtil.normalise(brandForm,BrandForm.class);
		BrandPojo p = ConvertUtil.objectMapper(brandForm, BrandPojo.class);
		brandService.addBrand(p);
	}
	
	public BrandData getBrandById(int brandId) throws ApiException {
		BrandPojo brandPojo = brandService.getBrandById(brandId);
		return ConvertUtil.objectMapper(brandPojo, BrandData.class);
	}
	
	public List<BrandData> getAllBrands() {
		List<BrandPojo> brandList = brandService.getAllBrands();
		List<BrandData> brandDataList = new ArrayList<>();
		for (BrandPojo brandPojo : brandList) {
			brandDataList.add(ConvertUtil.objectMapper(brandPojo, BrandData.class));
		}
		return brandDataList;
	}
	
	public void updateBrand(int brandId, BrandForm brandForm) throws ApiException {
		StringUtil.normalise(brandForm,BrandForm.class);
		BrandPojo brandPojo = ConvertUtil.objectMapper(brandForm, BrandPojo.class);
		brandService.updateBrand(brandId,brandPojo);
	}

	public List<String> getBrandList(){
		return brandService.getBrandList();
	}

	public List<String> getCategoryList(String brand){
		return brandService.getCategoryList(brand);
	}
}
