package com.example.userservicespring.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusRequest {
    private int user_id;
    private String status;

    public UpdateStatusRequest() {}

    public UpdateStatusRequest(int user_id, String status) {
        this.user_id = user_id;
        this.status = status;
    }
}
