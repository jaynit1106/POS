package com.increff.pos.service;

import java.time.Instant;
import java.time.Period;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.SchedulerDao;
import com.increff.pos.pojo.SchedulerPojo;


@Service
@Transactional(rollbackOn = ApiException.class)
public class SchedulerService {

	@Autowired
	private  SchedulerDao schedulerDao;
	

	public void addReport(SchedulerPojo schedulerPojo) throws ApiException {
		schedulerDao.insert(schedulerPojo);
	}

	public SchedulerPojo get(String date) throws ApiException {
		return schedulerDao.select(date);
	}

	public List<SchedulerPojo> getReportInRange(String startDate,String endDate) throws ApiException {
		if(startDate.compareTo(endDate)>0){throw new ApiException("please select a valid time range");}

		Instant range = Instant.now().minus(Period.ofDays(365));
		if(String.valueOf(range).compareTo(startDate+"T00:00:00Z")>0){throw new ApiException("Only one year is allowed for the reports");}

		return schedulerDao.selectRange(startDate,endDate);
	}

}
