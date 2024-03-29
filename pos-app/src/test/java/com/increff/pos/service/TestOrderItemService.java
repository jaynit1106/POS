package com.increff.pos.service;

import com.increff.pos.dao.*;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.PojoUtil;
import org.hibernate.criterion.Order;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestOrderItemService extends AbstractUnitTest{
    @Autowired
    private  BrandService brandService ;
    @Autowired
    private  BrandDao brandDao ;
    @Autowired
    private  ProductService productService ;
    @Autowired
    private  ProductDao productDao ;
    @Autowired
    private  InventoryService inventoryService ;
    @Autowired
    private  InventoryDao inventoryDao ;
    @Autowired
    private  OrderDao orderDao ;
    @Autowired
    private  OrderItemDao orderItemDao ;
    @Autowired
    private OrderService orderService;

    @Test
    public void testAdd() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand", "category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name", "abcdabcd", 30, brandService.getAllBrands().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100, productService.getAllProducts().get(0).getId()));
        //adds order
        orderDao.insert(PojoUtil.getOrderPojo());
        //adds order item
        List<OrderItemPojo> list = new ArrayList<>();
        OrderItemPojo pojo = PojoUtil.getOrderItemPojo(orderDao.selectAll(OrderPojo.class).get(0).getId(), 20, productDao.selectAll(ProductPojo.class).get(0).getId(), 20.22);
        list.add(pojo);

        orderService.addOrderItems(list);

        //checking if the product is added
        List<OrderItemPojo> data = orderService.getOrderItemByOrderId(orderDao.selectAll(OrderPojo.class).get(0).getId());
        Assert.assertEquals(data.size(),1);
    }

    @Test
    public void testGetOrderItemBYOrderId() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDao.selectAll(BrandPojo.class).get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDao.selectAll(ProductPojo.class).get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-item
        orderItemDao.insert(PojoUtil.getOrderItemPojo(orderDao.selectAll(OrderPojo.class).get(0).getId(),20,productDao.selectAll(ProductPojo.class).get(0).getId(),100));

        //checking the operation
        List<OrderItemPojo> list = orderService.getOrderItemByOrderId(orderDao.selectAll(OrderPojo.class).get(0).getId());
        OrderItemPojo item = list.get(0);
        assertEquals(item.getSellingPrice(),100,0);
        assertEquals(item.getQuantity(),20);
        assertEquals(item.getOrderId(),orderDao.selectAll(OrderPojo.class).get(0).getId());
    }



}
