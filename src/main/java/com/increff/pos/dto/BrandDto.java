package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ConvertUtil;


@Component
public class BrandDto {
	
	@Autowired
	BrandService brandService;
	
	public void add(BrandForm form) throws ApiException{
		BrandPojo p = ConvertUtil.objectMapper(form, BrandPojo.class);
		BrandPojo brand = brandService.getBrandId(p.getBrand(), p.getCategory());
		if(StringUtil.isEmpty(form.getBrand())) {
			throw new ApiException("Brand cannot be empty");
		}
		if(StringUtil.isEmpty(form.getCategory())) {
			throw new ApiException("Category cannot be empty");
		}
		if(Objects.isNull(brand))brandService.add(p);
		else throw new ApiException("Brand and Category already exists");
	}
	
	public BrandData get(int id) throws ApiException {
		BrandPojo p = brandService.get(id);
		return ConvertUtil.objectMapper(p, BrandData.class);
	}
	
	public List<BrandData> getAll() {
		List<BrandPojo> list = brandService.getAll();
		List<BrandData> list2 = new ArrayList<>();
		for (BrandPojo p : list) {
			list2.add(ConvertUtil.objectMapper(p, BrandData.class));
		}
		return list2;
	}
	
	public void update(int id, BrandForm f) throws ApiException {

		BrandPojo p = ConvertUtil.objectMapper(f, BrandPojo.class);
		BrandPojo brand = brandService.getBrandId(p.getBrand(), p.getCategory());

		if(Objects.isNull(brand))brandService.update(id, p);
		else throw new ApiException("Brand and Category already exists");
	}
}
