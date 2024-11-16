package com.example.userservice.controller;

import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.request.RefreshTokenRequest;
import com.example.userservice.dto.request.RegisterRequest;
import com.example.userservice.dto.request.UpdateUserRequest;
import com.example.userservice.dto.response.LoginResponse;
import com.example.userservice.dto.response.RegisterResponse;
import com.example.userservice.dto.response.UserResponse;
import com.example.userservice.model.Users;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = userService.register(registerRequest);
        return ResponseEntity.ok(registerResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String newAccessToken = userService.refreshAccessToken(refreshTokenRequest);

        Map<String, String> response = new HashMap<>();
        response.put("access_token", newAccessToken);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-token")
    public ResponseEntity<Boolean> isTokenBlacklisted(@RequestParam("token") String token) {
        boolean isBlacklisted = userService.isTokenBlacklisted(token);
        return ResponseEntity.ok(isBlacklisted);
    }

    @GetMapping("/list")
    public UserResponse<Page<Users>> getAllUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone_number,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return userService.getAllUsers(name, email, phone_number, page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse<Users>> getUserById(@PathVariable int id) {
        UserResponse<Users> response = userService.getUserById(id);
        return ResponseEntity.status(Integer.parseInt(response.getCode())).body(response);
    }

    @PostMapping
    public ResponseEntity<UserResponse<Users>> createUser(@RequestBody Users user) {
        UserResponse<Users> response = userService.createUser(user);
        return ResponseEntity.status(Integer.parseInt(response.getCode())).body(response);
    }

    @PutMapping()
    public ResponseEntity<UserResponse<Users>> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        UserResponse<Users> response = userService.updateUser(updateUserRequest);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{user_id}")
    public ResponseEntity<UserResponse<Void>> deleteUser(@PathVariable int user_id) {
        UserResponse<Void> response = userService.deleteUser(user_id);
        return ResponseEntity.ok(response);
    }
}
