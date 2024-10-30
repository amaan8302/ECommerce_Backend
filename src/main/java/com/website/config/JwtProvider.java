package com.website.config;

import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {
	SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
	public String generateToken(Authentication auth) {
		String authorities = auth.getAuthorities().stream()
		        .map(GrantedAuthority::getAuthority)
		        .collect(Collectors.joining(","));
		String jwt=Jwts.builder()
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime()+86400000)) //24 hour time
				.claim("email",auth.getName())
				.claim("authorities", authorities)
				.signWith(key)
				.compact();
		
		return jwt;
	}	
	//TO CLAIM EMAIL
//	public String getEmailFromToken(String jwt) {
//	    jwt = jwt.substring(7);
//	    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(jwt).getBody();
//		String email = String.valueOf(claims.get("email"));
//	    return email;
//	}
	public String getEmailFromToken(String jwt) {
	    jwt = jwt.substring(7); // Remove the 'Bearer ' prefix
	    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody(); // Use parseClaimsJws instead
	    String email = String.valueOf(claims.get("email"));
	    return email;
	}
}









