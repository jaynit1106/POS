package com.increff.pos.dto;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PojoUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

public class TestBrandDto extends AbstractUnitTest{
    @Autowired
    private  BrandDao brandDao;
    @Autowired
    private   BrandDto brandDto;


    //Testing The CRUD Operations
    @Test
    public void testAddAndgetAllBrands() throws ApiException {
        String brand = "testBrand";
        String category = "testCategory";

        //Check The Insert Operation
        BrandPojo p = PojoUtil.getBrandPojo(brand,category);
        brandDao.insert(p);

        //Verifying the getAll operation
        List<BrandData> list = brandDto.getAllBrands();
        assertEquals(list.get(0).getBrand(),brand);
        assertEquals(list.get(0).getCategory(),category);
    }
    @Test
    public void testGetById() throws ApiException {
        String brand = "testBrand";
        String category = "testCategory";

        BrandPojo p = PojoUtil.getBrandPojo(brand,category);
        brandDao.insert(p);
        List<BrandData> list = brandDto.getAllBrands();

        //Verifying the getById operation
        BrandData data = brandDto.getBrandById(list.get(0).getId());
        assertEquals(data.getBrand(),brand);
        assertEquals(data.getCategory(),category);

    }
    @Test
    public void testUpdate() throws ApiException {
        String brand = "testBrand";
        String category = "testCategory";

        BrandPojo p = PojoUtil.getBrandPojo(brand,category);
        brandDao.insert(p);

        List<BrandData> list = brandDto.getAllBrands();

        //Verifying the Update function
        brand="brand";
        category="category";
        int id = list.get(0).getId();
        brandDto.updateBrand(list.get(0).getId(),PojoUtil.getBrandForm(brand,category));
        list = brandDto.getAllBrands();
        assertEquals(list.get(0).getBrand(),brand);
        assertEquals(list.get(0).getCategory(),category);
        assertEquals(list.get(0).getId(),id);
    }

    //To check if brand and category can be unique
    @Test
    public void testBrandCategoryUniquenessAtAdd() throws ApiException {
        BrandPojo p = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(p);

        //Check Uniqueness at time of add
        BrandForm f = PojoUtil.getBrandForm("brand","category");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand and Category already exists");
        brandDto.addBrand(f);

    }

    @Test
    public void testBrandCategoryUniquenessAtUpdate() throws ApiException {
        BrandPojo p = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(p);

        //Check Uniqueness at the time of update
        List<BrandData> list = brandDto.getAllBrands();
        BrandForm form = PojoUtil.getBrandForm("brand","category");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand and Category already exists");
        brandDto.updateBrand(list.get(0).getId(),form);
    }
    @Test
    public void testEmptyExceptions() throws ApiException {
        //checking for empty constraints
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand cannot be empty");
        brandDto.addBrand(PojoUtil.getBrandForm("","category"));
    }

    @Test
    public void testGetUniqueBrands(){
        //adding multiple brands
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        brandDao.insert(PojoUtil.getBrandPojo("brand1","category1"));
        brandDao.insert(PojoUtil.getBrandPojo("brand","category2"));

        //checking for unique brands
        assertEquals(2,brandDto.getBrandList().size());

    }
    @Test
    public void testUniqueCategoryList(){
        //adding multiple brands
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        brandDao.insert(PojoUtil.getBrandPojo("brand1","category1"));
        brandDao.insert(PojoUtil.getBrandPojo("brand","category2"));
        brandDao.insert(PojoUtil.getBrandPojo("brand","category3"));

        //checking for unique brands
        assertEquals(3,brandDto.getCategoryList("brand").size());
    }

}
