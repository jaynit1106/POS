package com.increff.pos.dto;

import com.increff.pos.dao.*;
import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PojoUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TestReportsDto extends AbstractUnitTest{
    @Autowired
    private final ReportsDto reportsDto = new ReportsDto();
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

    @Test
    public void testGetInventoryReport() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDao.selectAll(BrandPojo.class).get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDao.selectAll(ProductPojo.class).get(0).getId()));

        InventoryReportForm form = new InventoryReportForm();
        form.setBrand("All");
        form.setCategory("All");
        List<InventoryReportData> list = reportsDto.getInventoryReport(form);
        Assert.assertEquals(list.get(0).getBrand(),"brand");
        Assert.assertEquals(list.get(0).getCategory(),"category");
        Assert.assertEquals(list.get(0).getQuantity(),100);
    }

    @Test
    public void testGetSalesReport() throws ApiException {
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
        orderItemDao.insert(PojoUtil.getOrderItemPojo(orderDao.selectAll(OrderPojo.class).get(0).getId(),11,productDao.selectAll(ProductPojo.class).get(0).getId(),20));

        SalesReportForm salesReportForm = new SalesReportForm();
        salesReportForm.setEndDate(Instant.parse("2024-01-01T00:00:00Z"));
        salesReportForm.setStartDate(Instant.parse("2021-01-01T00:00:00Z"));
        List<SalesReportData> list = reportsDto.getSalesReport(salesReportForm);
        Assert.assertEquals(list.get(0).getBrand(),"brand");
        Assert.assertEquals(list.get(0).getCategory(),"category");
        Assert.assertEquals(list.get(0).getQuantity(),22);
        Assert.assertEquals(list.get(0).getRevenue(),440,0);
    }


}
