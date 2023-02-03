package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.PojoUtil;
import io.swagger.annotations.Api;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestBrandService extends AbstractUnitTest{

    @Autowired
    private final BrandService brandService = new BrandService();
    @Autowired
    private final BrandDao brandDao = new BrandDao();

    @Test
    public void testAdd() throws ApiException {
        //add a product
        brandService.add(PojoUtil.getBrandPojo("brand","brand"));
        List<BrandPojo> list = brandService.getAll();

        //checking if the product has been added
        assertEquals(1,list.size());
    }
    @Test
    public void testGetAll(){
        //add brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        brandDao.insert(PojoUtil.getBrandPojo("brand1","category1"));
        brandDao.insert(PojoUtil.getBrandPojo("brand2","category2"));

        //checking getAll operation
        List<BrandPojo> list = brandService.getAll();
        assertEquals(3,list.size());
    }

    @Test
    public void testGetById() throws ApiException {
        String brand = "testBrand";
        String category = "testCategory";

        BrandPojo p = PojoUtil.getBrandPojo(brand,category);
        brandDao.insert(p);
        List<BrandPojo> list = brandService.getAll();

        //Verifying the getById operation
        BrandPojo data = brandService.get(list.get(0).getId());
        assertEquals(data.getBrand(),brand);
        assertEquals(data.getCategory(),category);

    }

    @Test
    public void testUpdate() throws ApiException {
        String brand = "testBrand";
        String category = "testCategory";

        BrandPojo p = PojoUtil.getBrandPojo(brand,category);
        brandDao.insert(p);

        List<BrandPojo> list = brandService.getAll();

        //Verifying the Update function
        brand="brand";
        category="category";
        int id = list.get(0).getId();
        brandService.update(list.get(0).getId(),PojoUtil.getBrandPojo(brand,category));
        list = brandService.getAll();
        assertEquals(list.get(0).getBrand(),brand);
        assertEquals(list.get(0).getCategory(),category);
        assertEquals(list.get(0).getId(),id);
    }

    @Test
    public void testGetBrandId() throws ApiException {
        //creating brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));

        List<BrandPojo> list = brandService.getAll();

        //checking for BrandId
        BrandPojo p = brandService.getBrandId("brand","category");
        assertEquals(list.get(0).getId(),p.getId());
    }

    @Test
    public void testUniqueBrandList(){
        //adding multiple brands
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        brandDao.insert(PojoUtil.getBrandPojo("brand1","category1"));
        brandDao.insert(PojoUtil.getBrandPojo("brand","category2"));

        //checking for unique brands
        assertEquals(2,brandService.getBrandList().size());
    }

    @Test
    public void testUniqueCategoryList(){
        //adding multiple brands
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        brandDao.insert(PojoUtil.getBrandPojo("brand1","category1"));
        brandDao.insert(PojoUtil.getBrandPojo("brand","category2"));
        brandDao.insert(PojoUtil.getBrandPojo("brand","category3"));

        //checking for unique categories
        assertEquals(3,brandService.getCategoryList("brand").size());

        //checking for all category
        assertEquals(4,brandService.getCategoryList("All").size());
    }
}
