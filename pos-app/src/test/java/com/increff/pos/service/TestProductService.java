package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.ProductData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.PojoUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class TestProductService extends AbstractUnitTest{
    @Autowired
    private final BrandService brandService = new BrandService();
    @Autowired
    private final BrandDao brandDao = new BrandDao();
    @Autowired
    private final ProductService productService = new ProductService();
    @Autowired
    private final ProductDao productDao = new ProductDao();

    @Test
    public void testAdd() throws ApiException {
        //adds brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));

        //adds Product
        productService.add(PojoUtil.getProductPojo("name","barcode",20,brandService.getAll().get(0).getId()));

        //checking if the product is added
        List<ProductPojo> list = productService.getAll();
        assertEquals(1,list.size());
    }

    @Test
    public void testGetAll(){
        //adds brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));

        //adds product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",20,brandService.getAll().get(0).getId()));
        productDao.insert(PojoUtil.getProductPojo("name1","abcdabce",40,brandService.getAll().get(0).getId()));
        productDao.insert(PojoUtil.getProductPojo("name2","abcdabcf",30,brandService.getAll().get(0).getId()));

        //checking the getAll function
        List<ProductPojo> list = productService.getAll();
        assertEquals(3,list.size());
    }

    @Test
    public void testGetById() throws ApiException {
        //adds brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //adds product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",20,brandService.getAll().get(0).getId()));

        List<ProductPojo> list = productService.getAll();

        ProductPojo p = productService.get(list.get(0).getId());
        assertEquals(p.getName(),"name");
        assertEquals(p.getId(),list.get(0).getId());
        assertEquals(p.getBarcode(),"abcdabcd");
        assertEquals(p.getMrp(),20,0);
    }
    @Test
    public void testUpdate() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(brand);
        List<BrandPojo> list = brandService.getAll();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name","abcdabcd",29,list.get(0).getId());
        productDao.insert(p);
        List<ProductPojo> productDataList = productService.getAll();

        //checking the update Operation
        productService.update(productDataList.get(0).getId(),PojoUtil.getProductPojo("changedName","abcdabcd",290.00,brandService.getAll().get(0).getId()));
        productDataList = productService.getAll();
        assertEquals(productDataList.get(0).getBarcode(),"abcdabcd");
        assertEquals(productDataList.get(0).getMrp(),290,0);
        assertEquals(productDataList.get(0).getName(),"changedname");
    }

    @Test
    public void testGetCheck(){
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(brand);
        List<BrandPojo> list = brandService.getAll();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name","abcdabcd",29,list.get(0).getId());
        productDao.insert(p);

        try{
            productService.get(100000);
        }catch (ApiException e){
            return;
        }
        fail();
    }
    @Test
    public void testGetProductByBarcode() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(brand);
        List<BrandPojo> list = brandService.getAll();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name","abcdabcd",29,list.get(0).getId());
        productDao.insert(p);

        List<ProductPojo> lists = productService.getAll();
        ProductPojo pojo = productService.getProductByBarcode("abcdabcd");

        assertEquals(pojo.getId(),lists.get(0).getId());
        assertEquals(pojo.getName(),lists.get(0).getName());
    }

    @Test
    public void testGetProductByNameAndBrandId(){
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(brand);
        List<BrandPojo> list = brandService.getAll();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name","abcdabcd",29,list.get(0).getId());
        productDao.insert(p);

        //getting the object using brandId and name
        ProductPojo pojo = productService.getProductByNameAndBrandId(list.get(0).getId(),"name");
        assertNotNull(pojo);
    }

    @Test
    public void testGetAllBarcode(){
        //adding multiple products
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",20,brandService.getAll().get(0).getId()));
        productDao.insert(PojoUtil.getProductPojo("name1","abcdabce",20,brandService.getAll().get(0).getId()));

        //checking for the list of barcodes
        assertEquals(2,productService.getBarcodeList().size());
    }
}
