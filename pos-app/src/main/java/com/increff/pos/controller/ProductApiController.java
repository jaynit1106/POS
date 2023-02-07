package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductApiController {

	@Autowired
	private ProductDto dto;

	@ApiOperation(value = "Adds a Product")
	@RequestMapping(path = "/api/products", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm form) throws ApiException {
		dto.add(form);
	}

	@ApiOperation(value = "Gets a Product by ID")
	@RequestMapping(path = "/api/products/{id}", method = RequestMethod.GET)
	public ProductData get(@PathVariable int id) throws ApiException {
		return dto.get(id);
	}

	@ApiOperation(value = "Gets list of all products")
	@RequestMapping(path = "/api/products", method = RequestMethod.GET)
	public List<ProductData> getAll() throws ApiException {
		return dto.getAll();
	}

	@ApiOperation(value = "Updates a product")
	@RequestMapping(path = "/api/products/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm form) throws ApiException {
		dto.update(id, form);
	}

	@ApiOperation(value = "Get Barcode List")
	@GetMapping(path = "/api/products/barcode")
	public List<String> getBarcodeList(){
		return dto.getBarcodeList();
	}
	



}
