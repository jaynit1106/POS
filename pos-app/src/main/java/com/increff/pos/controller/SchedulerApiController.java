package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.model.form.SchedulerForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.increff.pos.dto.SchedulerDto;
import com.increff.pos.model.data.SchedulerData;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class SchedulerApiController {
	
	@Autowired
	private SchedulerDto schedulerDto;

	@ApiOperation(value = "Gets list of all Schedulers")
	@RequestMapping(path = "/api/scheduler", method = RequestMethod.POST)
	public List<SchedulerData> getReportInRange(@RequestBody SchedulerForm schedulerForm) throws ApiException {
		return schedulerDto.getReportInRange(schedulerForm);
	}

}
