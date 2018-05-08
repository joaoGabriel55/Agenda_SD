/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quaresma.util;

import java.security.Key;
import java.util.Calendar;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.quaresma.dao.UserDao;
import com.quaresma.dao.UserDaoImpl;
import com.quaresma.model.User;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 *
 * @author Taniro
 */
public class TokenUtil {
    
    public static String criaToken(String username, Integer idUser) {                
        
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS384;
        
        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("35tdsxz");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
 
        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .claim("usuario", username)
                .claim("create", Calendar.getInstance().getTime())
                .signWith(signatureAlgorithm, signingKey);//Token completo e compactado
        
        String id = Long.toString(idUser);
        
        String compact = builder.compact();
        
        return compact + ";" + id;
    }
    
    public static void validaToken(String token, int idUser) throws Exception {
        // Verifica se o token existe no banco de dados, caso não existir lançar uma exceção
    	try {
			UserDao userDao = new UserDaoImpl();
			User user = userDao.findById(idUser);
			
			if(user.getToken() != null) {
				if(token == user.getToken())
					throw new Exception();
			}
				
		} finally {
			// TODO: handle finally clause
		}
    	
    }
}
