package com.increff.pos.dto;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.ProductData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PojoUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

public class TestProductDto extends AbstractUnitTest{
    @Autowired
    private final ProductDto productDto = new ProductDto();
    @Autowired
    private final ProductDao productDao = new ProductDao();
    @Autowired
    private final BrandDao brandDao = new BrandDao();
    @Autowired
    private  final BrandDto brandDto = new BrandDto();

    @Test
    public void testAddAndGetAll() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAll();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name","abcdabcd",29,list.get(0).getId());
        productDao.insert(p);

        //checking the add and getAll operation
        List<ProductData> productDataList = productDto.getAll();
        assertEquals(productDataList.get(0).getBarcode(),"abcdabcd");
        assertEquals(productDataList.get(0).getMrp(),"29.00");
        assertEquals(productDataList.get(0).getName(),"name");
        assertEquals(productDataList.get(0).getBrand(),"brand");
        assertEquals(productDataList.get(0).getCategory(),"category");
    }

    @Test
    public void testGetById() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAll();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name","abcdabcd",29,list.get(0).getId());
        productDao.insert(p);
        List<ProductData> productDataList = productDto.getAll();

        //checking getById
        ProductData product = productDto.get(productDataList.get(0).getId());
        assertEquals(product.getBarcode(),"abcdabcd");
        assertEquals(product.getMrp(),"29.00");
        assertEquals(product.getName(),"name");
        assertEquals(product.getBrand(),"brand");
        assertEquals(product.getCategory(),"category");
    }
    @Test
    public void testUpdate() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand","category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAll();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name","abcdabcd",29,list.get(0).getId());
        productDao.insert(p);
        List<ProductData> productDataList = productDto.getAll();

        //checking the update Operation
        productDto.update(productDataList.get(0).getId(),PojoUtil.getProductForm("brand","category","changedname","abcdabcd",290));
        productDataList = productDto.getAll();
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
        List<BrandData> list = brandDto.getAll();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name", "abcdabcd", 29, list.get(0).getId());
        productDao.insert(p);

        //checking same brand category name exception
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Product already Exists");
        productDto.add(PojoUtil.getProductForm("brand", "category", "name", "abcdabce", 290));

    }

    @Test
    public void testForBarcodeUniqueness() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand", "category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAll();

        //create a product
        ProductPojo p = PojoUtil.getProductPojo("name", "abcdabcd", 29, list.get(0).getId());
        productDao.insert(p);

        //Checking same barcode exception
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Barcode already Exists");
        productDto.add(PojoUtil.getProductForm("brand", "category", "names", "abcdabcd", 290));

    }

    @Test
    public void testForBrandAndCategoryExists() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand", "category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAll();

        //checking if API exception is thrown when brand and category does not exist
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand And Category Does Not Exist");
        productDto.add(PojoUtil.getProductForm("brand1", "category1", "names", "abcdabcd", 290));

    }

    @Test
    public void testForIncorrectBarcode() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand", "category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAll();


        //checks for incorrect barcode format
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Barcode Should be of 8 characters");
        productDto.add(PojoUtil.getProductForm("brand", "category", "names", "abcd", 290));

    }
    @Test
    public void testForIncorrectMrp() throws ApiException {
        //create a brand
        BrandPojo brand = PojoUtil.getBrandPojo("brand", "category");
        brandDao.insert(brand);
        List<BrandData> list = brandDto.getAll();

        //check for negative mrp
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("MRP should be positive");
        productDto.add(PojoUtil.getProductForm("brand", "category", "names", "abcdabcf", -290));

    }

    @Test
    public void testGetAllBarcode(){
        //adding multiple products
        brandDao.insert(PojoUtil.getBrandPojo("brand","category"));
        productDao.insert(PojoUtil.getProductPojo("name","abcdabcd",20,brandDto.getAll().get(0).getId()));
        productDao.insert(PojoUtil.getProductPojo("name1","abcdabce",20,brandDto.getAll().get(0).getId()));

        //checking for the list of barcodes
        assertEquals(2,productDto.getBarcodeList().size());
    }
}
