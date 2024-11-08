package com.example.userservice.service;

import com.example.userservice.dto.*;
import com.example.userservice.model.Users;
import com.example.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Value("${jwt.refreshExpiration}")
    private Long refreshExpiration;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        LoginResponse response = new LoginResponse();

        try {
            Users user = userRepository.findByUsername(loginRequest.getUsername());

            if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                String accessToken = generateToken(user, jwtExpiration);
                String refreshToken = generateToken(user, refreshExpiration);

                response.setCode("200");
                response.setMessage("Successfully");

                LoginResponse.Data data = new LoginResponse.Data();
                data.setAccess_token(accessToken);
                data.setRefresh_token(refreshToken);
                data.setExpires_in(jwtExpiration / 1000);
                data.setRefresh_expires_in(refreshExpiration / 1000);
                String role = user.getRole().name();
                data.setRole(Collections.singletonList(role));

                response.setData(data);
            } else {
                response.setCode("401");
                response.setMessage("Invalid username or password");
            }
        } catch (Exception e) {
            response.setCode("500");
            response.setMessage("Internal server error");
        }

        return response;
    }

    public RegisterResponse register(RegisterRequest registerRequest) {
        RegisterResponse response = new RegisterResponse();

        try {
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                response.setCode("400");
                response.setMessage("Username already exists");
                return response;
            }

            Users user = new Users();
            user.setEmail(registerRequest.getEmail());
            user.setUsername(registerRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setName(registerRequest.getName());

            userRepository.save(user);

            response.setCode("200");
            response.setMessage("Successfully");

        } catch (Exception e) {
            response.setCode("500");
            response.setMessage("Internal server error");
            System.err.println("Error during registration: " + e.getMessage());
        }

        return response;
    }

    private String generateToken(Users user, Long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(SignatureAlgorithm.HS256, jwtSecret)
                    .compact();
        } catch (Exception e) {
            System.err.println("Error generating token: " + e.getMessage());
            throw new RuntimeException("Could not generate token", e);
        }
    }

    public String refreshAccessToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(refreshToken)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = claims.getSubject();
        Users user = userRepository.findByUsername(username);

        if (user != null) {
            return generateToken(user, jwtExpiration);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
