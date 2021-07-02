package com.hotel.jwt.security;



import java.io.Serializable;
import java.security.Key;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Value("${security.secret}")
    private String secret;

    @Value("${security.expiration}")
    private String expirationTime;

    private Key key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final var expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
    
    
}
