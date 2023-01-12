package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.SchedulerDto;
import com.increff.pos.model.SchedulerData;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class SchedulerApiController {
	
	@Autowired
	private SchedulerDto dto;


	@ApiOperation(value = "Gets an Scheduler by ID")
	@RequestMapping(path = "/api/scheduler/{date}", method = RequestMethod.GET)
	public SchedulerData get(@PathVariable String date) throws ApiException {
		return dto.get(date);
	}

	@ApiOperation(value = "Gets list of all Schedulers")
	@RequestMapping(path = "/api/scheduler", method = RequestMethod.GET)
	public List<SchedulerData> getAll() {
		return dto.getAll();
	}

}
