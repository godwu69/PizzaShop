package com.example.api_gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Getter
@Component
public class JwtUtil {
    private final SecretKey accessToken;

    public JwtUtil(@Value("${jwt.secret}") String jwtSecret) {
        byte[] decodedAccessKey = Base64.getDecoder().decode(jwtSecret.getBytes());
        this.accessToken = Keys.hmacShaKeyFor(decodedAccessKey);
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(accessToken).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            System.out.println("Invalid Token: " + e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token, SecretKey key) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public String getUsernameFromJwtToken(String token) {
        return getClaims(token, accessToken).getSubject();
    }

    public String getRoleFromJwtToken(String token) {
        return getClaims(token, accessToken).get("role", String.class);
    }

    public String getUserIdFromJwtToken(String token) {
        return getClaims(token, accessToken).get("user_id", String.class);
    }
}