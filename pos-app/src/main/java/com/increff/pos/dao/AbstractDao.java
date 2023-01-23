package com.increff.pos.dao;


import com.increff.pos.pojo.BrandPojo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


public abstract class AbstractDao {
	
	@PersistenceContext
	private EntityManager em;

	protected <T> T getSingle(TypedQuery<T> query) {
		return query.getResultList().stream().findFirst().orElse(null);
	}
	
	public <T> void insert(T pojo) {
		em.persist(pojo);
	}
	
	protected <T> TypedQuery<T> getQuery(String jpql, Class<T> clazz) {
		return em.createQuery(jpql, clazz);
	}

	public <T> List<T> selectAll(Class<T> clazz){
		final String SELECT_ALL = "select p from "+clazz.getName()+" p";
		TypedQuery<T> query = getQuery(SELECT_ALL, clazz);
		return query.getResultList();
	}

	public <T> T select(int id,Class<T> clazz) {
		final String SELECT_ID= "select p from "+clazz.getName()+" p where id=:id";
		TypedQuery<T> query = getQuery(SELECT_ID, clazz);
		query.setParameter("id", id);
		return getSingle(query);
	}

	protected EntityManager em() {
		return em;
	}

}
