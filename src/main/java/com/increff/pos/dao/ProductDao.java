package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.ProductPojo;

@Repository
public class ProductDao extends AbstractDao {
	private static final String select_id = "select p from ProductPojo p where id=:id";
	private static final String select_all = "select p from ProductPojo p";
	private static final String check_barcode="select p from ProductPojo p where barcode=:barcode";
	private static final String product_exist="select p from ProductPojo p where brandId=:brandId and name=:name";
	
	
	public ProductPojo select(int id) {
		TypedQuery<ProductPojo> query = getQuery(select_id, ProductPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<ProductPojo> selectAll() {
		TypedQuery<ProductPojo> query = getQuery(select_all, ProductPojo.class);
		return query.getResultList();
	}
	
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

}
