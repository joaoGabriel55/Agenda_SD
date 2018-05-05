package com.quaresma.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SuppressWarnings("serial")
public class CustomNoContentException extends WebApplicationException{

	public CustomNoContentException() {
		super(
				Response
				.status(Response.Status.NO_CONTENT)
				.type(MediaType.APPLICATION_JSON)
				.build()
		);
	}
	
	
	
}
