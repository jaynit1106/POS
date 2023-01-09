package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;


@Component
public class BrandDto {
	
	@Autowired
	BrandService brandService;
	
	public void add(BrandForm form) throws ApiException{
		BrandPojo p = convert(form);
		BrandPojo brand = brandService.getBrandId(p.getBrand(), p.getCategory());
		if(brand==null)brandService.add(p);
		else throw new ApiException("Brand and Category already exists");
	}
	
	public BrandData get(int id) throws ApiException {
		BrandPojo p = brandService.get(id);
		return convert(p);
	}
	
	public List<BrandData> getAll() {
		List<BrandPojo> list = brandService.getAll();
		List<BrandData> list2 = new ArrayList<BrandData>();
		for (BrandPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}
	
	public void update(int id, BrandForm f) throws ApiException {
		BrandPojo p = convert(f);
		BrandPojo brand = brandService.getBrandId(p.getBrand(), p.getCategory());
		if(brand==null)brandService.update(id, p);
		else throw new ApiException("Brand and Category already exists");
	}
	
	private static BrandData convert(BrandPojo p) {
		BrandData d = new BrandData();
		d.setCategory(p.getCategory());
		d.setBrand(p.getBrand());
		d.setId(p.getId());
		return d;
	}

	private static BrandPojo convert(BrandForm f) {
		BrandPojo p = new BrandPojo();
		p.setBrand(f.getBrand());
		p.setCategory(f.getCategory());
		return p;
	}

}
