package com.example.userservicespring.service.impl;

import com.example.userservicespring.dto.request.UpdateUserRequest;
import com.example.userservicespring.dto.response.UserResponse;
import com.example.userservicespring.model.Users;
import com.example.userservicespring.repository.UserRepository;
import com.example.userservicespring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse<List<Users>> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return new UserResponse<>("200", "Successfully", users);
    }

    @Override
    public UserResponse<Users> getUserById(int user_id) {
        Optional<Users> user = userRepository.findById(user_id);
        return user.map(value -> new UserResponse<>("200", "Successfully", value))
                .orElseGet(() -> new UserResponse<>("404", "User not found"));
    }

    @Override
    public UserResponse<Users> createUser(Users user) {
        try {
            Users createdUser = userRepository.save(user);
            return new UserResponse<>("201", "User created successfully", createdUser);
        } catch (Exception e) {
            return new UserResponse<>("500", "Error creating user");
        }
    }

    @Override
    public UserResponse<Users> updateUser(UpdateUserRequest updateUserRequest) {
        try {
            Users user = userRepository.findById(updateUserRequest.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found with id " + updateUserRequest.getUser_id()));

            updateUserRequest.getEmail().ifPresent(user::setEmail);
            updateUserRequest.getUsername().ifPresent(user::setUsername);
            updateUserRequest.getPhone().ifPresent(user::setPhone);
            updateUserRequest.getPassword().ifPresent(user::setPassword);
            updateUserRequest.getName().ifPresent(user::setName);
            updateUserRequest.getGender().ifPresent(gender -> user.setGender(Users.Gender.valueOf(gender)));
            updateUserRequest.getDob().ifPresent(dob -> user.setDob(LocalDate.parse(dob)));
            updateUserRequest.getAddress().ifPresent(user::setAddress);
            updateUserRequest.getRole().ifPresent(role -> user.setRole(Users.Role.valueOf(role)));
            updateUserRequest.getStatus().ifPresent(status -> user.setStatus(Users.Status.valueOf(status)));
            user.setUpdated_at(LocalDateTime.now());

            user.setUpdated_at(LocalDateTime.now());
            Users updatedUser = userRepository.save(user);
            return new UserResponse<>("200", "User updated successfully", updatedUser);
        } catch (RuntimeException e) {
            return new UserResponse<>("404", e.getMessage(), null);
        } catch (Exception e) {
            return new UserResponse<>("500", "Error updating user: " + e.getMessage());
        }
    }


    @Override
    public UserResponse<Void> deleteUser(int user_id) {
        try {
            Users user = userRepository.findById(user_id)
                    .orElseThrow(() -> new RuntimeException("User not found with id " + user_id));
            userRepository.delete(user);
            return new UserResponse<>("204", "User deleted successfully");
        } catch (Exception e) {
            return new UserResponse<>("500", "Error deleting user");
        }
    }
}
