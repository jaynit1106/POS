package com.increff.pos.dao;

import java.util.List;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.BrandPojo;

@Repository
public class BrandDao extends AbstractDao {

	
	private static final String select_id = "select p from BrandPojo p where id=:id";
	private static final String select_all = "select p from BrandPojo p";
	private static final String check_id = "select p from BrandPojo p where (p.brand=:brand and p.category=:category)";
	private static final String select_by_brand = "select p from BrandPojo p where (p.brand=:brand)";
	private static final String select_by_category = "select p from BrandPojo p where (p.category=:category)";


	public BrandPojo select(int id) {
		TypedQuery<BrandPojo> query = getQuery(select_id, BrandPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query = getQuery(select_all, BrandPojo.class);
		return query.getResultList();
	}
	
	public BrandPojo checkRepeat(String brand , String category) {
		TypedQuery<BrandPojo> query = getQuery(check_id, BrandPojo.class);
		query.setParameter("category", category);
		query.setParameter("brand", brand);
		return getSingle(query);
	}
	
	public List<BrandPojo> selectByBrand(String brand) {
		TypedQuery<BrandPojo> query = getQuery(select_by_brand, BrandPojo.class);
		query.setParameter("brand", brand);
		return query.getResultList();
	}
	
	public List<BrandPojo> selectByCategory(String category) {
		TypedQuery<BrandPojo> query = getQuery(select_by_category, BrandPojo.class);
		query.setParameter("category", category);
		return query.getResultList();
	}

	public void update(BrandPojo p) {
	}



}
