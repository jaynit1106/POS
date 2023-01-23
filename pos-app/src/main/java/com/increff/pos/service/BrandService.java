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
@Transactional(rollbackOn = ApiException.class)
public class BrandService {

	@Autowired
	private final BrandDao dao = new BrandDao();


	public void add(BrandPojo p) throws ApiException {
		if(StringUtil.isEmpty(p.getBrand())) {
			throw new ApiException("Brand cannot be empty");
		}
		if(StringUtil.isEmpty(p.getCategory())) {
			throw new ApiException("Category cannot be empty");
		}
		if(Objects.nonNull(dao.checkRepeat(p.getBrand(),p.getCategory())))throw new ApiException("Brand and Category already exists");
		dao.insert(p);
	}

	public BrandPojo get(int id) throws ApiException {
		BrandPojo p = dao.select(id,BrandPojo.class);
		if(Objects.isNull(p))throw new ApiException("Brand with id-"+id+" does not exist");
		return p;
	}

	public List<BrandPojo> getAll() {
		return dao.selectAll(BrandPojo.class);
	}

	public void update(int id, BrandPojo p) throws ApiException {
		if(StringUtil.isEmpty(p.getBrand())) {
			throw new ApiException("Brand cannot be empty");
		}
		if(StringUtil.isEmpty(p.getCategory())) {
			throw new ApiException("Category cannot be empty");
		}
		if(Objects.nonNull(dao.checkRepeat(p.getBrand(),p.getCategory())))throw new ApiException("Brand and Category already exists");
		BrandPojo ex = dao.select(id,BrandPojo.class);
		if(Objects.isNull(ex))throw new ApiException("Brand with id-"+id+" does not exist");
		ex. setCategory(p.getCategory());
		ex. setBrand(p.getBrand());
	}

	public List<String> getBrandList(){
		List<String> list = dao.getBrandList();
		Collections.sort(list);
		Collections.reverse(list);
		return list;
	}
	public List<String> getCategoryList(String brand){
		List<String> list = dao.getCategoryList(brand);
		Collections.sort(list);
		Collections.reverse(list);
		return list;
	}
	
	public BrandPojo getBrandId(String brand,String category) throws ApiException{
		BrandPojo p = dao.checkRepeat(brand, category);
		if(Objects.isNull(p))throw new ApiException("Brand And Category Does Not Exist");
		return p;
	}

}
