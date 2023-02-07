package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.model.SchedulerForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
	@RequestMapping(path = "/api/scheduler", method = RequestMethod.POST)
	public List<SchedulerData> getRange(@RequestBody SchedulerForm form) throws ApiException {
		return dto.getRange(form);
	}

}
