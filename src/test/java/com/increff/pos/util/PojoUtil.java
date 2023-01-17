package com.increff.pos.util;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.*;
import com.increff.pos.pojo.*;

public class PojoUtil {
    public static BrandPojo getBrandPojo(String brand,String category){
        BrandPojo p =new BrandPojo();
        p.setBrand(brand);
        p.setCategory(category);
        return p;
    }
    public static BrandForm getBrandForm(String brand, String category){
        BrandForm p =new BrandForm();
        p.setBrand(brand);
        p.setCategory(category);
        return p;
    }

    public static ProductPojo getProductPojo(String name,String barcode,double mrp , int brandId ){
        ProductPojo p =new ProductPojo();
        p.setBrandId(brandId);
        p.setMrp(mrp);
        p.setName(name);
        p.setBarcode(barcode);
        return p;
    }

    public static ProductForm getProductForm(String brand, String category , String name,String barcode,double mrp){
        ProductForm p =new ProductForm();
        p.setBrand(brand);
        p.setCategory(category);
        p.setBarcode(barcode);
        p.setName(name);
        p.setMrp(mrp);
        return p;
    }

    public static InventoryPojo getInventoryPojo(int quantity, int id){
        InventoryPojo p =new InventoryPojo();
        p.setQuantity(quantity);
        p.setId(id);
        return p;
    }

    public static InventoryForm getInventoryForm(int quantity, String barcode){
        InventoryForm p =new InventoryForm();
        p.setQuantity(quantity);
        p.setBarcode(barcode);
        return p;
    }

    public static OrderPojo getOrderPojo(){
        OrderPojo p =new OrderPojo();
        return p;
    }
    public static OrderItemPojo getOrderItemPojo(int orderId,int quantity,int productId,double sellingPrice){
        OrderItemPojo p =new OrderItemPojo();
        p.setOrderId(orderId);
        p.setQuantity(quantity);
        p.setProductId(productId);
        p.setSellingPrice(sellingPrice);
        return p;
    }
    public static OrderItemForm getOrderItemForm(int quantity, String barcode, double sellingPrice){
        OrderItemForm p =new OrderItemForm();
        p.setQuantity(quantity);
        p.setSellingPrice(sellingPrice);
        p.setBarcode(barcode);
        return p;
    }
}
