package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.UserPojo;

@Repository
public class UserDao extends AbstractDao {

	private static String SELECT_EMAIL = "select p from UserPojo p where email=:email";


	public UserPojo select(String email) {
		TypedQuery<UserPojo> query = getQuery(SELECT_EMAIL, UserPojo.class);
		query.setParameter("email", email);
		return getSingle(query);
	}

	public void update(UserPojo p) {
	}


}
