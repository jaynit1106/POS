package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.ProductPojo;

@Repository
public class ProductDao extends AbstractDao {
	private static final String check_barcode="select p from ProductPojo p where barcode=:barcode";
	private static final String product_exist="select p from ProductPojo p where brandId=:brandId and name=:name";
	private static final String GET_ALL_BARCODE="select barcode from ProductPojo p";
	

	
	public ProductPojo barcodeExist(String barcode) {
		TypedQuery<ProductPojo> query = getQuery(check_barcode, ProductPojo.class);
		query.setParameter("barcode", barcode);
		return getSingle(query);
	}
	
	public ProductPojo getProductByNameAndBrandId(int brandId,String name) {
		TypedQuery<ProductPojo> query = getQuery(product_exist, ProductPojo.class);
		query.setParameter("brandId", brandId);
		query.setParameter("name", name);
		return getSingle(query);
	}

	public List<String> getBarcodeList(){
		TypedQuery<String> query = getQuery(GET_ALL_BARCODE, String.class);
		return query.getResultList();
	}
}