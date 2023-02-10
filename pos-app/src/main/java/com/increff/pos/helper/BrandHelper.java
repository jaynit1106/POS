package com.increff.pos.helper;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BrandHelper {

    @Autowired
    private  BrandDao brandDao;
    public void validateBrandPojo(BrandPojo brandPojo) throws ApiException {
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
