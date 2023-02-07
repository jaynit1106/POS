package com.increff.pos.dao;

import java.util.List;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.BrandPojo;

@Repository
public class BrandDao extends AbstractDao {

	
	private static final String CHECK_ID = "select p from BrandPojo p where (p.brand=:brand and p.category=:category)";
	private static final String GET_BRAND = "select distinct brand from BrandPojo p";
	private static final String GET_CATEGORY = "select distinct category from BrandPojo p where p.brand=:brand";


	public BrandPojo getBrandByNameAndCategory(String brand , String category) {
		TypedQuery<BrandPojo> query = getQuery(CHECK_ID, BrandPojo.class);
		query.setParameter("category", category);
		query.setParameter("brand", brand);
		return getSingle(query);
	}

	public List<String> getBrandList(){
		TypedQuery<String> query = getQuery(GET_BRAND, String.class);
		return query.getResultList();
	}

	public List<String> getCategoryList(String brand){
		TypedQuery<String> query = getQuery(GET_CATEGORY, String.class);
		query.setParameter("brand",brand);
		return query.getResultList();
	}
}
