package com.increff.pos.dto;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PojoUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TestProductDto extends AbstractUnitTest{
    @Autowired
    private  ProductDto productDto ;
    @Autowired
    private  ProductDao productDao ;
    @Autowired
    private  BrandDao brandDao ;
    @Autowired
    private   BrandDto brandDto ;

    @Test
    public void testAddAndGetAll() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAllBrands();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name","abcdabcd",29,list.get(0).getId());
        productDao.insert(p);

        //checking the add and getAll operation
        List<ProductData> productDataList = productDto.getAllProducts();
        assertEquals(productDataList.get(0).getBarcode(),"abcdabcd");
        assertEquals(productDataList.get(0).getMrp(),"29.00");
        assertEquals(productDataList.get(0).getName(),"name");
        assertEquals(productDataList.get(0).getBrand(),"brand");
        assertEquals(productDataList.get(0).getCategory(),"category");
    }

    @Test
    public void testUpdate() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAllBrands();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name","abcdabcd",29,list.get(0).getId());
        productDao.insert(p);
        List<ProductData> productDataList = productDto.getAllProducts();

        //checking the update Operation
        productDto.updateProduct(productDataList.get(0).getId(),PojoUtil.getProductForm("brand","category","changedname","changeds",290));
        productDataList = productDto.getAllProducts();
        assertEquals(productDataList.get(0).getBarcode(),"abcdabcd");
        assertEquals(productDataList.get(0).getMrp(),"290.00");
        assertEquals(productDataList.get(0).getName(),"changedname");
        assertEquals(productDataList.get(0).getBrand(),"brand");
        assertEquals(productDataList.get(0).getCategory(),"category");
    }

    @Test
    public void testForProductUniqueness() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand", "category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAllBrands();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name", "abcdabcd", 29, list.get(0).getId());
        productDao.insert(p);

        //checking same brand category name exception
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Product already Exists");
        productDto.addProduct(PojoUtil.getProductForm("brand", "category", "name", "abcdabce", 290));

    }

    @Test
    public void testForBarcodeUniqueness() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand", "category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAllBrands();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name", "abcdabcd", 29, list.get(0).getId());
        productDao.insert(p);

        //Checking same barcode exception
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Barcode already Exists");
        productDto.addProduct(PojoUtil.getProductForm("brand", "category", "names", "abcdabcd", 290));

    }

    @Test
    public void testForBrandAndCategoryExists() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand", "category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAllBrands();

        //checking if API exception is thrown when brand and category does not exist
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand And Category Does Not Exist");
        productDto.addProduct(PojoUtil.getProductForm("brand1", "category1", "names", "abcdabcd", 290));

    }

    @Test
    public void testForIncorrectBarcode() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand", "category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAllBrands();


        //checks for incorrect barcode format
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Barcode Should be of 8 characters");
        productDto.addProduct(PojoUtil.getProductForm("brand", "category", "names", "abcd", 290));

    }
    @Test
    public void testForIncorrectMrp() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand", "category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAllBrands();

        //check for negative mrp
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("MRP should be positive");
        productDto.addProduct(PojoUtil.getProductForm("brand", "category", "names", "abcdabcf", -290));

    }

    @Test
    public void testGetAllBarcode(){
        //adding multiple products
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",20,brandDto.getAllBrands().get(0).getId()));
        productDao.insert(PojoUtil.getProductPojo("name1","abcdabce",20,brandDto.getAllBrands().get(0).getId()));

        //checking for the list of barcodes
        assertEquals(2,productDto.getBarcodeList().size());
    }
}
