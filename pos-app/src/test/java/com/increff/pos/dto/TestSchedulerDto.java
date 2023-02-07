package com.increff.pos.dto;

import com.increff.pos.dao.*;
import com.increff.pos.model.data.SchedulerData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PojoUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

public class TestSchedulerDto extends AbstractUnitTest{
    @Autowired
    private  BrandDao brandDao ;
    @Autowired
    private  ProductDao productDao ;
    @Autowired
    private  InventoryDao inventoryDao ;
    @Autowired
    private  OrderDao orderDao ;
    @Autowired
    private  OrderItemDao orderItemDao ;
    @Autowired
    private  SchedulerDto schedulerDto ;

    @Test
    public void testAdd() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDao.selectAll(BrandPojo.class).get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDao.selectAll(ProductPojo.class).get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-items
        orderItemDao.insert(PojoUtil.getOrderItemPojo(orderDao.selectAll(OrderPojo.class).get(0).getId(),11,productDao.selectAll(ProductPojo.class).get(0).getId(),20));

        //testing scheduler add
        schedulerDto.add();
        SchedulerData p = schedulerDto.get(String.valueOf(Instant.now()).substring(0,10));
        Assert.assertNotNull(p);

    }

    @Test
    public void testGetById() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDao.selectAll(BrandPojo.class).get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDao.selectAll(ProductPojo.class).get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-items
        orderItemDao.insert(PojoUtil.getOrderItemPojo(orderDao.selectAll(OrderPojo.class).get(0).getId(),11,productDao.selectAll(ProductPojo.class).get(0).getId(),20));
        //creating a schedule
        schedulerDto.add();

        SchedulerData data = schedulerDto.get(String.valueOf(Instant.now()).substring(0,10));

        Assert.assertEquals(data.getInvoiced_orders_count(),1);
        Assert.assertEquals(data.getTotal_revenue(),220,0);
        Assert.assertEquals(data.getInvoiced_items_count(),11);

    }
}
