package com.increff.pos.dto;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.ProductData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PojoUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

public class TestProductDto extends AbstractUnitTest{
    @Autowired
    private final ProductDto productDto = new ProductDto();
    @Autowired
    private final ProductDao productDao = new ProductDao();
    @Autowired
    private final BrandDao brandDao = new BrandDao();
    @Autowired
    private  final BrandDto brandDto = new BrandDto();

    @Test
    public void testAddAndGetById() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAll();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name","abcdabcd",29,list.get(0).getId());
        productDao.insert(p);

        //checking the add and getAll operation
        List<ProductData> productDataList = productDto.getAll();
        assertEquals(productDataList.get(0).getBarcode(),"abcdabcd");
        assertEquals(productDataList.get(0).getMrp(),"29.00");
        assertEquals(productDataList.get(0).getName(),"name");
        assertEquals(productDataList.get(0).getBrand(),"brand");
        assertEquals(productDataList.get(0).getCategory(),"category");
    }

    @Test
    public void testGetById() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAll();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name","abcdabcd",29,list.get(0).getId());
        productDao.insert(p);
        List<ProductData> productDataList = productDto.getAll();

        //checking getById
        ProductData product = productDto.get(productDataList.get(0).getId());
        assertEquals(product.getBarcode(),"abcdabcd");
        assertEquals(product.getMrp(),"29.00");
        assertEquals(product.getName(),"name");
        assertEquals(product.getBrand(),"brand");
        assertEquals(product.getCategory(),"category");
    }
    @Test
    public void testUpdate() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAll();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name","abcdabcd",29,list.get(0).getId());
        productDao.insert(p);
        List<ProductData> productDataList = productDto.getAll();

        //checking the update Operation
        productDto.update(productDataList.get(0).getId(),PojoUtil.getProductForm("brand","category","changedName","abcdabcd",290));
        productDataList = productDto.getAll();
        assertEquals(productDataList.get(0).getBarcode(),"abcdabcd");
        assertEquals(productDataList.get(0).getMrp(),"290.00");
        assertEquals(productDataList.get(0).getName(),"changedname");
        assertEquals(productDataList.get(0).getBrand(),"brand");
        assertEquals(productDataList.get(0).getCategory(),"category");
    }

    @Test
    public void testForUniquenessExceptions() {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand", "category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAll();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name", "abcdabcd", 29, list.get(0).getId());
        productDao.insert(p);

        boolean flag=false;

        //checking same brand category name exception
        try {
            productDto.add(PojoUtil.getProductForm("brand", "category", "name", "abcdabce", 290));
        }catch (ApiException e){
            flag=true;
        }
        if(Objects.equals(flag,false))fail();

        //Checking same barcode exception
        try {
            //checking if API exception are thrown
            productDto.add(PojoUtil.getProductForm("brand", "category", "names", "abcdabcd", 290));
        }catch (ApiException e){
            return;
        }
        fail();
    }

    @Test
    public void testForBrandAndCategoryExists(){
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand", "category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAll();

        try{
            //checking if API exception is thrown when brand and category does not exist
            productDto.add(PojoUtil.getProductForm("brand1", "category1", "names", "abcdabcd", 290));
        }catch (ApiException e){
            return;
        }
        fail();
    }

    @Test
    public void testForFormats(){
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand", "category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAll();

        boolean flag = false;
        //checks for incorrect barcode format
        try {
            productDto.add(PojoUtil.getProductForm("brand", "category", "names", "abcd", 290));
        } catch (ApiException e) {
            flag = true;
        }
        if(Objects.equals(flag,false))fail();

        //check for negative mrp
        try {
            productDto.add(PojoUtil.getProductForm("brand", "category", "names", "abcdabcf", -290));
        } catch (ApiException e) {
           return;
        }
        fail();
    }
}
