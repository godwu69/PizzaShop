package com.example.userservice.dto;

import java.util.List;

public class LoginResponse {
    private String code;
    private String message;
    private Data data;

    public static class Data {
        private String access_token;
        private String refresh_token;
        private long expires_in;
        private long refresh_expires_in;
        private List<String> role;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public long getRefresh_expires_in() {
            return refresh_expires_in;
        }

        public void setRefresh_expires_in(long refresh_expires_in) {
            this.refresh_expires_in = refresh_expires_in;
        }

        public long getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(long expires_in) {
            this.expires_in = expires_in;
        }

        public List<String> getRole() {
            return role;
        }

        public void setRole(List<String> role) {
            this.role = role;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
