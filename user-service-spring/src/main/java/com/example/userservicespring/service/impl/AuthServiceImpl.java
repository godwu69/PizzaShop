package com.example.userservicespring.service.impl;

import com.example.userservicespring.dto.request.LoginRequest;
import com.example.userservicespring.dto.request.RefreshTokenRequest;
import com.example.userservicespring.dto.request.RegisterRequest;
import com.example.userservicespring.dto.response.LoginResponse;
import com.example.userservicespring.dto.response.RegisterResponse;
import com.example.userservicespring.model.Users;
import com.example.userservicespring.repository.UserRepository;
import com.example.userservicespring.service.AuthService;
import com.example.userservicespring.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        LoginResponse response = new LoginResponse();

        try {
            Users user = userRepository.findByUsername(loginRequest.getUsername());

            if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                String role = user.getRole().name();

                String accessToken = jwtUtil.generateToken(user.getUsername(), role);
                String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

                response.setCode("200");
                response.setMessage("Successfully");

                LoginResponse.Data data = new LoginResponse.Data();
                data.setAccess_token(accessToken);
                data.setRefresh_token(refreshToken);
                data.setExpires_in(jwtUtil.getJwtExpiration() / 1000);
                data.setRefresh_expires_in(jwtUtil.getJwtRefreshExpiration() / 1000);
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

    @Override
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
            registerRequest.getRole().ifPresent(role -> user.setRole(Users.Role.valueOf(role)));

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

    @Override
    public String refreshAccessToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        try {
            String username = jwtUtil.extractUsername(refreshToken);
            Users user = userRepository.findByUsername(username);

            if (user != null) {
                return jwtUtil.generateToken(user.getUsername(), user.getRole().name());
            } else {
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid refresh token");
        }
    }
}
