package com.quaresma.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.quaresma.dao.UserDao;
import com.quaresma.dao.UserDaoImpl;
import com.quaresma.exceptions.OutputMessage;
import com.quaresma.model.User;
import com.quaresma.util.TokenUtil;

@Path("/authentic")
public class AuthenticService {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticUser(User user, @Context SecurityContext securityContext) {
		UserDao userDao = new UserDaoImpl();
		try {
			User userLogged = validUser(user.getUserName(), user.getPassword());
			String token = TokenUtil.criaToken(user.getUserName(), userLogged.getId());
			
			userLogged.setToken(token);
			userDao.save(userLogged);
			
			return Response.status(Response.Status.OK).entity(new OutputMessage(200, token)).build();
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(new OutputMessage(Response.Status.UNAUTHORIZED.getStatusCode(),
							"Permission denied " + e.getMessage()))
					.build();
		}

	}

	public User validUser(String userName, String pass) throws Exception {
		UserDao userDao = new UserDaoImpl();

		User user = userDao.validUser(userName, pass);

		if (user == null)
			throw new Exception("User doesn't exists.");
		else
			return user;
	}

}
