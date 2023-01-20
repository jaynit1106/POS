package com.increff.pos.service;

import com.increff.pos.dao.*;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.util.PojoUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestOrderItemService extends AbstractUnitTest{
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
    @Autowired
    private final OrderItemService orderItemService = new OrderItemService();

    @Test
    public void testAdd() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand", "category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name", "abcdabcd", 30, brandService.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100, productService.getAll().get(0).getId()));
        //adds order
        orderDao.insert(PojoUtil.getOrderPojo());
        //adds order item
        List<OrderItemPojo> list = new ArrayList<>();
        OrderItemPojo pojo = PojoUtil.getOrderItemPojo(orderDao.selectAll().get(0).getId(), 20, productDao.selectAll().get(0).getId(), 20.22);
        list.add(pojo);

        orderItemService.add(list);

        //checking if the product is added
        List<OrderItemPojo> data = orderItemService.getAll();
        Assert.assertEquals(data.size(),1);
    }
    @Test
    public void testGetAll() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand", "category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name", "abcdabcd", 30, brandService.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100, productService.getAll().get(0).getId()));
        //adds order
        orderDao.insert(PojoUtil.getOrderPojo());
        //adds order item
        List<OrderItemPojo> list = new ArrayList<>();
        OrderItemPojo pojo = PojoUtil.getOrderItemPojo(orderDao.selectAll().get(0).getId(), 20, productDao.selectAll().get(0).getId(), 20.22);
        list.add(pojo);
        pojo = PojoUtil.getOrderItemPojo(orderDao.selectAll().get(0).getId(), 20, productDao.selectAll().get(0).getId(), 20.22);
        list.add(pojo);
        pojo = PojoUtil.getOrderItemPojo(orderDao.selectAll().get(0).getId(), 20, productDao.selectAll().get(0).getId(), 20.22);
        list.add(pojo);
        orderItemService.add(list);

        //checking the get all operation
        List<OrderItemPojo> data = orderItemService.getAll();
        Assert.assertEquals(data.size(),3);
    }

    @Test
    public void testGetOrderItemBYOrderId() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDao.selectAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDao.selectAll().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-item
        orderItemDao.insert(PojoUtil.getOrderItemPojo(orderDao.selectAll().get(0).getId(),20,productDao.selectAll().get(0).getId(),100));

        //checking the operation
        List<OrderItemPojo> list = orderItemService.getOrderItemByOrderId(orderDao.selectAll().get(0).getId());
        OrderItemPojo item = list.get(0);
        assertEquals(item.getSellingPrice(),100,0);
        assertEquals(item.getQuantity(),20);
        assertEquals(item.getOrderId(),orderDao.selectAll().get(0).getId());
    }

    @Test
    public void testGetById() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDao.selectAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDao.selectAll().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-item
        orderItemDao.insert(PojoUtil.getOrderItemPojo(orderDao.selectAll().get(0).getId(),20,productDao.selectAll().get(0).getId(),100));

        List<OrderItemPojo> list = orderItemService.getAll();

        //checking get by Id
        OrderItemPojo data = orderItemService.get(list.get(0).getId());
        assertEquals(data.getQuantity(),20);
        assertEquals(data.getSellingPrice(),100,0);
        assertEquals(data.getId(),list.get(0).getId());
    }

    @Test
    public void testGetCheck(){
        //checking if Api Exception is thrown or not
        try {
            orderItemService.get(10000);
        }catch (ApiException e){
            return;
        }
        fail();
    }
}
