package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandApiController {
	
	@Autowired
	private BrandDto brandDto ;

	@ApiOperation(value = "Adds an Brand")
	@RequestMapping(path = "/api/brands", method = RequestMethod.POST)
	public void addBrand(@RequestBody BrandForm brandForm) throws ApiException {
		brandDto.addBrand(brandForm);
	}

	@ApiOperation(value = "Gets an brand by ID")
	@RequestMapping(path = "/api/brands/{brandId}", method = RequestMethod.GET)
	public BrandData getBrandById(@PathVariable int brandId) throws ApiException {
		return brandDto.getBrandById(brandId);
	}

	@ApiOperation(value = "gets all unique brand list")
	@GetMapping(path = "/api/brands/unique")
	public List<String> getBrandList(){
		return brandDto.getBrandList();
	}

	@ApiOperation(value = "gets all unique category list")
	@GetMapping(path = "/api/brands/unique/{brand}")
	public List<String> getCategoryList(@PathVariable String brand){
		return brandDto.getCategoryList(brand);
	}

	@ApiOperation(value = "Gets list of all Brands")
	@RequestMapping(path = "/api/brands", method = RequestMethod.GET)
	public List<BrandData> getAllBrands() {
		return brandDto.getAllBrands();
	}

	@ApiOperation(value = "Updates an Brand")
	@RequestMapping(path = "/api/brands/{brandId}", method = RequestMethod.PUT)
	public void update(@PathVariable int brandId, @RequestBody BrandForm brandForm) throws ApiException {
		brandDto.updateBrand(brandId, brandForm);
	}
}
