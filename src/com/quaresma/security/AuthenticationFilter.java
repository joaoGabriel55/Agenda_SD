package com.quaresma.security;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import com.quaresma.exceptions.CustomNotAuthorizedException;
import com.quaresma.util.TokenUtil;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Obtem o valor do dado do HEADER AUTHORIZATION da requisição HTTP
		final String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		
		// Verifica existe o header e se ele está no formato correto
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new CustomNotAuthorizedException("Usuário não está logado.");
		}
		
		// Obtem o token
		String tokenTemp = authorizationHeader.substring("Bearer".length()).trim();
		
		String[] token = tokenTemp.split(";");
		final String idUser = token[1];
		
		try { 
			// Valida o token
			TokenUtil.validaToken(token[0], Integer.parseInt(idUser));
		} catch (Exception e) {
			// Aborta a execução
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}

		requestContext.setSecurityContext(new SecurityContext() {

			@Override
			public Principal getUserPrincipal() {
				return new Principal() {
					
					/**getName() == id user*/
					@Override
					public String getName() {
						// usuario/ID/etc que veio do banco após a validacao do token
						return idUser;
					}

				};
			}

			@Override
			public boolean isUserInRole(String role) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isSecure() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public String getAuthenticationScheme() {
				// TODO Auto-generated method stub
				return null;
			}
		});

	}

}
