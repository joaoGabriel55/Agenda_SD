package com.quaresma.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SuppressWarnings("serial")
public class CustomNotAuthorizedException extends WebApplicationException{
	
	public CustomNotAuthorizedException() {
		super(
				Response
				.status(Response.Status.UNAUTHORIZED)
				.type(MediaType.APPLICATION_JSON)
				.build()
		);
	}
	
	public CustomNotAuthorizedException(String message) { 
		super(
				Response
				.status(Response.Status.UNAUTHORIZED)
				.entity(new OutputMessage(Response.Status.UNAUTHORIZED.getStatusCode(), message))
				.type(MediaType.APPLICATION_JSON)
				.build()
		);
	}
}
