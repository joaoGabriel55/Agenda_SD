package com.quaresma.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.quaresma.model.User;
import com.quaresma.persist.GenericDaoImpl;
import com.quaresma.util.HibernateUtil;

public class UserDaoImpl extends GenericDaoImpl<User, Integer> implements UserDao {
	public UserDaoImpl() {
		super(User.class);
	}

	@SuppressWarnings("deprecation")
	@Override
	public User validUser(String userName, String pass) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Criteria consulta = session.createCriteria(User.class);
			consulta.add(Restrictions.eq("userName",userName))
					.add(Restrictions.eq("password", pass));
			User resultado = (User) consulta.uniqueResult();
			
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			session.close();
		}
	}

}
