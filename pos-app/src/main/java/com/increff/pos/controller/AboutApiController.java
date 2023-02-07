package com.increff.pos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.AboutAppDto;
import com.increff.pos.model.data.AboutAppData;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class AboutApiController {

	@Autowired
	private AboutAppDto dto;

	@ApiOperation(value = "Gives application name and version")
	@RequestMapping(path = "/api/about", method = RequestMethod.GET)
	public AboutAppData getDetails() {
		return dto.getDetails();
	}



}
