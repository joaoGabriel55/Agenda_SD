package com.quaresma.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.quaresma.model.Contact;
import com.quaresma.persist.GenericDaoImpl;
import com.quaresma.util.HibernateUtil;

public class ContactDaoImpl extends GenericDaoImpl<Contact, Integer> implements ContactDao {
	public ContactDaoImpl() {
		super(Contact.class);
	}

	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	@Override
	public List<Contact> findAllByContactUser(Integer idUser, String order, String orderBy) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Query query = session.getSession().createQuery(
					"SELECT c.id, c.name, c.phone, c.rating, c.description FROM Contact c "
					+ " INNER JOIN c.user as u "
					+ " WHERE u.id = " + idUser + " ORDER BY " + "c." + orderBy + " " + order);
			
			
			List<Contact> contacts = query.list();
	
			return contacts;
		} finally {
			session.close();
		}
	}


}
