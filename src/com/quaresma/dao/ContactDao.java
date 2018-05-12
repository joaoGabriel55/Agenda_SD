package com.quaresma.dao;

import java.util.List;

import com.quaresma.model.Contact;
import com.quaresma.persist.GenericDao;

public interface ContactDao extends GenericDao<Contact, Integer> { 
	
	public List<Contact> findAllByContactUser(Integer idUser, String order, String orderBy);
	
}
