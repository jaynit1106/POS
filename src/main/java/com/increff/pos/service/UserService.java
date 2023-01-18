package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.model.InfoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndView;
@Service
public class UserService {

	@Autowired
	private final UserDao dao = new UserDao();

	@Autowired
	private InfoData info;

	@Transactional
	public ModelAndView add(UserPojo p) throws ApiException {
		normalize(p);
		UserPojo existing = dao.select(p.getEmail());
		if (existing != null) {
			info.setMessage("email id already exists");
			return new ModelAndView("redirect:/site/signup");
		}
		dao.insert(p);
		info.setMessage("");
		return  new ModelAndView("redirect:/site/login");
	}

	@Transactional(rollbackOn = ApiException.class)
	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}

	@Transactional
	public List<UserPojo> getAll() {
		return dao.selectAll();
	}

	protected static void normalize(UserPojo p) {
		p.setEmail(p.getEmail().toLowerCase().trim());
		p.setRole(p.getRole().toLowerCase().trim());
	}
}
