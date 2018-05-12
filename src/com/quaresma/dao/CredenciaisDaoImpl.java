package com.quaresma.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import com.quaresma.model.Credenciais;
import com.quaresma.persist.GenericDaoImpl;
import com.quaresma.util.HibernateUtil;

public class CredenciaisDaoImpl extends GenericDaoImpl<Credenciais, Integer> implements CredenciaisDao {
	public CredenciaisDaoImpl() {
		super(Credenciais.class);
	}
	
	
	/** Credencias == User  */
	@SuppressWarnings("deprecation")
	@Override
	public Credenciais validUser(String userName, String pass) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Criteria consulta = session.createCriteria(Credenciais.class);
			consulta.add(Restrictions.eq("username",userName))
					.add(Restrictions.eq("password", pass));
			Credenciais resultado = (Credenciais) consulta.uniqueResult();
			
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			session.close();
		}
	}


	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public Credenciais findByTokenUser(String token) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Credenciais credenciais = new Credenciais();
		String tokenClean = token.trim();
		try {
			Query query = session.getSession().createQuery(
					"SELECT c FROM Credenciais c "
					+ " WHERE c.token = :tokenClean");
			
			query.setParameter("tokenClean", tokenClean);
			
			credenciais = (Credenciais) query.uniqueResult();
			
			return credenciais;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			session.close();
		}
	}

}
