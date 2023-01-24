package com.increff.pos.dto;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.model.OrderData;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PojoUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TestOrderDto extends  AbstractUnitTest{
    @Autowired
    private final OrderDao orderDao = new OrderDao();
    @Autowired
    private final OrderDto orderDto = new OrderDto();

    @Test
    public void testAdd() throws ApiException {
        //checking the add operation
        orderDto.add();

        List<OrderData> list = orderDto.getAll();
        Assert.assertEquals(list.size(),1);
    }

    @Test
    public void testGetById() throws ApiException {
        orderDto.add();
        List<OrderData> list = orderDto.getAll();

        OrderData data = orderDto.get(list.get(0).getId());
        Assert.assertEquals(data.getId(),list.get(0).getId());
        Assert.assertEquals(data.getTimestamp(),list.get(0).getTimestamp());

    }

}
