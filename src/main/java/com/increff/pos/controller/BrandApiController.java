package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandApiController {
	
	@Autowired
	private final BrandDto dto = new BrandDto();

	@ApiOperation(value = "Adds an Brand")
	@RequestMapping(path = "/api/brand", method = RequestMethod.POST)
	public void add(@RequestBody BrandForm form) throws ApiException {
		dto.add(form);
	}

	@ApiOperation(value = "Gets an brand by ID")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
	public BrandData get(@PathVariable int id) throws ApiException {
		return dto.get(id);
	}

	@ApiOperation(value = "gets all unique brand list")
	@GetMapping(path = "/api/brand/unique")
	public List<String> getBrandList(){
		return dto.getBrandList();
	}

	@ApiOperation(value = "gets all unique category list")
	@PostMapping(path = "/api/brand/unique")
	public List<String> getCategoryList(@RequestBody BrandForm f){
		return dto.getCategoryList(f.getBrand());
	}

	@ApiOperation(value = "Gets list of all Brands")
	@RequestMapping(path = "/api/brand", method = RequestMethod.GET)
	public List<BrandData> getAll() {
		return dto.getAll();
	}

	@ApiOperation(value = "Updates an Brand")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandForm f) throws ApiException {
		dto.update(id, f);
	}
}
