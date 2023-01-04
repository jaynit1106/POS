package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;

@Repository
public class ProductDao extends AbstractDao {
	private static String delete_id = "delete from ProductPojo p where id=:id";
	private static String select_id = "select p from ProductPojo p where id=:id";
	private static String select_all = "select p from ProductPojo p";
	private static String get_brand_id="select p from BrandPojo p where brand=:brand and category=:category";
	private static String check_barcode="select p from ProductPojo p where barcode=:barcode";
	
	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(ProductPojo p) {
		em.persist(p);
	}
	
	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
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
	
	public List<BrandPojo> getBrandId(String brand,String category) {
		TypedQuery<BrandPojo> query = getQuery(get_brand_id, BrandPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return query.getResultList();
	}
	
	public boolean barcodeExist(String barcode) {
		TypedQuery<ProductPojo> query = getQuery(check_barcode, ProductPojo.class);
		query.setParameter("barcode", barcode);
		if(query.getResultList().isEmpty())return false;
		return true;
	}
	
	public void update(ProductPojo p) {
	}
}
