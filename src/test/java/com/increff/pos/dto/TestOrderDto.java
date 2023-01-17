package com.increff.pos.dto;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.util.PojoUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TestOrderDto extends  AbstractUnitTest{
    @Autowired
    private final OrderDao orderDao = new OrderDao();

    @Test
    public void testAdd(){
        //checking the add operation
        orderDao.insert(PojoUtil.getOrderPojo());
    }
}
