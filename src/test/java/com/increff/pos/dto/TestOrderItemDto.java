package com.increff.pos.dto;

import com.increff.pos.dao.*;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PojoUtil;
import io.swagger.annotations.Api;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestOrderItemDto extends AbstractUnitTest{
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
    private final InventoryDto inventoryDto = new InventoryDto();
    @Autowired
    private final BrandDto brandDto = new BrandDto();
    @Autowired
    private final ProductDto productDto = new ProductDto();
    @Autowired
    private final OrderDto orderDto = new OrderDto();
    @Autowired
    private final OrderItemDto orderItemDto = new OrderItemDto();

    @Test
    public void testAddAndGetAll() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAll().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-item
        orderItemDao.insert(PojoUtil.getOrderItemPojo(orderDto.getAll().get(0).getId(),20,productDto.getAll().get(0).getId(),100));

        //checking the getAll functionality
        List<OrderItemData> list = orderItemDto.getAll();
        assertEquals(list.get(0).getOrderId(),orderDto.getAll().get(0).getId());
        assertEquals(list.get(0).getQuantity(),20);
        assertEquals(list.get(0).getSellingPrice(),(double) 100,0);
        assertEquals(list.get(0).getProductId(),productDto.getAll().get(0).getId());
    }

    @Test
    public void testForValidFormats() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAll().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-item
        boolean flag = false;
        try {
            //checking for API exception for invalid quantity
            List<OrderItemForm> list = new ArrayList<>();
            list.add(PojoUtil.getOrderItemForm(-20,"abcdabcd",100.23));
            orderItemDto.add(list);
        }catch (ApiException e){
            flag = true;

        } catch (ParserConfigurationException e) {

        } catch (TransformerException e) {

        }
        if(Objects.equals(flag,false))fail();

        flag = false;
        try {
            //checking for API exception for invalid sellingPrice
            List<OrderItemForm> list = new ArrayList<>();
            list.add(PojoUtil.getOrderItemForm(10,"abcdabcd",-100));
            orderItemDto.add(list);
        }catch (ApiException e){
            flag = true;
        } catch (ParserConfigurationException e) {

        } catch (TransformerException e) {

        }
        if(Objects.equals(flag,false))fail();
        try {
            //checking for API exception for invalid product
            List<OrderItemForm> list = new ArrayList<>();
            list.add(PojoUtil.getOrderItemForm(10,"abcdabce",100));
            orderItemDto.add(list);
        }catch (ApiException e){
            return;
        } catch (ParserConfigurationException e) {
            return;
        } catch (TransformerException e) {
            return;
        }
        fail();
    }

    @Test
    public void testForExcessQuantity() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAll().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-item

        try {
            //checking for API exception for excess quantity
            List<OrderItemForm> list = new ArrayList<>();
            list.add(PojoUtil.getOrderItemForm(120,"abcdabcd",100.23));
            orderItemDto.add(list);
        }catch (ApiException e){
            return;
        } catch (ParserConfigurationException e) {
            return;
        } catch (TransformerException e) {
            return;
        }
        fail();
    }

    @Test
    public void testGetOrderItemBYOrderId() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAll().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-item
        orderItemDao.insert(PojoUtil.getOrderItemPojo(orderDto.getAll().get(0).getId(),20,productDto.getAll().get(0).getId(),100));

        List<OrderItemData> list = orderItemDto.getOrderItemByOrderID(orderDto.getAll().get(0).getId());
        OrderItemData item = list.get(0);
        assertEquals(item.getName(),"name");
        assertEquals(item.getBarcode(),"abcdabcd");
        assertEquals(item.getSellingPrice(),100,0);
        assertEquals(item.getQuantity(),20);
        assertEquals(item.getOrderId(),orderDto.getAll().get(0).getId());
    }

}


