package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductApiController {

	@Autowired
	private ProductService service;

	@ApiOperation(value = "Adds a Product")
	@RequestMapping(path = "/api/product", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm form) throws ApiException {
		ProductPojo p = convert(form);
		p.setbrandId(service.getBrandId(form.getBrand(), form.getCategory()));
		p.setBarcode(service.generateBarcode(p));
		service.productExist(p.getbrandId(), p.getName());
		service.add(p);
	}

	
	@ApiOperation(value = "Deletes a Product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets a Product by ID")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
	public ProductData get(@PathVariable int id) throws ApiException {
		ProductPojo p = service.get(id);
		return convert(p);
	}

	@ApiOperation(value = "Gets list of all products")
	@RequestMapping(path = "/api/product", method = RequestMethod.GET)
	public List<ProductData> getAll() {
		List<ProductPojo> list = service.getAll();
		List<ProductData> list2 = new ArrayList<ProductData>();
		for (ProductPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates a product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm f) throws ApiException {
		ProductPojo p = convert(f);
		p.setbrandId(service.getBrandId(f.getBrand(), f.getCategory()));
		service.productExist(p.getbrandId(), p.getName());
		service.update(id, p);
	}
	

	private static ProductData convert(ProductPojo p) {
		ProductData d = new ProductData();
		d.setName(p.getName());
		d.setId(p.getId());
		d.setBarcode(p.getBarcode());
		d.setMrp(p.getMrp());
		return d;
	}

	private static ProductPojo convert(ProductForm f) {
		ProductPojo p = new ProductPojo();
		p.setName(f.getName());
		p.setMrp(f.getMrp());
		return p;
	}

}
