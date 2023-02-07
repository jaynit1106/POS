package com.increff.pos.dto;

import com.increff.pos.dao.*;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PojoUtil;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestOrderDto extends  AbstractUnitTest{
    @Autowired
    private final BrandDao brandDao = new BrandDao();
    @Autowired
    private final ProductDao productDao = new ProductDao();
    @Autowired
    private final InventoryDao inventoryDao = new InventoryDao();
    @Autowired
    private final OrderDao orderDao = new OrderDao();
    @Autowired
    private final BrandDto brandDto = new BrandDto();
    @Autowired
    private final ProductDto productDto = new ProductDto();
    @Autowired
    private final OrderDto orderDto = new OrderDto();
    @Autowired
    private final OrderItemDto orderItemDto = new OrderItemDto();

    @Test
    public void testAdd() throws ApiException, ParserConfigurationException, TransformerException, IOException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAll().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());

        //add order-item
        List<OrderItemForm> lists = new ArrayList<>();
        lists.add(PojoUtil.getOrderItemForm(11,"abcdabcd",100.23));
        orderDto.add(lists);

        //checking the order-item
        List<OrderItemData> list = orderItemDto.getAll();
        assertEquals(list.get(0).getOrderId(),orderDto.getAll().get(0).getId());
        assertEquals(list.get(0).getQuantity(),11);
        assertEquals(new Double(list.get(0).getSellingPrice()),(Double) 100.23,0);
        assertEquals(list.get(0).getProductId(),productDto.getAll().get(0).getId());
    }

    @Test
    public void testForInvalidQuantity() throws ApiException, ParserConfigurationException, IOException, TransformerException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAll().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-item

        //checking for API exception for invalid quantity
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please Enter a Valid Quantity for abcdabcd");
        List<OrderItemForm> list = new ArrayList<>();
        list.add(PojoUtil.getOrderItemForm(-20,"abcdabcd",100.23));
        orderDto.add(list);

        //checking for API exception for invalid sellingPrice
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Prodjuct already Exists");
        list = new ArrayList<>();
        list.add(PojoUtil.getOrderItemForm(10,"abcdabcd",-100));
        orderDto.add(list);

    }

    @Test
    public void testForInvalidSellingPrice() throws ApiException, ParserConfigurationException, IOException, TransformerException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAll().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-item


        //checking for API exception for invalid sellingPrice
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please Enter a Valid Price for abcdabcd");
        List<OrderItemForm> list = new ArrayList<>();
        list.add(PojoUtil.getOrderItemForm(10,"abcdabcd",-100));
        orderDto.add(list);

    }

    @Test
    public void testForInvalidProduct() throws ApiException, ParserConfigurationException, IOException, TransformerException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAll().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());


        //checking for API exception for invalid product
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Product abcdabce Does Not exists");
        List<OrderItemForm> list = new ArrayList<>();
        list.add(PojoUtil.getOrderItemForm(10,"abcdabce",100));
        orderDto.add(list);

    }

    @Test
    public void testForExcessQuantity() throws ApiException, ParserConfigurationException, IOException, TransformerException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAll().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAll().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-item


        //checking for API exception for excess quantity
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("[{\"abcdabcd\":\"Only 100 pieces left for abcdabcd\"}]");
        List<OrderItemForm> list = new ArrayList<>();
        list.add(PojoUtil.getOrderItemForm(120,"abcdabcd",100.23));
        orderDto.add(list);
    }

    @Test
    public void testCreatePdf(){
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",20,brandDao.selectAll(BrandPojo.class).get(0).getId()));
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDao.selectAll(ProductPojo.class).get(0).getId()));

        List<OrderItemForm> list = new ArrayList<>();
        list.add(PojoUtil.getOrderItemForm(20,"abcdabcd",90));


        //checking creating the pdf part
        try {
            int id = orderDto.add(list);
            orderDto.generatedPdf(id);
        } catch (ParserConfigurationException e) {
            Assert.fail();
        } catch (IOException e) {
            Assert.fail();
        } catch (TransformerException e) {
            Assert.fail();
        } catch (ApiException e) {
            Assert.fail();
        }
    }

}
