package com.increff.pos.service;

import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.PojoUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.FileOutputStream;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestOrderService extends AbstractUnitTest{
    @Autowired
    private  OrderService orderService;

    @Test
    public void testAdd() throws ApiException {
        //checking the add operation
        orderService.add(PojoUtil.getOrderPojo());
        List<OrderPojo> list = orderService.getAll();
        assertEquals(list.size(),1);
    }

    @Test
    public void testGetById() throws ApiException {
        orderService.add(PojoUtil.getOrderPojo());
        List<OrderPojo> list = orderService.getAll();

        OrderPojo data = orderService.get(list.get(0).getId());
        assertEquals(data.getId(),list.get(0).getId());
        assertEquals(data.getTimestamp(),list.get(0).getTimestamp());

    }

    @Test
    public void testGetAll() throws ApiException {
        orderService.add(PojoUtil.getOrderPojo());
        orderService.add(PojoUtil.getOrderPojo());
        orderService.add(PojoUtil.getOrderPojo());

        List<OrderPojo> list = orderService.getAll();
        assertEquals(list.size(),3);
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
        assertEquals(list.size(),3);
    }

}
