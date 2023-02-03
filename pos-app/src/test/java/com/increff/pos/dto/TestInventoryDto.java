package com.increff.pos.dto;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.InventoryData;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.util.PojoUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestInventoryDto extends AbstractUnitTest{
    @Autowired
    private final BrandDao brandDao = new BrandDao();
    @Autowired
    private final ProductDao productDao = new ProductDao();
    @Autowired
    private final InventoryDao inventoryDao = new InventoryDao();
    @Autowired
    private final InventoryDto inventoryDto = new InventoryDto();
    @Autowired
    private final BrandDto brandDto = new BrandDto();
    @Autowired
    private final ProductDto productDto = new ProductDto();

    @Test
    public void testAddAndGetAll() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAll().get(0).getId()));

        //checking the getAll operations
        List<InventoryData> list = inventoryDto.getAll();
        assertEquals(list.get(0).getQuantity(),100);
        assertEquals(list.get(0).getName(),"name");
        assertEquals(list.get(0).getBarcode(),"abcdabcd");
        assertEquals(list.get(0).getId(),productDto.getAll().get(0).getId());

    }

    @Test
    public void testGetById() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAll().get(0).getId()));

        List<InventoryData> list = inventoryDto.getAll();
        //checking getById operation
        InventoryData data = inventoryDto.get(list.get(0).getId());
        assertEquals(data.getQuantity(),100);
        assertEquals(data.getName(),"name");
        assertEquals(data.getBarcode(),"abcdabcd");
        assertEquals(data.getId(),productDto.getAll().get(0).getId());

    }

    @Test
    public void testUpdate() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAll().get(0).getId()));

        List<InventoryData> list = inventoryDto.getAll();

        //update
        inventoryDto.update(list.get(0).getId(),PojoUtil.getInventoryForm(200,"abcdabcd"));
        list = inventoryDto.getAll();

        //checking update operation
        InventoryData data = inventoryDto.get(list.get(0).getId());
        assertEquals(data.getQuantity(),200);
        assertEquals(data.getName(),"name");
        assertEquals(data.getBarcode(),"abcdabcd");
        assertEquals(data.getId(),productDto.getAll().get(0).getId());
    }

    @Test
    public void testForNegativeQuantity() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAll().get(0).getId()));

        //Negative Quantity API exception
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Quantity cannot be Negative");
        inventoryDto.add(PojoUtil.getInventoryForm(-100,"abcdabcd"));

    }
    @Test
    public void testForAdditionOfInventory() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAll().get(0).getId()));

        //adding the quantity so the quantity should be 100+150 = 250
        inventoryDto.add(PojoUtil.getInventoryForm(150,"abcdabcd"));


        //checking the quantity
        List<InventoryData> list = inventoryDto.getAll();
        assertEquals(list.get(0).getQuantity(),250);
        assertEquals(list.get(0).getName(),"name");
        assertEquals(list.get(0).getBarcode(),"abcdabcd");
        assertEquals(list.get(0).getId(),productDto.getAll().get(0).getId());
    }

    @Test
    public void testForProductDoesNotExist() throws ApiException {
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAll().get(0).getId()));

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Product Does Not Exists");
        inventoryDto.add(PojoUtil.getInventoryForm(100,"abcdabce"));

    }

}
