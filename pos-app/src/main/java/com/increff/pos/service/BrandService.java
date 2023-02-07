package com.increff.pos.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.increff.pos.dao.BrandDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.StringUtil;


@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandService {

	@Autowired
	private BrandDao brandDao ;


	public void addBrand(BrandPojo brandPojo) throws ApiException {
		validateBrandPojo(brandPojo);
		brandDao.insert(brandPojo);
	}

	public BrandPojo getBrandById(int brandId) throws ApiException {
		BrandPojo brandPojo = brandDao.select(brandId,BrandPojo.class);
		if(Objects.isNull(brandPojo)) {
			throw new ApiException("Brand with id-" + brandId + " does not exist");
		}
		return brandPojo;
	}

	public List<BrandPojo> getAllBrands() {
		return brandDao.selectAll(BrandPojo.class);
	}

	public void updateBrand(int brandId, BrandPojo brandPojo) throws ApiException {
		validateBrandPojo(brandPojo);

		BrandPojo ex = brandDao.select(brandId,BrandPojo.class);
		if(Objects.isNull(ex)) {
			throw new ApiException("Brand with id-" + brandId + " does not exist");
		}

		ex. setCategory(brandPojo.getCategory());
		ex. setBrand(brandPojo.getBrand());
	}

	public List<String> getBrandList(){
		List<String> brandList = brandDao.getBrandList();
		Collections.sort(brandList);
		Collections.reverse(brandList);
		return brandList;
	}
	public List<String> getCategoryList(String brand){
		if(Objects.equals(brand,"All")){
			List<BrandPojo> brandList = brandDao.selectAll(BrandPojo.class);

			Set<String> categorySet = brandList.stream().map(BrandPojo::getCategory).collect(Collectors.toSet());
			List<String> categoryList = categorySet.stream().collect(Collectors.toList());

			Collections.sort(categoryList);
			Collections.reverse(categoryList);
			return categoryList;
		}
		List<String> categoryList = brandDao.getCategoryList(brand);
		Collections.sort(categoryList);
		Collections.reverse(categoryList);
		return categoryList;
	}
	
	public BrandPojo getBrandByNameAndCategory(String brand,String category) throws ApiException{
		BrandPojo brandPojo = brandDao.getBrandByNameAndCategory(brand, category);
		if(Objects.isNull(brandPojo)) {
			throw new ApiException("Brand And Category Does Not Exist");
		}
		return brandPojo;
	}

	public void validateBrandPojo(BrandPojo brandPojo) throws ApiException{
		if(StringUtil.isEmpty(brandPojo.getBrand())) {
			throw new ApiException("Brand cannot be empty");
		}
		if(StringUtil.isEmpty(brandPojo.getCategory())) {
			throw new ApiException("Category cannot be empty");
		}
		if(Objects.nonNull(brandDao.getBrandByNameAndCategory(brandPojo.getBrand(),brandPojo.getCategory()))) {
			throw new ApiException("Brand and Category already exists");
		}
	}

}
