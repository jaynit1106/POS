package com.increff.pos.dto;

import com.increff.pos.dao.*;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PojoUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestOrderDto extends  AbstractUnitTest{
    @Autowired
    private  BrandDao brandDao = new BrandDao();
    @Autowired
    private  ProductDao productDao ;
    @Autowired
    private  InventoryDao inventoryDao ;
    @Autowired
    private  OrderDao orderDao ;
    @Autowired
    private  BrandDto brandDto ;
    @Autowired
    private  ProductDto productDto ;
    @Autowired
    private  OrderDto orderDto ;
    @Autowired
    private  OrderItemDto orderItemDto ;

    @Test
    public void testAdd() throws ApiException, ParserConfigurationException, TransformerException, IOException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAllBrands().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAllProducts().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());

        //add order-item
        List<OrderItemForm> lists = new ArrayList<>();
        lists.add(PojoUtil.getOrderItemForm(11,"abcdabcd",20));
        orderDto.addOrder(lists);

        //checking the order-item
        List<OrderItemData> list = orderItemDto.getOrderItemByOrderID(orderDto.getAllOrders().get(0).getId());
        assertEquals(list.get(0).getOrderId(),orderDto.getAllOrders().get(0).getId());
        assertEquals(list.get(0).getQuantity(),11);
        assertEquals(new Double(list.get(0).getSellingPrice()),(Double) 20.0,0);
        assertEquals(list.get(0).getProductId(),productDto.getAllProducts().get(0).getId());
    }

    @Test
    public void testForInvalidQuantity() throws ApiException, ParserConfigurationException, IOException, TransformerException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAllBrands().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAllProducts().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-item

        //checking for API exception for invalid quantity
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please Enter a Valid Quantity for abcdabcd");
        List<OrderItemForm> list = new ArrayList<>();
        list.add(PojoUtil.getOrderItemForm(-20,"abcdabcd",20));
        orderDto.addOrder(list);

        //checking for API exception for invalid sellingPrice
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Prodjuct already Exists");
        list = new ArrayList<>();
        list.add(PojoUtil.getOrderItemForm(10,"abcdabcd",-100));
        orderDto.addOrder(list);

    }

    @Test
    public void testForInvalidSellingPrice() throws ApiException, ParserConfigurationException, IOException, TransformerException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAllBrands().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAllProducts().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-item


        //checking for API exception for invalid sellingPrice
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please Enter a Valid Price for abcdabcd");
        List<OrderItemForm> list = new ArrayList<>();
        list.add(PojoUtil.getOrderItemForm(10,"abcdabcd",-100));
        orderDto.addOrder(list);

    }

    @Test
    public void testForInvalidProduct() throws ApiException, ParserConfigurationException, IOException, TransformerException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAllBrands().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAllProducts().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());


        //checking for API exception for invalid product
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Product abcdabce Does Not exists");
        List<OrderItemForm> list = new ArrayList<>();
        list.add(PojoUtil.getOrderItemForm(10,"abcdabce",100));
        orderDto.addOrder(list);

    }

    @Test
    public void testForExcessQuantity() throws ApiException, ParserConfigurationException, IOException, TransformerException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAllBrands().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAllProducts().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-item


        //checking for API exception for excess quantity
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("[{\"abcdabcd\":\"Only 100 pieces left for abcdabcd\"}]");
        List<OrderItemForm> list = new ArrayList<>();
        list.add(PojoUtil.getOrderItemForm(120,"abcdabcd",20));
        orderDto.addOrder(list);
    }

    @Test
    public void testCreatePdf(){
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",20,brandDao.selectAll(BrandPojo.class).get(0).getId()));
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDao.selectAll(ProductPojo.class).get(0).getId()));

        List<OrderItemForm> list = new ArrayList<>();
        list.add(PojoUtil.getOrderItemForm(20,"abcdabcd",10));


        //checking creating the pdf part
        try {
            int id = orderDto.addOrder(list);
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

    @Test
    public void testGetOrderById() throws ApiException {
        orderDao.insert(PojoUtil.getOrderPojo());
        Assert.assertNotNull(orderDto.getOrderById(orderDao.selectAll(OrderPojo.class).get(0).getId()));
    }

}
