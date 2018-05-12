package com.quaresma.dao;

import com.quaresma.model.User;
import com.quaresma.persist.GenericDaoImpl;

public class UserDaoImpl extends GenericDaoImpl<User, Integer> implements UserDao {
	public UserDaoImpl() {
		super(User.class);
	}

}
