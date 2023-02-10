package com.increff.pos.helper;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ProductHelper {
    @Autowired
    private  ProductDao productDao;

    public void validateProductPojo(ProductPojo productPojo) throws ApiException {
        if(StringUtil.isEmpty(productPojo.getName())) {
            throw new ApiException("Name cannot be empty");
        }
        if(productPojo.getBarcode().length()!=8) {
            throw new ApiException("Barcode Should be of 8 characters");
        }
        if(productPojo.getMrp()<=0) {
            throw new ApiException("MRP should be positive");
        }
        if(Objects.nonNull(productDao.getProductByNameAndBrandId(productPojo.getBrandId(),productPojo.getName()))) {
            throw new ApiException("Product already Exists");
        }
        if(Objects.nonNull(productDao.barcodeExist(productPojo.getBarcode()))) {
            throw new ApiException("Barcode already Exists");
        }
    }
}
