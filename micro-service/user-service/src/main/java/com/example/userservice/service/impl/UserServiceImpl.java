package com.example.userservice.service.impl;

import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.request.RefreshTokenRequest;
import com.example.userservice.dto.request.RegisterRequest;
import com.example.userservice.dto.request.UpdateUserRequest;
import com.example.userservice.dto.response.LoginResponse;
import com.example.userservice.dto.response.RegisterResponse;
import com.example.userservice.dto.response.UserResponse;
import com.example.userservice.model.TokenBlackList;
import com.example.userservice.model.Users;
import com.example.userservice.repository.TokenBlacklistRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import com.example.userservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil, TokenBlacklistRepository tokenBlacklistRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.findByToken(token).isPresent();
    }

    @Override
    public void logout(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            Date expiryDate = jwtUtil.getExpirationFromToken(token);

            TokenBlackList tokenBlackList = new TokenBlackList();
            tokenBlackList.setToken(token);
            tokenBlackList.setExpiry_date(expiryDate);

            tokenBlacklistRepository.save(tokenBlackList);
        } catch (Exception e) {
            throw new RuntimeException("Failed to logout: " + e.getMessage());
        }
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        LoginResponse response = new LoginResponse();

        try {
            Users user = userRepository.findByUsername(loginRequest.getUsername());

            if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                String role = user.getRole().name();

                String accessToken = jwtUtil.generateToken(String.valueOf(user.getUser_id()), user.getUsername(), role);
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
            String username = jwtUtil.getUsernameFromJwtToken(refreshToken);
            Users user = userRepository.findByUsername(username);

            if (user != null) {
                return jwtUtil.generateToken(String.valueOf(user.getUser_id()), user.getUsername(), user.getRole().name());
            } else {
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid refresh token");
        }
    }

    @Override
    public UserResponse<Page<Users>> getAllUsers(String name, String email, String phone_number, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Users> users = userRepository.findUsersByParam(name, email, phone_number, pageable);

            return new UserResponse<>("200", "Successfully", users);
        } catch (Exception e) {
            System.err.println("Error fetching users: " + e.getMessage());
            return new UserResponse<>("500", "Error fetching users: " + e.getMessage(), null);
        }
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
            updateUserRequest.getPhone_number().ifPresent(user::setPhone_number);
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
