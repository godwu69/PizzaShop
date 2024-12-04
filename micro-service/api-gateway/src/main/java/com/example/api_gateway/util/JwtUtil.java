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

@Component
@Getter
public class JwtUtil {
    private final SecretKey accessToken;

    public JwtUtil(@Value("${jwt.secret}") String jwtSecret) {
        byte[] decodedAccessKey = Base64.getDecoder().decode(jwtSecret.getBytes());
        this.accessToken = Keys.hmacShaKeyFor(decodedAccessKey);
    }

    public boolean isValidToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(accessToken).build().parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }
}
