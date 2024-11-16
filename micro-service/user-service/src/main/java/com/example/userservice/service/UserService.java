package com.example.userservice.service;

import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.request.RefreshTokenRequest;
import com.example.userservice.dto.request.RegisterRequest;
import com.example.userservice.dto.request.UpdateUserRequest;
import com.example.userservice.dto.response.LoginResponse;
import com.example.userservice.dto.response.RegisterResponse;
import com.example.userservice.dto.response.UserResponse;
import com.example.userservice.model.Users;
import org.springframework.data.domain.Page;

public interface UserService {
    LoginResponse login(LoginRequest loginRequest);
    RegisterResponse register(RegisterRequest registerRequest);
    String refreshAccessToken(RefreshTokenRequest refreshTokenRequest);
    UserResponse<Page<Users>> getAllUsers(String name, String email, String phone_number, int page, int size);
    UserResponse<Users> getUserById(int user_id);
    UserResponse<Users> createUser(Users user);
    UserResponse<Users> updateUser(UpdateUserRequest updateUserRequest);
    UserResponse<Void> deleteUser(int user_id);
    boolean isTokenBlacklisted(String token);
    void logout(String token);
}
