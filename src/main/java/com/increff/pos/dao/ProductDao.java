package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.ProductPojo;

@Repository
public class ProductDao extends AbstractDao {
	private static String delete_id = "delete from ProductPojo p where id=:id";
	private static String delete_brandId = "delete from ProductPojo p where brandId=:brandId";
	private static String select_id = "select p from ProductPojo p where id=:id";
	private static String select_all = "select p from ProductPojo p";
	private static String select_all_products = "select p from ProductPojo p where brandId=:brandId";
	private static String check_barcode="select p from ProductPojo p where barcode=:barcode";
	private static String product_exist="select p from ProductPojo p where brandId=:brandId and name=:name";
	
	@PersistenceContext
	private EntityManager em;

	public void insert(ProductPojo p) {
		em.persist(p);
	}
	
	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public int deleteProducts(int brandId) {
		Query query = em.createQuery(delete_brandId);
		query.setParameter("brandId", brandId);
		return query.executeUpdate();
	}
	
	
	public ProductPojo select(int id) {
		TypedQuery<ProductPojo> query = getQuery(select_id, ProductPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<ProductPojo> selectAll() {
		TypedQuery<ProductPojo> query = getQuery(select_all, ProductPojo.class);
		return query.getResultList();
	}
	
	public List<ProductPojo> selectAllProducts(int brandId) {
		TypedQuery<ProductPojo> query = getQuery(select_all_products, ProductPojo.class);
		query.setParameter("brandId", brandId);
		return query.getResultList();
	}
	
	public ProductPojo barcodeExist(String barcode) {
		TypedQuery<ProductPojo> query = getQuery(check_barcode, ProductPojo.class);
		query.setParameter("barcode", barcode);
		return getSingle(query);
	}
	
	public ProductPojo productExist(int brandId,String name) {
		TypedQuery<ProductPojo> query = getQuery(product_exist, ProductPojo.class);
		query.setParameter("brandId", brandId);
		query.setParameter("name", name);
		return getSingle(query);
	}
	
	public void update(ProductPojo p) {
	}
}
