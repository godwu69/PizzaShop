package com.example.api_gateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://localhost:8100/api/v1/users")
public interface UserServiceClient {
    @GetMapping("/check-token")
    Boolean isTokenBlacklisted(@RequestParam("token") String token);
}
