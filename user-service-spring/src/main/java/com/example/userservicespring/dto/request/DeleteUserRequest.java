package com.example.userservicespring.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteUserRequest {
    private int user_id;

    public DeleteUserRequest() {
    }

    public DeleteUserRequest(int user_id) {
        this.user_id = user_id;
    }

}
