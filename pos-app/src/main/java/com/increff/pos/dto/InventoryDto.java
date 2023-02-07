package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;

@Component
public class InventoryDto {

	@Autowired
	private InventoryService inventoryService;

	@Autowired
	private ProductService productService;
	
	public void addInventory(@RequestBody InventoryForm inventoryForm) throws ApiException {
		InventoryPojo inventoryPojo = ConvertUtil.objectMapper(inventoryForm, InventoryPojo.class);

		ProductPojo productPojo = productService.getProductByBarcode(inventoryForm.getBarcode());
		inventoryPojo.setId(productPojo.getId());

		inventoryService.addInventory(inventoryPojo);
	}

	
	public List<InventoryData> getAllInventorys() throws ApiException {
		List<InventoryPojo> inventoryPojoList = inventoryService.getAllInventorys();
		List<InventoryData> inventoryDataList = new ArrayList<>();
		for (InventoryPojo inventoryPojo : inventoryPojoList) {
			InventoryData inventoryData =  ConvertUtil.objectMapper(inventoryPojo,InventoryData.class);

			ProductPojo productPojo = productService.getProductById(inventoryPojo.getId());
			inventoryData.setBarcode(productPojo.getBarcode());
			inventoryData.setName(productPojo.getName());

			inventoryDataList.add(inventoryData);
		}
		return inventoryDataList;
	}
	
	public void updateInventory(int inventoryId,InventoryForm inventoryForm) throws ApiException {
		InventoryPojo inventoryPojo = ConvertUtil.objectMapper(inventoryForm, InventoryPojo.class);
		inventoryService.updateInventory(inventoryId, inventoryPojo);
	}
}
