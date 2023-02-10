package com.increff.pos.dto;

import com.increff.pos.dao.*;
import com.increff.pos.model.data.BrandReportData;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.BrandReportForm;
import com.increff.pos.model.form.InventoryReportForm;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PojoUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;

public class TestReportsDto extends AbstractUnitTest{
    @Autowired
    private  ReportsDto reportsDto ;
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
        orderItemDao.insert(PojoUtil.getOrderItemPojo(3,11,productDao.selectAll(ProductPojo.class).get(0).getId(),20));

        SalesReportForm salesReportForm = new SalesReportForm();
        salesReportForm.setEndDate(Instant.now());
        salesReportForm.setStartDate(Instant.now().minusSeconds(2000));
        List<SalesReportData> list = reportsDto.getSalesReport(salesReportForm);
        Assert.assertEquals(list.get(0).getBrand(),"brand");
        Assert.assertEquals(list.get(0).getCategory(),"category");
        Assert.assertEquals(list.get(0).getQuantity(),11);
        Assert.assertEquals(list.get(0).getRevenue(),220,0);
    }

    @Test
    public void testBrandReport(){
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand1","category"));
        brandDao.insert(PojoUtil.getBrandPojo("brand1","category1"));
        brandDao.insert(PojoUtil.getBrandPojo("brand","category2"));

        BrandReportForm brandReportForm = new BrandReportForm();
        brandReportForm.setBrand("brand1");
        brandReportForm.setCategory("All");
        List<BrandReportData> list = reportsDto.getBrandReport(brandReportForm);

        Assert.assertEquals(2,list.size());
        Assert.assertEquals("brand1",list.get(0).getBrand());
        Assert.assertEquals("category",list.get(0).getCategory());


    }


}
