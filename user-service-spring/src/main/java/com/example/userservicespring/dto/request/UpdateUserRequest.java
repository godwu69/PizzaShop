package com.example.userservicespring.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Setter
@Getter
public class UpdateUserRequest {
    private int user_id;
    private Optional<String> email = Optional.empty();
    private Optional<String> username = Optional.empty();
    private Optional<String> phone = Optional.empty();
    private Optional<String> password = Optional.empty();
    private Optional<String> name = Optional.empty();
    private Optional<String> gender = Optional.empty();
    private Optional<String> dob = Optional.empty();
    private Optional<String> address = Optional.empty();
    private Optional<String> role = Optional.empty();
    private Optional<String> status = Optional.empty();

    public UpdateUserRequest(int user_id, Optional<String> email, Optional<String> username, Optional<String> phone,
                             Optional<String> password, Optional<String> name, Optional<String> gender,
                             Optional<String> dob, Optional<String> address, Optional<String> role,
                             Optional<String> status) {
        this.user_id = user_id;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.role = role;
        this.status = status;
    }

    public UpdateUserRequest() {
    }
}
