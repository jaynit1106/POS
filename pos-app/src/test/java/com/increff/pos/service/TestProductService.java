package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.PojoUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class TestProductService extends AbstractUnitTest{
    @Autowired
    private  BrandService brandService ;
    @Autowired
    private  BrandDao brandDao ;
    @Autowired
    private  ProductService productService ;
    @Autowired
    private  ProductDao productDao;

    @Test
    public void testAdd() throws ApiException {
        //adds brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));

        //adds Product
        productService.add(PojoUtil.getProductPojo("name","barcode2",20,brandService.getAllBrands().get(0).getId()));

        //checking if the product is added
        List<ProductPojo> list = productService.getAllProducts();
        assertEquals(1,list.size());
    }

    @Test
    public void testGetAll(){
        //adds brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));

        //adds product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",20,brandService.getAllBrands().get(0).getId()));
        productDao.insert(PojoUtil.getProductPojo("name1","abcdabce",40,brandService.getAllBrands().get(0).getId()));
        productDao.insert(PojoUtil.getProductPojo("name2","abcdabcf",30,brandService.getAllBrands().get(0).getId()));

        //checking the getAll function
        List<ProductPojo> list = productService.getAllProducts();
        assertEquals(3,list.size());
    }

    @Test
    public void testGetById() throws ApiException {
        //adds brand
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        //adds product
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",20,brandService.getAllBrands().get(0).getId()));

        List<ProductPojo> list = productService.getAllProducts();

        ProductPojo p = productService.getProductById(list.get(0).getId());
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
        List<BrandPojo> list = brandService.getAllBrands();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name","abcdabcd",29,list.get(0).getId());
        productDao.insert(p);
        List<ProductPojo> productDataList = productService.getAllProducts();

        //checking the update Operation
        productService.updateProduct(productDataList.get(0).getId(),PojoUtil.getProductPojo("changedname","abcdabce",290.00,brandService.getAllBrands().get(0).getId()));
        productDataList = productService.getAllProducts();
        assertEquals(productDataList.get(0).getBarcode(),"abcdabcd");
        assertEquals(productDataList.get(0).getMrp(),290,0);
        assertEquals(productDataList.get(0).getName(),"changedname");
    }

    @Test
    public void testGetCheck(){
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(brand);
        List<BrandPojo> list = brandService.getAllBrands();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name","abcdabcd",29,list.get(0).getId());
        productDao.insert(p);

        try{
            productService.getProductById(100000);
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
        List<BrandPojo> list = brandService.getAllBrands();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name","abcdabcd",29,list.get(0).getId());
        productDao.insert(p);

        List<ProductPojo> lists = productService.getAllProducts();
        ProductPojo pojo = productService.getProductByBarcode("abcdabcd");

        assertEquals(pojo.getId(),lists.get(0).getId());
        assertEquals(pojo.getName(),lists.get(0).getName());
    }

    @Test
    public void testGetProductByNameAndBrandId(){
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(brand);
        List<BrandPojo> list = brandService.getAllBrands();

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
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",20,brandService.getAllBrands().get(0).getId()));
        productDao.insert(PojoUtil.getProductPojo("name1","abcdabce",20,brandService.getAllBrands().get(0).getId()));

        //checking for the list of barcodes
        assertEquals(2,productService.getBarcodeList().size());
    }
}
