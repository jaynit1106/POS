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
	
	public void add(BrandForm form) throws ApiException{
		StringUtil.normalise(form,BrandForm.class);
		BrandPojo p = ConvertUtil.objectMapper(form, BrandPojo.class);
		brandService.add(p);
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
		StringUtil.normalise(f,BrandForm.class);
		BrandPojo p = ConvertUtil.objectMapper(f, BrandPojo.class);
		brandService.update(id,p);
	}

	public List<String> getBrandList(){
		return brandService.getBrandList();
	}

	public List<String> getCategoryList(String brand){
		return brandService.getCategoryList(brand);
	}
}
