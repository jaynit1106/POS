package com.increff.pos.service;

import com.increff.pos.dao.*;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.util.PojoUtil;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TestInventoryService extends AbstractUnitTest{
    @Autowired
    private final BrandService brandService = new BrandService();
    @Autowired
    private final BrandDao brandDao = new BrandDao();
    @Autowired
    private final ProductService productService = new ProductService();
    @Autowired
    private final ProductDao productDao = new ProductDao();
    @Autowired
    private final InventoryService inventoryService = new InventoryService();
    @Autowired
    private final InventoryDao inventoryDao = new InventoryDao();
    @Autowired
    private final OrderDao orderDao = new OrderDao();
    @Autowired
    private final OrderItemDao orderItemDao = new OrderItemDao();


    @Test
    public void testAdd() throws ApiException {
        //adds brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //adds product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",20,brandService.getAll().get(0).getId()));

        //inserting inventory
        inventoryService.add(PojoUtil.getInventoryPojo(100,productService.getAll().get(0).getId()));

        List<InventoryPojo> list = inventoryService.getAll();

        //checking if it is added
        assertEquals(list.size(),1);
    }

    @Test
    public void testGetAll() throws ApiException {
        //adds brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //adds products
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",20,brandService.getAll().get(0).getId()));
        productDao.insert(PojoUtil.getProductPojo("name1","abcdabce",20,brandService.getAll().get(0).getId()));

        //inserting inventory
        inventoryService.add(PojoUtil.getInventoryPojo(200,productService.getAll().get(0).getId()));
        inventoryService.add(PojoUtil.getInventoryPojo(100,productService.getAll().get(1).getId()));

        //checking the operation
        assertEquals(2,inventoryService.getAll().size());
    }

    @Test
    public void testUpdate() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandService.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productService.getAll().get(0).getId()));

        List<InventoryPojo> list = inventoryService.getAll();

        //update
        inventoryService.update(list.get(0).getId(),PojoUtil.getInventoryPojo(200,productService.getAll().get(0).getId()));
        list = inventoryService.getAll();

        //checking update operation
        InventoryPojo data = inventoryService.get(list.get(0).getId());
        assertEquals(data.getQuantity(),200);
    }

    @Test
    public void testCheckAndCreateOrder() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandService.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productService.getAll().get(0).getId()));
        //adds order
        orderDao.insert(PojoUtil.getOrderPojo());

        List<OrderItemPojo> list = new ArrayList<>();
        OrderItemPojo pojo = PojoUtil.getOrderItemPojo(orderDao.selectAll().get(0).getId(),20,productDao.selectAll().get(0).getId(),20.22);
        list.add(pojo);

        inventoryService.checkAndCreateOrder(list);

        //checking if the quantity is reduced or not
        assertEquals(80,inventoryService.getAll().get(0).getQuantity());
    }

    //ask mentor about it
//    @Test
//    public void testCheckQuantityRollBack() throws ApiException {
//        //create brand
//        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
//        //create product
//        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandService.getAll().get(0).getId()));
//        //add inventory
//        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productService.getAll().get(0).getId()));
//        //adds order
//        orderDao.insert(PojoUtil.getOrderPojo());
//
//        try {
//            List<OrderItemPojo> list = new ArrayList<>();
//            //first item with valid quantity
//            OrderItemPojo pojo1 = PojoUtil.getOrderItemPojo(orderDao.selectAll().get(0).getId(), 20, productDao.selectAll().get(0).getId(), 20.22);
//            list.add(pojo1);
//
//            //second item with invalid quantity
//            OrderItemPojo pojo2 = PojoUtil.getOrderItemPojo(orderDao.selectAll().get(0).getId(), 200, productDao.selectAll().get(0).getId(), 20.22);
//            list.add(pojo2);
//
//            inventoryService.checkAndCreateOrder(list);
//        }catch (ApiException e){
//            //checking if the quantity is roll backed
////            assertEquals(100,inventoryService.getAll().get(0).getQuantity());
//            return;
//        }
//        fail();
//    }

}
