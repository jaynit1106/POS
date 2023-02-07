package com.increff.pos.dto;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PojoUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestInventoryDto extends AbstractUnitTest{
    @Autowired
    private  BrandDao brandDao ;
    @Autowired
    private  ProductDao productDao ;
    @Autowired
    private  InventoryDao inventoryDao ;
    @Autowired
    private  InventoryDto inventoryDto ;
    @Autowired
    private  BrandDto brandDto ;
    @Autowired
    private  ProductDto productDto ;

    @Test
    public void testAddAndgetAllBrands() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAllBrands().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAllProducts().get(0).getId()));

        //checking the getAll operations
        List<InventoryData> list = inventoryDto.getAllInventorys();
        assertEquals(list.get(0).getQuantity(),100);
        assertEquals(list.get(0).getName(),"name");
        assertEquals(list.get(0).getBarcode(),"abcdabcd");
        assertEquals(list.get(0).getId(),productDto.getAllProducts().get(0).getId());

    }

    @Test
    public void testUpdate() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAllBrands().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAllProducts().get(0).getId()));

        List<InventoryData> list = inventoryDto.getAllInventorys();

        //update
        inventoryDto.updateInventory(list.get(0).getId(),PojoUtil.getInventoryForm(200,"abcdabcd"));
        list = inventoryDto.getAllInventorys();

        //checking update operation
        InventoryData data = inventoryDto.getAllInventorys().get(0);
        assertEquals(data.getQuantity(),200);
        assertEquals(data.getName(),"name");
        assertEquals(data.getBarcode(),"abcdabcd");
        assertEquals(data.getId(),productDto.getAllProducts().get(0).getId());
    }

    @Test
    public void testForNegativeQuantity() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAllBrands().get(0).getId()));

        //Negative Quantity API exception
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Quantity cannot be Negative");
        inventoryDto.addInventory(PojoUtil.getInventoryForm(-100,"abcdabcd"));

    }
    @Test
    public void testForAdditionOfInventory() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAllBrands().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAllProducts().get(0).getId()));

        //adding the quantity so the quantity should be 100+150 = 250
        inventoryDto.addInventory(PojoUtil.getInventoryForm(150,"abcdabcd"));


        //checking the quantity
        List<InventoryData> list = inventoryDto.getAllInventorys();
        assertEquals(list.get(0).getQuantity(),250);
        assertEquals(list.get(0).getName(),"name");
        assertEquals(list.get(0).getBarcode(),"abcdabcd");
        assertEquals(list.get(0).getId(),productDto.getAllProducts().get(0).getId());
    }

    @Test
    public void testForProductDoesNotExist() throws ApiException {
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAllBrands().get(0).getId()));

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Product Does Not Exists");
        inventoryDto.addInventory(PojoUtil.getInventoryForm(100,"abcdabce"));

    }

}
