package com.increff.pos.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
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
public class OrderService {

	@Autowired
	private final OrderDao dao = new OrderDao();

	@Transactional(rollbackOn = ApiException.class)
	public void add(OrderPojo p) throws ApiException {
		dao.insert(p);
	}

	@Transactional(rollbackOn = ApiException.class)
	public OrderPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<OrderPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional
	public OrderPojo getCheck(int id) throws ApiException {
		OrderPojo p = dao.select(id);
		if (Objects.isNull(p)) {
			throw new ApiException("Order with given ID does not exit, id: " + id);
		}
		return p;
	}
	
	public void downloadPdf(@PathVariable int id, HttpServletRequest request,HttpServletResponse response) throws ApiException {
		try {
		
		File file = new File("C:\\Increff Project\\POS\\src\\main\\resources\\com\\increff\\pos\\pdf\\invoice "+id+".pdf");
		
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		String mimeType = URLConnection.guessContentTypeFromStream(inputStream);
		
		if(mimeType == null) {
			mimeType = "application/octet-stream";
		}
		
		response.setContentType(mimeType);
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
		
		FileCopyUtils.copy(inputStream, response.getOutputStream());
		}catch (Exception e) {
			throw new ApiException("Unable to download the file");
		}
	}
}
