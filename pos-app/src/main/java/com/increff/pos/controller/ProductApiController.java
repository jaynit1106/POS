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
	private ProductDto productDto;

	@ApiOperation(value = "Adds a Product")
	@RequestMapping(path = "/api/products", method = RequestMethod.POST)
	public void addProduct(@RequestBody ProductForm productForm) throws ApiException {
		productDto.addProduct(productForm);
	}

	@ApiOperation(value = "Gets list of all products")
	@RequestMapping(path = "/api/products", method = RequestMethod.GET)
	public List<ProductData> getAllProducts() throws ApiException {
		return productDto.getAllProducts();
	}

	@ApiOperation(value = "Updates a product")
	@RequestMapping(path = "/api/products/{productId}", method = RequestMethod.PUT)
	public void updateProduct(@PathVariable int productId, @RequestBody ProductForm productForm) throws ApiException {
		productDto.updateProduct(productId, productForm);
	}

	@ApiOperation(value = "Get Barcode List")
	@GetMapping(path = "/api/products/barcode")
	public List<String> getBarcodeList(){
		return productDto.getBarcodeList();
	}
	



}
