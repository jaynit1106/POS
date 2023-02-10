package com.increff.pos.util;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class MapUtil {
    @Autowired
    private static ProductService productService;

    public static HashMap<Integer,Double> getBrandsRevenue(HashMap<Integer,Double> revenueProducts, List<ProductPojo> products){

        HashMap<Integer,Double> revenueBrands = new HashMap<>();
        for(ProductPojo product : products) {
            double price = 0;
            int brandId = product.getBrandId();
            int productId = product.getId();
            if(revenueBrands.get(brandId)!=null)price = revenueBrands.get(brandId);
            revenueBrands.put(brandId, price + revenueProducts.get(productId));
        }

        return revenueBrands;
    }

    public static HashMap<Integer,Integer> getBrandsQuantity(HashMap<Integer,Integer> quantityProducts,List<ProductPojo> products){

        HashMap<Integer,Integer> quantityBrands = new HashMap<>();
        for(ProductPojo product : products) {
            Integer quant = 0;
            int brandId = product.getBrandId();
            int productId = product.getId();
            if(quantityBrands.get(brandId)!=null)quant = quantityBrands.get(brandId);
            quantityBrands.put(brandId, quant + quantityProducts.get(productId));
        }

        return quantityBrands;
    }

    public static HashMap<Integer,Double> getProductsRevenue(List<OrderItemPojo> orders){

        HashMap<Integer,Double> revenueProducts = new HashMap<>();
        for(OrderItemPojo item : orders) {
            double price = 0;
            int productId = item.getProductId();
            if(revenueProducts.get(productId)!=null)price = revenueProducts.get(productId);
            revenueProducts.put(productId, price + item.getQuantity()*item.getSellingPrice());
        }

        return revenueProducts;
    }



}
