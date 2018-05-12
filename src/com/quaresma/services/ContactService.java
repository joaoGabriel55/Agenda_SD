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
		Contact contact = ContactDao.findById(id);

		//return Response.status(Response.Status.NO_CONTENT).build();
		if (contact == null)
			throw new CustomNoContentException();
		
		if(contact.getUser() != null) {
			if(contact.getUser().getId() == Integer.parseInt((securityContext.getUserPrincipal().getName()))) {
				ContactDao.delete(contact);				
			} else {
				throw new CustomNoContentException();
			}
		}
		
		
		return Response.status(Response.Status.OK)
				//.entity(new OutputMessage(200, "Contact Removida"))
				.entity(new OutputMessage(200,"Objeto removido por "+ securityContext.getUserPrincipal().getName()))
				.build();
	}

	@PUT
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(Contact contact, @Context SecurityContext securityContext) {

		try {
			if(contact.getUser().getId() == Integer.parseInt((securityContext.getUserPrincipal().getName()))) {
				ContactDao contactDao = new ContactDaoImpl();
				contactDao.save(contact);
			}
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new OutputMessage(500, e.getMessage()))
					.build();
		}

		return Response.status(Response.Status.OK).entity(contact).build();

	}

	@GET
	@Secured
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listById(@PathParam("id") int idContact, @Context SecurityContext securityContext) {
		try {
			ContactDao ContactDAO = new ContactDaoImpl();
			int idUser = Integer.parseInt(securityContext.getUserPrincipal().getName());
			Contact obj = ContactDAO.findById(idContact);
			
			if (obj == null) {
				return Response.status(Response.Status.NO_CONTENT).build();
			} else {
				if(idUser == obj.getUser().getId())
					return Response.status(Response.Status.OK).entity(obj).build();
				else
					return Response.status(Response.Status.NO_CONTENT).build();

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
							@QueryParam("sort") @DefaultValue("asc") String sort,
							@Context SecurityContext securityContext) {
		try {
			ContactDao ContactDAO = new ContactDaoImpl();
			List<Contact> obj;
			int idUser = Integer.parseInt(securityContext.getUserPrincipal().getName());
			if (sort.equals("desc")) {
				obj = ContactDAO.findAllByContactUser(idUser, sort, orderBy);
			} else {
				obj = ContactDAO.findAllByContactUser(idUser, sort, orderBy);
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
