package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.dto.OrderDto;
import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.PojoUtil;
import io.swagger.annotations.Api;
import org.hibernate.criterion.Order;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TestOrderService extends AbstractUnitTest{

    @Autowired
    private final OrderDao orderDao = new OrderDao();
    @Autowired
    private final BrandDao brandDao = new BrandDao();
    @Autowired
    private final ProductDao productDao = new ProductDao();
    @Autowired
    private final InventoryDao inventoryDao = new InventoryDao();
    @Autowired
    private final OrderService orderService = new OrderService();
    @Autowired
    private final OrderItemDto orderItemDto = new OrderItemDto();

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
    public void testGetCheck() throws ApiException {
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

        List<OrderPojo> list = orderService.selectRange(Instant.parse("2023-01-24T00:00:00Z"),Instant.parse("2023-01-24T23:59:59Z"));
        Assert.assertEquals(list.size(),3);
    }


}
