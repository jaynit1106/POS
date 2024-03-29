package com.increff.pos.service;

import com.increff.pos.dao.*;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.PojoUtil;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TestInventoryService extends AbstractUnitTest{
    @Autowired
    private  BrandService brandService = new BrandService();
    @Autowired
    private  BrandDao brandDao;
    @Autowired
    private  ProductService productService ;
    @Autowired
    private  ProductDao productDao ;
    @Autowired
    private  InventoryService inventoryService ;
    @Autowired
    private  InventoryDao inventoryDao;
    @Autowired
    private  OrderDao orderDao ;
    @Autowired
    private  OrderItemDao orderItemDao ;


    @Test
    public void testAdd() throws ApiException {
        //adds brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //adds product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",20,brandService.getAllBrands().get(0).getId()));

        //inserting inventory
        inventoryService.addInventory(PojoUtil.getInventoryPojo(100,productService.getAllProducts().get(0).getId()));

        List<InventoryPojo> list = inventoryService.getAllInventorys();

        //checking if it is added
        assertEquals(list.size(),1);
    }

    @Test
    public void testGetAll() throws ApiException {
        //adds brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //adds products
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",20,brandService.getAllBrands().get(0).getId()));
        productDao.insert(PojoUtil.getProductPojo("name1","abcdabce",20,brandService.getAllBrands().get(0).getId()));

        //inserting inventory
        inventoryService.addInventory(PojoUtil.getInventoryPojo(200,productService.getAllProducts().get(0).getId()));
        inventoryService.addInventory(PojoUtil.getInventoryPojo(100,productService.getAllProducts().get(1).getId()));

        //checking the operation
        assertEquals(2,inventoryService.getAllInventorys().size());
    }

    @Test
    public void testUpdate() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandService.getAllBrands().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productService.getAllProducts().get(0).getId()));

        List<InventoryPojo> list = inventoryService.getAllInventorys();

        //update
        inventoryService.updateInventory(list.get(0).getId(),PojoUtil.getInventoryPojo(200,productService.getAllProducts().get(0).getId()));
        list = inventoryService.getAllInventorys();

        //checking update operation
        InventoryPojo data = inventoryService.getInventoryById(list.get(0).getId());
        assertEquals(data.getQuantity(),200);
    }

    @Test
    public void testCheckAndCreateOrder() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandService.getAllBrands().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productService.getAllProducts().get(0).getId()));
        //adds order
        orderDao.insert(PojoUtil.getOrderPojo());

        List<OrderItemPojo> list = new ArrayList<>();
        OrderItemPojo pojo = PojoUtil.getOrderItemPojo(orderDao.selectAll(OrderPojo.class).get(0).getId(),20,productDao.selectAll(ProductPojo.class).get(0).getId(),20.22);
        list.add(pojo);

        inventoryService.checkAndCreateOrder(list);

        //checking if the quantity is reduced or not
        assertEquals(80,inventoryService.getAllInventorys().get(0).getQuantity());
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
