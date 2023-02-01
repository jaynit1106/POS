package com.increff.pos.dto;

import com.increff.pos.dao.*;
import com.increff.pos.model.SchedulerData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.SchedulerPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PojoUtil;
import com.increff.pos.util.TimestampUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;
import java.util.List;

public class TestSchedulerDto extends AbstractUnitTest{
    @Autowired
    private final BrandDao brandDao = new BrandDao();
    @Autowired
    private final ProductDao productDao = new ProductDao();
    @Autowired
    private final InventoryDao inventoryDao = new InventoryDao();
    @Autowired
    private final OrderDao orderDao = new OrderDao();
    @Autowired
    private final OrderItemDao orderItemDao = new OrderItemDao();
    @Autowired
    private final SchedulerDto schedulerDto = new SchedulerDto();

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
        List<SchedulerData> list = schedulerDto.getAll();
        Assert.assertEquals(list.size(),1);

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

        SchedulerData data = schedulerDto.get(schedulerDto.getAll().get(0).getDate());

        Assert.assertEquals(data.getInvoiced_orders_count(),1);
        Assert.assertEquals(data.getTotal_revenue(),220,0);
        Assert.assertEquals(data.getInvoiced_items_count(),11);
        Assert.assertEquals(data.getDate(),schedulerDto.getAll().get(0).getDate());

    }
}
