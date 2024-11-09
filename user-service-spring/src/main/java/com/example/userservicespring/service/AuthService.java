package com.example.userservicespring.service;

import com.example.userservicespring.dto.request.LoginRequest;
import com.example.userservicespring.dto.request.RefreshTokenRequest;
import com.example.userservicespring.dto.request.RegisterRequest;
import com.example.userservicespring.dto.response.LoginResponse;
import com.example.userservicespring.dto.response.RegisterResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    RegisterResponse register(RegisterRequest registerRequest);
    String refreshAccessToken(RefreshTokenRequest refreshTokenRequest);
}