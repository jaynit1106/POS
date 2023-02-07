package com.increff.pos.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderPojo;


@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {

	@Autowired
	private OrderDao orderDao;


	public void addOrder(OrderPojo orderPojo) throws ApiException {
		orderDao.insert(orderPojo);
	}

	public OrderPojo getOrderById(int orderId) throws ApiException {
		OrderPojo orderPojo = orderDao.select(orderId,OrderPojo.class);
		if (Objects.isNull(orderPojo)) {
			throw new ApiException("Order with given ID does not exit, id: " + orderId);
		}
		return orderPojo;
	}

	public List<OrderPojo> getAllOrders() {
		return orderDao.selectAll(OrderPojo.class);
	}


	public List<OrderPojo> selectOrdersInRange(Instant startDate, Instant endDate){
		return orderDao.selectOrdersInRange(startDate,endDate);
	}

}
