package com.increff.pos.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.StringUtil;


@Service
public class BrandService {

	@Autowired
	private final BrandDao dao = new BrandDao();


	@Transactional(rollbackOn = ApiException.class)
	public void add(BrandPojo p) throws ApiException {
		normalize(p);
		dao.insert(p);
	}

	@Transactional(rollbackOn = ApiException.class)
	public BrandPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<BrandPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, BrandPojo p) throws ApiException {
		normalize(p);
		BrandPojo ex = getCheck(id);
		ex. setCategory(p.getCategory());
		ex. setBrand(p.getBrand());
	}

	@Transactional
	public BrandPojo getCheck(int id) throws ApiException {
		BrandPojo p = dao.select(id);
		if (Objects.isNull(p)) {
			throw new ApiException("Brand with given ID does not exit, id: " + id);
		}
		return p;
	}
	@Transactional
	public List<String> getBrandList(){
		List<String> list = dao.getBrandList();
		Collections.sort(list);
		Collections.reverse(list);
		return list;
	}
	@Transactional
	public List<String> getCategoryList(String brand){
		List<String> list = dao.getCategoryList(brand);
		Collections.sort(list);
		Collections.reverse(list);
		return list;
	}
	
	@Transactional
	public BrandPojo getBrandId(String brand,String category){
		return dao.checkRepeat(brand, category);
	}

	protected static void normalize(BrandPojo p) {
		p. setBrand(StringUtil.toLowerCase(p. getBrand()));
		p. setCategory(StringUtil.toLowerCase(p.getCategory()));
	}
}
