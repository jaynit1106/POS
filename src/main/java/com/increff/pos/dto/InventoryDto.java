package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;

@Component
public class InventoryDto {

	@Autowired
	private final InventoryService inventoryService = new InventoryService();
	
	
	@Autowired
	private final ProductService productService = new ProductService();
	
	public void add(@RequestBody InventoryForm form) throws ApiException {
		if(form.getQuantity()<0)throw new ApiException("Quantity cannot be Negative");
		InventoryPojo p = ConvertUtil.objectMapper(form, InventoryPojo.class);

		ProductPojo product = productService.getProductByBarcode(form.getBarcode());
		if(Objects.isNull(product))throw new ApiException("Product Does Not Exist");
		p.setId(product.getId());
		
		InventoryPojo inventory = inventoryService.get(p.getId()); 
		if(!Objects.isNull(inventory)) {
			inventory.setQuantity(inventory.getQuantity()+form.getQuantity());
			inventoryService.update(inventory.getId(), inventory);
			return;
		}
		inventoryService.add(p);
	}
	
	public InventoryData get(int id) throws ApiException {
		InventoryPojo p = inventoryService.get(id);
		if(Objects.isNull(p))throw new ApiException("Inventory does not exist");
		InventoryData data =  ConvertUtil.objectMapper(p, InventoryData.class);
		ProductPojo product = productService.get(id);
		data.setBarcode(product.getBarcode());
		data.setName(product.getName());
		return  data;
	}
	
	public List<InventoryData> getAll() throws ApiException {
		List<InventoryPojo> list = inventoryService.getAll();
		List<InventoryData> list2 = new ArrayList<>();
		for (InventoryPojo p : list) {
			InventoryData data =  ConvertUtil.objectMapper(p,InventoryData.class);
			ProductPojo product = productService.get(p.getId());
			data.setBarcode(product.getBarcode());
			data.setName(product.getName());
			list2.add(data);
		}
		return list2;
	}
	
	public void update(int id,InventoryForm form) throws ApiException {
		if(form.getQuantity()<0)throw new ApiException("Quantity cannot be Negative");
		InventoryPojo inventory = ConvertUtil.objectMapper(form, InventoryPojo.class);
		inventoryService.update(id, inventory);
	}
}
