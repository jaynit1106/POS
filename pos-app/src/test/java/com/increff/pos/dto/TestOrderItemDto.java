package com.increff.pos.dto;

import com.increff.pos.dao.*;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PojoUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

public class TestOrderItemDto extends AbstractUnitTest{

    @Autowired
    private  BrandDao brandDao;
    @Autowired
    private  ProductDao productDao ;
    @Autowired
    private  InventoryDao inventoryDao ;
    @Autowired
    private  OrderDao orderDao ;
    @Autowired
    private  OrderItemDao orderItemDao ;
    @Autowired
    private  BrandDto brandDto ;
    @Autowired
    private  ProductDto productDto ;
    @Autowired
    private  OrderDto orderDto ;

    @Test
    public void testGetAll() throws ApiException, ParserConfigurationException, TransformerException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAllBrands().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAllProducts().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-item
        orderItemDao.insert(PojoUtil.getOrderItemPojo(orderDto.getAllOrders().get(0).getId(),20,productDto.getAllProducts().get(0).getId(),100));

        //checking the getAll functionality
        List<OrderItemData> list = orderDto.getOrderItemByOrderID(orderDto.getAllOrders().get(0).getId());
        assertEquals(list.get(0).getOrderId(),orderDto.getAllOrders().get(0).getId());
        assertEquals(list.get(0).getQuantity(),20);
        assertEquals(list.get(0).getSellingPrice(),"100.00");
        assertEquals(list.get(0).getProductId(),productDto.getAllProducts().get(0).getId());
    }

    @Test
    public void testGetOrderItemBYOrderId() throws ApiException {
        //create brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //create product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",30,brandDto.getAllBrands().get(0).getId()));
        //add inventory
        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDto.getAllProducts().get(0).getId()));
        //add order
        orderDao.insert(PojoUtil.getOrderPojo());
        //add order-item
        orderItemDao.insert(PojoUtil.getOrderItemPojo(orderDto.getAllOrders().get(0).getId(),20,productDto.getAllProducts().get(0).getId(),100));

        List<OrderItemData> list = orderDto.getOrderItemByOrderID(orderDto.getAllOrders().get(0).getId());
        OrderItemData item = list.get(0);
        assertEquals(item.getName(),"name");
        assertEquals(item.getBarcode(),"abcdabcd");
        assertEquals(item.getSellingPrice(),"100.00");
        assertEquals(item.getQuantity(),20);
        assertEquals(item.getOrderId(),orderDto.getAllOrders().get(0).getId());
    }


//    @Test
//    public void testRollback() {
//        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
//        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",20,brandDao.selectAll(BrandPojo.class).get(0).getId()));
//        inventoryDao.insert(PojoUtil.getInventoryPojo(100,productDao.selectAll(ProductPojo.class).get(0).getId()));
//        Assert.assertEquals(100,inventoryDao.selectAll(InventoryPojo.class).get(0).getQuantity());
//
//        List<OrderItemForm> list = new ArrayList<>();
//        list.add(PojoUtil.getOrderItemForm(20,"abcdabcd",90));
//        list.add(PojoUtil.getOrderItemForm(80,"abcdabcd",90));
//        list.add(PojoUtil.getOrderItemForm(100,"abcdabcd",90));
//
//        try {
//            orderItemDto.add(list);
//            fail();
//        }catch (ParserConfigurationException | TransformerException | IOException | RuntimeException e){
//            Assert.assertEquals(100,inventoryDao.selectAll(InventoryPojo.class).get(0).getQuantity());
//        } catch (ApiException e){
//            Assert.assertEquals(100,inventoryDao.selectAll(InventoryPojo.class).get(0).getQuantity());
//        }
//    }
//    @Transactional(rollbackOn = ApiException.class)
//    public void createOrder(List<OrderItemForm> list) throws ParserConfigurationException, IOException, TransformerException, ApiException {
//        try {
//            orderItemDto.add(list);
//        }catch (ApiException e){
//            throw new ApiException("Rollback");
//        }
//    }
}


