package com.quaresma.dao;

import com.quaresma.model.Credenciais;
import com.quaresma.persist.GenericDao;

public interface CredenciaisDao extends GenericDao<Credenciais, Integer> {

	public Credenciais validUser(String userName, String pass);
	
	public Credenciais findByTokenUser(String token);
}
