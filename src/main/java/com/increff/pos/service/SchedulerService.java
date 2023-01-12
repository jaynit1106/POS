package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.SchedulerDao;
import com.increff.pos.pojo.SchedulerPojo;


@Service
public class SchedulerService {

	@Autowired
	private SchedulerDao dao;
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(SchedulerPojo p) throws ApiException {
		dao.insert(p); 
	}

	@Transactional(rollbackOn = ApiException.class)
	public SchedulerPojo get(String date) throws ApiException {
		return getCheck(date);
	}

	@Transactional
	public List<SchedulerPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(String  date, SchedulerPojo p) throws ApiException {
		SchedulerPojo ex = getCheck(date);
		ex.setInvoiced_items_count(p.getInvoiced_items_count());
		ex.setInvoiced_orders_count(p.getInvoiced_orders_count());
		ex.setTotal_revenue(p.getTotal_revenue());
		return;
	}

	@Transactional
	public SchedulerPojo getCheck(String date) throws ApiException {
		SchedulerPojo p = dao.select(date);
		return p;
	}
}
