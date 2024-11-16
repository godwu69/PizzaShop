package com.example.userservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Setter
@Getter
public class UpdateUserRequest {
    private int user_id;
    private Optional<String> email = Optional.empty();
    private Optional<String> username = Optional.empty();
    private Optional<String> phone_number = Optional.empty();
    private Optional<String> password = Optional.empty();
    private Optional<String> name = Optional.empty();
    private Optional<String> gender = Optional.empty();
    private Optional<String> dob = Optional.empty();
    private Optional<String> address = Optional.empty();
    private Optional<String> role = Optional.empty();
    private Optional<String> status = Optional.empty();
    private LocalDateTime updated_at;

    public UpdateUserRequest(int user_id, Optional<String> email, Optional<String> username, Optional<String> phone_number,
                             Optional<String> password, Optional<String> name, Optional<String> gender,
                             Optional<String> dob, Optional<String> address, Optional<String> role,
                             Optional<String> status, LocalDateTime updated_at) {
        this.user_id = user_id;
        this.email = email;
        this.username = username;
        this.phone_number = phone_number;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.role = role;
        this.status = status;
        this.updated_at = updated_at;
    }

    public UpdateUserRequest() {
    }
}
