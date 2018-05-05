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
	public List<Contact> findAllByContactUser(Integer idUser) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Query query = session.getSession().createQuery(
					"SELECT c FROM Contact c "
					+ " INNER JOIN c.user as u "
					+ " WHERE u.id = " + idUser);
			
			
			List<Contact> contacts = query.list();
	
			return contacts;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	@Override
	public Contact findContactUser(Integer idUser, Integer idContact) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Contact contact = new Contact();
		try {
			Query query = session.getSession().createQuery(
					"SELECT c FROM Contact c "
					+ " INNER JOIN c.user as u "
					+ " WHERE u.id = " + idUser + " AND c.id = "+ idContact);
			
			contact = (Contact) query.uniqueResult();
			
		} catch (Exception e) {
			e.getMessage();
		} finally {
			session.close();
		}
		return contact;
	}

}
