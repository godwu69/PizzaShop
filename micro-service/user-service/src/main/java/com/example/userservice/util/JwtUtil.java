package com.example.userservice.util;

import com.example.userservice.repository.TokenBlacklistRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Getter
@Component
public class JwtUtil {

    private final SecretKey accessToken;
    private final SecretKey refreshToken;
    private final Long jwtExpiration;
    private final Long jwtRefreshExpiration;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    public JwtUtil(TokenBlacklistRepository tokenBlacklistRepository, @Value("${jwt.secret}") String jwtSecret, @Value("${jwt.refresh}") String jwtRefresh, @Value("${jwt.expiration}") Long jwtExpiration, @Value("${jwt.refreshExpiration}") Long jwtRefreshExpiration) {
        byte[] decodedAccessKey = Base64.getDecoder().decode(jwtSecret.getBytes());
        byte[] decodedRefreshKey = Base64.getDecoder().decode(jwtRefresh.getBytes());
        this.accessToken = Keys.hmacShaKeyFor(decodedAccessKey);
        this.refreshToken = Keys.hmacShaKeyFor(decodedRefreshKey);
        this.jwtExpiration = jwtExpiration;
        this.jwtRefreshExpiration = jwtRefreshExpiration;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }


    public String generateToken(String user_id, String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("user_id", user_id)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .setId(UUID.randomUUID().toString())
                .signWith(accessToken, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpiration))
                .signWith(refreshToken, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.findByToken(token).isPresent();
    }

    public boolean isValidToken(String token) {
        if (isTokenBlacklisted(token)) {
            System.out.println("Token is blacklisted.");
            return false;
        }

        if (isExpired(token)) {
            System.out.println("Token has expired.");
            return false;
        }

        try {
            Jwts.parserBuilder().setSigningKey(accessToken).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            System.out.println("Invalid Token: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Token parsing error: " + e.getMessage());
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

    public boolean isExpired(String token) {
        Date expiration = getExpirationFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationFromToken(String token) {
        return getClaims(token, accessToken).getExpiration();
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
