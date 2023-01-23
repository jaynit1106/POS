package com.increff.pos.dto;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PojoUtil;
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
    }
}
