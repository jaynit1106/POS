package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryApiController {

	@Autowired
	private InventoryDto inventoryDto;

	@ApiOperation(value = "Adds a Product Quantity")
	@RequestMapping(path = "/api/inventorys", method = RequestMethod.POST)
	public void addInventory(@RequestBody InventoryForm inventoryForm) throws ApiException {
		inventoryDto.addInventory(inventoryForm);
	}


	@ApiOperation(value = "Gets list of all products Quantity")
	@RequestMapping(path = "/api/inventorys", method = RequestMethod.GET)
	public List<InventoryData> getAllInventorys() throws ApiException {
		return inventoryDto.getAllInventorys();
	}

	@ApiOperation(value = "Updates a product Quantity")
	@RequestMapping(path = "/api/inventorys/{inventoryId}", method = RequestMethod.PUT)
	public void update(@PathVariable int inventoryId, @RequestBody InventoryForm inventoryForm) throws ApiException {
		inventoryDto.updateInventory(inventoryId, inventoryForm);
	}
}
