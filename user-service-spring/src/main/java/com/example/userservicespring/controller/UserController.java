package com.example.userservicespring.controller;

import com.example.userservicespring.dto.request.DeleteUserRequest;
import com.example.userservicespring.dto.request.UpdateStatusRequest;
import com.example.userservicespring.dto.request.UpdateUserRequest;
import com.example.userservicespring.dto.response.UserResponse;
import com.example.userservicespring.model.Users;
import com.example.userservicespring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserResponse<List<Users>>> getAllUsers() {
        UserResponse<List<Users>> response = userService.getAllUsers();
        return ResponseEntity.status(Integer.parseInt(response.getCode())).body(response);
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

    @PutMapping("/update")
    public ResponseEntity<UserResponse<Users>> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        UserResponse<Users> response = userService.updateUser(updateUserRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/status")
    public ResponseEntity<UserResponse<Users>> updateStatus(@RequestBody UpdateStatusRequest updateStatusRequest) {
        UserResponse<Users> response = userService.updateStatus(updateStatusRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<UserResponse<Void>> deleteUser(@RequestBody DeleteUserRequest deleteUserRequest) {
        UserResponse<Void> response = userService.deleteUser(deleteUserRequest);
        return ResponseEntity.ok(response);
    }
}
