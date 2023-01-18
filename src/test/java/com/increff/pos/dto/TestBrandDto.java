package com.increff.pos.dto;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PojoUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Objects;

public class TestBrandDto extends AbstractUnitTest{
    @Autowired
    private final BrandDao brandDao = new BrandDao();
    @Autowired
    private  final BrandDto brandDto = new BrandDto();
    //Testing The CRUD Operations
    @Test
    public void testAddAndGetAll() throws ApiException {
        String brand = "testBrand";
        String category = "testCategory";

        //Check The Insert Operation
        BrandPojo p = PojoUtil.getBrandPojo(brand,category);
        brandDao.insert(p);

        //Verifying the getAll operation
        List<BrandData> list = brandDto.getAll();
        assertEquals(list.get(0).getBrand(),brand);
        assertEquals(list.get(0).getCategory(),category);
    }
    @Test
    public void testGetById() throws ApiException {
        String brand = "testBrand";
        String category = "testCategory";

        BrandPojo p = PojoUtil.getBrandPojo(brand,category);
        brandDao.insert(p);
        List<BrandData> list = brandDto.getAll();

        //Verifying the getById operation
        BrandData data = brandDto.get(list.get(0).getId());
        assertEquals(data.getBrand(),brand);
        assertEquals(data.getCategory(),category);

    }
    @Test
    public void testUpdate() throws ApiException {
        String brand = "testBrand";
        String category = "testCategory";

        BrandPojo p = PojoUtil.getBrandPojo(brand,category);
        brandDao.insert(p);

        List<BrandData> list = brandDto.getAll();

        //Verifying the Update function
        brand="brand";
        category="category";
        int id = list.get(0).getId();
        brandDto.update(list.get(0).getId(),PojoUtil.getBrandForm(brand,category));
        list = brandDto.getAll();
        assertEquals(list.get(0).getBrand(),brand);
        assertEquals(list.get(0).getCategory(),category);
        assertEquals(list.get(0).getId(),id);
    }

    //To check if brand and category can be unique
    @Test
    public void testBrandCategoryUniqueness(){
        BrandPojo p = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(p);
        //Check Uniqueness at time of add
        boolean flag = false;
        try {
            BrandForm f = PojoUtil.getBrandForm("brand","category");
            brandDto.add(f);
        }catch (ApiException e){
            flag=true;
        }
        if(Objects.equals(flag,false))fail();

        //Check Uniqueness at the time of update
        try {
            List<BrandData> list = brandDto.getAll();
            BrandForm f = PojoUtil.getBrandForm("brand","category");
            brandDto.update(list.get(0).getId(),f);
        }catch (ApiException e){
            return;
        }
        fail();
    }
    @Test
    public void testEmptyExceptions(){
        try{
            brandDto.add(PojoUtil.getBrandForm("","category"));
        }catch (ApiException e){
            return;
        }
        fail();
    }
}
