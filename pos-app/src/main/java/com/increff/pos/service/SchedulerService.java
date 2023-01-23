package com.increff.pos.service;

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
	private final SchedulerDao dao = new SchedulerDao();
	

	public void add(SchedulerPojo p) throws ApiException {
		dao.insert(p); 
	}

	public SchedulerPojo get(String date) throws ApiException {
		return dao.select(date);
	}

	public List<SchedulerPojo> getAll() {
		return dao.selectAll(SchedulerPojo.class);
	}

}
