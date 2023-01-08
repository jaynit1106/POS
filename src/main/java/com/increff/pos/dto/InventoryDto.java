package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;

@Component
public class InventoryDto {

	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private ProductService productService;
	
	public void add(@RequestBody InventoryForm form) throws ApiException {
		InventoryPojo p = convert(form);
		BrandPojo brand=brandService.getBrandId(form.getBrand(), form.getCategory());
		if(brand == null)throw new ApiException("Brand and Category Does not exist");
		
		ProductPojo product = productService.productExist(brand.getId(), form.getName());
		if(product == null)throw new ApiException("Product Does Not Exist");
		p.setId(product.getId());
		
		inventoryService.get(p.getId());
		inventoryService.add(p);
	}
	
	public InventoryData get(int id) throws ApiException {
		return convert(inventoryService.get(id));
	}
	
	public List<InventoryData> getAll() {
		List<InventoryPojo> list = inventoryService.getAll();
		List<InventoryData> list2 = new ArrayList<InventoryData>();
		for (InventoryPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}
	
	public void update(int id,InventoryForm form) throws ApiException {
		InventoryPojo p = convert(form);
		inventoryService.update(id, p);
	}
	
	private static InventoryData convert(InventoryPojo p) {
		InventoryData d = new InventoryData();
		d.setId(p.getId());
		d.setQuantity(p.getQuantity());
		return d;
	}

	private static InventoryPojo convert(InventoryForm f) {
		InventoryPojo p = new InventoryPojo();
		p.setQuantity(f.getQuantity());
		return p;
	}
}
