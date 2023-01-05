package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryApiController {

	@Autowired
	private InventoryService service;

	@ApiOperation(value = "Adds a Product Quantity")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm form) throws ApiException {
		InventoryPojo p = convert(form);
		int brandId=service.getBrandId(form.getBrand(), form.getCategory());
		p.setId(service.getProductId(brandId, form.getName()));
		service.inventoryExist(p.getId());
		service.add(p);
	}

	
	@ApiOperation(value = "Deletes a Product Quantity")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets a Product Quantity by ID")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable int id) throws ApiException {
		InventoryPojo p = service.get(id);
		return convert(p);
	}

	@ApiOperation(value = "Gets list of all products Quantity")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() {
		List<InventoryPojo> list = service.getAll();
		List<InventoryData> list2 = new ArrayList<InventoryData>();
		for (InventoryPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates a product Quantity")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody InventoryForm f) throws ApiException {
		InventoryPojo p = convert(f);
		service.update(id, p);
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
