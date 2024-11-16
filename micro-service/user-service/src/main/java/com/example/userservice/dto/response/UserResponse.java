package com.example.userservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse<T> {
    private String code;
    private String message;
    private T data;

    public UserResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public UserResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
