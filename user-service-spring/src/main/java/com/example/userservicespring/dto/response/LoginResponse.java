package com.example.userservicespring.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class LoginResponse {
    private String code;
    private String message;
    private Data data;

    @Setter
    @Getter
    public static class
    Data {
        private String access_token;
        private String refresh_token;
        private long expires_in;
        private long refresh_expires_in;
        private List<String> role;

    }

}
