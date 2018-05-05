package com.quaresma.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.hibernate.criterion.Order;

import com.quaresma.dao.UserDao;
import com.quaresma.dao.UserDaoImpl;
import com.quaresma.exceptions.CustomNoContentException;
import com.quaresma.exceptions.OutputMessage;
import com.quaresma.model.User;
import com.quaresma.security.Secured;

@Path("/user")
public class UserService {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(User t) {

		try {
			UserDao dao = new UserDaoImpl();
			dao.save(t);

		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}

		return Response.status(Response.Status.CREATED).entity(t).build();

	}

	@DELETE
	@Secured
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") int id, @Context SecurityContext securityContext) throws CustomNoContentException{
		UserDao UserDao = new UserDaoImpl();
		User User = UserDao.findById(id);

		//return Response.status(Response.Status.NO_CONTENT).build();
		if (User == null)
			throw new CustomNoContentException();

		UserDao.delete(User);
		
		return Response.status(Response.Status.OK)
				//.entity(new OutputMessage(200, "User Removida"))
				.entity(new OutputMessage(200,"Objeto removido por "+ securityContext.getUserPrincipal().getName()))
				.build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(User t) {

		try {
			UserDao UserDao = new UserDaoImpl();
			UserDao.save(t);
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new OutputMessage(500, e.getMessage()))
					.build();
		}

		return Response.status(Response.Status.OK).entity(t).build();

	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listById(@PathParam("id") int id) {
		try {
			UserDao UserDAO = new UserDaoImpl();
			User obj = UserDAO.findById(id);
			if (obj == null) {
				return Response.status(Response.Status.NO_CONTENT).build();
			} else {
				return Response.status(Response.Status.OK).entity(obj).build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new OutputMessage(500, e.getMessage()))
					.build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAll(@QueryParam("orderby") @DefaultValue("id") String orderBy,
							@QueryParam("sort") @DefaultValue("asc") String sort) {
		try {
			UserDao UserDAO = new UserDaoImpl();
			List<User> obj;
			if (sort.equals("desc")) {
				obj = UserDAO.findAll(Order.desc(orderBy));
			} else {
				obj = UserDAO.findAll(Order.asc(orderBy));
			}
			if (obj == null) {
				return Response.status(Response.Status.NO_CONTENT).build();
			} else {
				return Response.status(Response.Status.OK).entity(obj).build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new OutputMessage(500, e.getMessage()))
					.build();
		}
	}

}
