package com.quaresma.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UncaughtExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable exception) {
		// TODO Auto-generated method stub
		return Response
				.status(getStatusType(exception))
				.entity(new OutputMessage(getStatusType(exception).getStatusCode(), "generic:" + exception.getMessage()))
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	private Response.StatusType getStatusType(Throwable ex){
		if(ex instanceof WebApplicationException)
			return ((WebApplicationException)ex).getResponse().getStatusInfo();
		else
			return Response.Status.INTERNAL_SERVER_ERROR;
	}

}
