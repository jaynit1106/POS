package com.increff.pos.service;

import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.PojoUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;

public class TestOrderService extends AbstractUnitTest{
    @Autowired
    private final OrderService orderService = new OrderService();

    @Test
    public void testAdd() throws ApiException {
        //checking the add operation
        orderService.add(PojoUtil.getOrderPojo());
        List<OrderPojo> list = orderService.getAll();
        Assert.assertEquals(list.size(),1);
    }

    @Test
    public void testGetById() throws ApiException {
        orderService.add(PojoUtil.getOrderPojo());
        List<OrderPojo> list = orderService.getAll();

        OrderPojo data = orderService.get(list.get(0).getId());
        Assert.assertEquals(data.getId(),list.get(0).getId());
        Assert.assertEquals(data.getTimestamp(),list.get(0).getTimestamp());

    }

    @Test
    public void testGetAll() throws ApiException {
        orderService.add(PojoUtil.getOrderPojo());
        orderService.add(PojoUtil.getOrderPojo());
        orderService.add(PojoUtil.getOrderPojo());

        List<OrderPojo> list = orderService.getAll();
        Assert.assertEquals(list.size(),3);
    }

    @Test
    public void testGetCheck(){
        try {
            orderService.getCheck(1000);
        }catch (ApiException e){
            return;
        }
        Assert.fail();
    }

    @Test
    public void testSelectRange() throws ApiException {
        orderService.add(PojoUtil.getOrderPojo());
        orderService.add(PojoUtil.getOrderPojo());
        orderService.add(PojoUtil.getOrderPojo());

        List<OrderPojo> list = orderService.selectRange(Instant.parse("2022-01-11T00:00:00Z"),Instant.parse("2024-01-11T00:00:00Z"));
        Assert.assertEquals(list.size(),3);
    }


}
