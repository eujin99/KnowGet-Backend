package com.knowget.knowgetbackend.global.config.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.access.token.expiration}")
	private long accessTokenExpiration;

	@Getter
	@Value("${jwt.refresh.token.expiration}")
	private long refreshTokenExpiration;

	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateAccessToken(String username) {
		return generateToken(username, accessTokenExpiration);
	}

	public String generateRefreshToken(String username) {
		return generateToken(username, refreshTokenExpiration);
	}

	public String generateToken(String username, long expiration) {
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder()
			.setClaims(claims)
			.setSubject(username)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expiration))
			.signWith(getSigningKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	public Boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(getSigningKey()).build()
			.parseClaimsJws(token)
			.getBody();
		return claims.getSubject();
	}

}
