package com.example.userservicespring.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Setter
@Getter
public class RegisterRequest {
    private String username;
    private String name;
    private String email;
    private String password;
    Optional<String> role;

    public RegisterRequest(String username, String name, String email, String password, Optional<String> role) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public RegisterRequest() {
    }

}
