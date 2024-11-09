package com.example.userservicespring.service;

import com.example.userservicespring.dto.request.UpdateUserRequest;
import com.example.userservicespring.dto.response.UserResponse;
import com.example.userservicespring.model.Users;

import java.util.List;

public interface UserService {
    UserResponse<List<Users>> getAllUsers();
    UserResponse<Users> getUserById(int user_id);
    UserResponse<Users> createUser(Users user);
    UserResponse<Users> updateUser(UpdateUserRequest updateUserRequest);
    UserResponse<Void> deleteUser(int user_id);
}
