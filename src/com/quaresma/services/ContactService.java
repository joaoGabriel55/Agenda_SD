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

import com.quaresma.dao.ContactDao;
import com.quaresma.dao.ContactDaoImpl;
import com.quaresma.dao.UserDao;
import com.quaresma.dao.UserDaoImpl;
import com.quaresma.exceptions.CustomNoContentException;
import com.quaresma.exceptions.OutputMessage;
import com.quaresma.model.Contact;
import com.quaresma.model.User;
import com.quaresma.security.Secured;

@Path("/contact")
public class ContactService {

	@POST
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Contact contact, @Context SecurityContext securityContext) {

		ContactDao contactDao = new ContactDaoImpl();
		UserDao userDao = new UserDaoImpl();
		try {
			String idUser = securityContext.getUserPrincipal().getName();//Id do usuario via token
			
			User user = userDao.findById(Integer.parseInt(idUser));
			
			if(user != null) {
				contact.setUser(user);
				contactDao.save(contact);
			} else {
				return Response.status(Response.Status.NOT_MODIFIED).entity(contact).build();
			}

		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
		} finally {
			userDao.close();
			contactDao.close();
		}

		return Response.status(Response.Status.CREATED).entity(new OutputMessage(200,"OK! Contato salvo com sucesso!")).build();

	}

	@DELETE
	@Secured
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") int id, @Context SecurityContext securityContext) throws CustomNoContentException{
		ContactDao ContactDao = new ContactDaoImpl();
		Contact Contact = ContactDao.findById(id);

		//return Response.status(Response.Status.NO_CONTENT).build();
		if (Contact == null)
			throw new CustomNoContentException();
		
		ContactDao.delete(Contact);
		
		return Response.status(Response.Status.OK)
				//.entity(new OutputMessage(200, "Contact Removida"))
				.entity(new OutputMessage(200,"Objeto removido por "+ securityContext.getUserPrincipal().getName()))
				.build();
	}

	@PUT
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(Contact t) {

		try {
			ContactDao ContactDao = new ContactDaoImpl();
			ContactDao.save(t);
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new OutputMessage(500, e.getMessage()))
					.build();
		}

		return Response.status(Response.Status.OK).entity(t).build();

	}

	@GET
	@Secured
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listById(@PathParam("id") int id) {
		try {
			ContactDao ContactDAO = new ContactDaoImpl();
			Contact obj = ContactDAO.findById(id);
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
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAll(@QueryParam("orderby") @DefaultValue("id") String orderBy,
							@QueryParam("sort") @DefaultValue("asc") String sort) {
		try {
			ContactDao ContactDAO = new ContactDaoImpl();
			List<Contact> obj;
			if (sort.equals("desc")) {
				obj = ContactDAO.findAll(Order.desc(orderBy));
			} else {
				obj = ContactDAO.findAll(Order.asc(orderBy));
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
