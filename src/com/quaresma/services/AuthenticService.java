package com.quaresma.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.quaresma.dao.CredenciaisDao;
import com.quaresma.dao.CredenciaisDaoImpl;
import com.quaresma.exceptions.OutputMessage;
import com.quaresma.model.Credenciais;
import com.quaresma.util.TokenUtil;

@Path("/authentic")
public class AuthenticService {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticUser(Credenciais user, @Context SecurityContext securityContext) {
		CredenciaisDao credenciaisDao = new CredenciaisDaoImpl();
		try {
			Credenciais userLogged = validUser(user.getUsername(), user.getPassword());
			String token = TokenUtil.criaToken(user.getUsername());
			
			userLogged.setToken(token);
			credenciaisDao.save(userLogged);
			
			return Response.status(Response.Status.OK).entity(new OutputMessage(200, token)).build();
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(new OutputMessage(Response.Status.UNAUTHORIZED.getStatusCode(),
							"Permission denied " + e.getMessage()))
					.build();
		}

	}

	public Credenciais validUser(String userName, String pass) throws Exception {
		CredenciaisDao userDao = new CredenciaisDaoImpl();

		Credenciais user = userDao.validUser(userName, pass);

		if (user == null)
			throw new Exception("User doesn't exists.");
		else
			return user;
	}

}
